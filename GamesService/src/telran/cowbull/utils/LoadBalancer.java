package telran.cowbull.utils;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.concurrent.Flow.*;

public class LoadBalancer<T> implements Subscriber<T> {

	public int getCountReject() {
		return countReject;
	}

	int bufferSize;
	int index = 0;
	int countReject = 0;
	ExecutorService executor;
	SubmissionPublisher<T>[] publishers;
	Subscription subscription;
	Class<Subscriber<T>> clazz;

	public LoadBalancer(ExecutorService executor, Class<Subscriber<T>> clazz, int nThreads, int bufferSize) throws Exception {
		this.bufferSize = bufferSize;
		this.executor = executor;
		publishers = new SubmissionPublisher[nThreads];
		this.clazz = clazz;
		createPublisher(executor, nThreads);
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		Arrays.stream(publishers).forEach(p -> {
			try {
				p.subscribe(clazz.getConstructor().newInstance());
			} catch (Exception e) {
				throw new RuntimeException(String.format("Exception: %s, message: %s", e.getClass().getName(), e.getMessage()));
			}
		});
		subscription.request(1);
	}

	private void createPublisher(ExecutorService executor, int nThreads) {
		for (int i = 0; i < publishers.length; i++) {
			publishers[i] = new SubmissionPublisher<T>(executor, bufferSize);
		}
	}

	@Override
	public void onNext(T item) {
		publishers[index++].offer(item, (s, n) -> {
			countReject++;
			s.onError(new IllegalStateException(String.format("rejected: %s", n)));
			return true;
		});
		if (index == publishers.length) {
			index = 0;
		}
		subscription.request(1);
	}

	@Override
	public void onError(Throwable throwable) {
		System.out.printf("Error: %s, message: %s\n", throwable.getClass().getSimpleName(), throwable.getMessage());
	}

	@Override
	public void onComplete() {
		for (SubmissionPublisher<T> submissionPublisher : publishers) {
			submissionPublisher.close();
		}
		executor.shutdown();
	}

}
