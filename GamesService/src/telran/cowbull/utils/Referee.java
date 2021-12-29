package telran.cowbull.utils;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

import telran.cowbull.dto.Competition;
import telran.cowbull.dto.User;
import telran.cowbull.mode.CowBullServerCompetitionMode;
import telran.cowbull.service.*;

public class Referee implements Subscriber<User> {

	Subscription subscription;
	CowBullService service;

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		service = CowBullServerCompetitionMode.service;
		subscription.request(1);
	}

	@Override
	public void onNext(User user) {
		try {
			if (service.getCompetition().isCompetition == false) {
				return;
			}
			Game game = service.setCurrentGame(user.userId);
			game.isCompetition = true;
			System.out.println(game);
			int timeOut = service.getCompetition().timeOutLimit;
			LocalDateTime tStart = LocalDateTime.now();
			LocalDateTime tFinish = tStart.plusMinutes(timeOut);

			while (service.getUser(user.userId).getCurrentGame() != null && LocalDateTime.now().isBefore(tFinish)) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					System.out.println("interrupt");
					break;
				}
			}
			service.cleanCurrentGame(user.userId);
			service.pushPlayerToStackCompetition(user.userId);
		} catch (Exception e) {
			System.out.println("error raferee" + e.getMessage());
		}
		System.out.println("end referee");
		subscription.request(1);
	}

	@Override
	public void onError(Throwable throwable) {
		System.out.println(throwable.getMessage());

	}

	@Override
	public void onComplete() {
		System.out.println("complite");
	}

}
