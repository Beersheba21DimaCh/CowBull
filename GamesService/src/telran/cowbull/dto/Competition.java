package telran.cowbull.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Competition implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static boolean isCompetition;
	public static boolean registration;
	public static LocalDateTime timeStart;
	public final LocalDateTime timeFinish;
	public LocalDateTime timePeriod;
	public final int period;
	public final int timeOutLimit;
	public Queue<User> players = new LinkedList<>();
	
	public Competition(LocalDateTime time, int period, int timeOutLimit) {
		timeStart = time;
		this.period = period;
		this.timeOutLimit = timeOutLimit;
		this.timeFinish = timeStart.plusMinutes(period);
		this.timePeriod = timeStart.plusMinutes(period);
		isCompetition = true;
		registration = true;
	}
	
	public CowBullCode addPlayer(User user) throws Exception {
		if(players.contains(user)) {
			throw new Exception("A member with the same name is already registered");
		}
		players.add(user);
		return CowBullCode.OK;
	}
	
	

}
