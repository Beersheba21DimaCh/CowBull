package telran.cowbull.service;

import java.time.LocalDateTime;
import telran.cowbull.dto.*;

public class GameCowsBulls extends Game {

	private static final long serialVersionUID = 1L;

	public GameCowsBulls(long gameId, User user) {
		super(gameId, user);
	}

	@Override
	public boolean step(String inputNumber) {
		step++;
		if (inputNumber.equals(number)) {
			setGameEndDate(LocalDateTime.now());
			moveHistory.add(new CowsBulls(number, 0, 4));
			return true;
		}
		moveHistory.add(calculateCowsBulls(inputNumber));
		return false;
	}

	private CowsBulls calculateCowsBulls(String inputNumber) {

		String inputNumberFormat = String.format("%4s", inputNumber);
		int cows = 0;
		int bulls = 0;
		char[] arInputNumber = inputNumberFormat.toCharArray();
		char[] arNumber = number.toCharArray();

		for (int i = 0; i < arNumber.length; i++) {
			for (int j = 0; j < arNumber.length; j++) {
				if (arInputNumber[i] == arNumber[j]) {
					if (i == j) {
						bulls++;
					} else
						cows++;
				}
			}
		}
		return new CowsBulls(inputNumber, cows, bulls);
	}



}
