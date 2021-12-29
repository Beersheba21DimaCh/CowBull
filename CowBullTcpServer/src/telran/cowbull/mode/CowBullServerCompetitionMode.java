package telran.cowbull.mode;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.*;
import java.util.concurrent.Flow.Subscriber;
import telran.cowbull.dto.*;
import telran.cowbull.service.CowBullService;
import telran.cowbull.utils.CowBullUtils;
import telran.cowbull.utils.LoadBalancer;
import telran.cowbull.utils.Referee;

public class CowBullServerCompetitionMode implements Runnable {

	public static CowBullService service;
	private static int portion;
	private ExecutorService executorRunCompetition;

	public CowBullServerCompetitionMode(CowBullService service, int portion) {
		CowBullServerCompetitionMode.service = service;
		CowBullServerCompetitionMode.portion = portion;
	}

	@Override
	public void run() {
		try {
			System.out.printf("Current time = %s, competition start in %s",
					CowBullUtils.parseLocalDateTime(LocalDateTime.now()),
					CowBullUtils.parseLocalDateTime(service.getCompetition().timeStart));
			long delay = Duration.between(LocalDateTime.now(), service.getCompetition().timeStart).toSeconds();
			System.out.println(" start competition through " + delay + " sec");
			ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
			executor.schedule(this::runCompetition, delay, TimeUnit.SECONDS);
			runTimerCompetition(executor);
		} catch (Exception e) {
			System.out.println("Error run " + e.getMessage());
		}

	}

	private void runTimerCompetition(ScheduledExecutorService executor) {
		while (Competition.isCompetition) {
			try {
				if (LocalDateTime.now().isAfter(service.getCompetition().timeFinish)) {
					System.out.println("LocalDateTime.now().isAfter(competition.timeFinish)");
					break;
				}
				Thread.sleep(1);
			} catch (Exception e) {
				System.out.println("Interrupt in timer competition " + e.getMessage());
			}
		}
		
		Competition.isCompetition = false;
		Competition.registration = false;
		executor.shutdown();
		executorRunCompetition.shutdownNow();
		System.out.println("end Competition");
	}

	public void runCompetition() {
		Competition.registration = false;
		System.out.println("run Competition");

		try {
			executorRunCompetition = Executors.newFixedThreadPool(service.getCompetition().players.size());
			SubmissionPublisher<User> publisher = new SubmissionPublisher<>();
			Referee referee = new Referee();
			LoadBalancer<User> balancer = new LoadBalancer<User>(executorRunCompetition,
					(Class<Subscriber<User>>) referee.getClass(), portion,
					service.getCompetition().players.size());
			publisher.subscribe(balancer);
			publishTask(publisher);

		} catch (Exception e) {
			Competition.isCompetition = false;
			System.out.println("No participants, exit from competition mode.");
		}
	}

	private void publishTask(SubmissionPublisher<User> publisher) throws Exception {

		while (service.getCompetition().isCompetition) {
			if (!service.getCompetition().players.isEmpty()) {
				publisher.submit(service.getCompetition().players.poll());
			}
			Thread.sleep(1);
		}

	}

}
