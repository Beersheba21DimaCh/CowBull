package telran.cowbull.service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import telran.cowbull.dto.*;

public abstract class Game implements Serializable, Comparable<Game> {

	public Game(long gameId, User user) {
		this.gameId = gameId;
		this.user = user;
		number = rndNumber();
	}

	private static final long serialVersionUID = 1L;

	public long gameId;
	public String number;
	public boolean isCompetition = false;
	public User user;
	public int step;
	public LocalDateTime gameEndDate;
	public LinkedList<CowsBulls> moveHistory = new LinkedList<>();

	public long getGameId() {
		return gameId;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getNumber() {
		return number;
	}

	public User getUser() {
		return user;
	}

	public LinkedList<CowsBulls> getMoveHistory() {
		return moveHistory;
	}

	public void setGameEndDate(LocalDateTime gameEndDate) {
		this.gameEndDate = gameEndDate;
	}

	public LocalDateTime getGameEndDate() {
		return gameEndDate;
	}


	private String rndNumber() {
		return new Random()
				.ints(1, 10)
				.distinct()
				.limit(4)
				.boxed()
				.map(p -> String.valueOf(p))
				.collect(Collectors.joining());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(String.format("GameId=%d user=%d number=%s\n", gameId, user.userId, number));
		moveHistory.stream().forEach(p -> {
			builder.append(p);
			builder.append("n=" + number + "\n");
		});
		return builder.toString();
	}

	public abstract boolean step(String number);
	
	@Override
	public int compareTo(Game game) {
		return this.step - game.step;
	}

}
