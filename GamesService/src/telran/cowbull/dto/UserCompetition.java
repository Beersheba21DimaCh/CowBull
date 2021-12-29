package telran.cowbull.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserCompetition implements Serializable{

	private static final long serialVersionUID = 1L;
	public final long userId;
	public final LocalDateTime dateTimeBeginningChemp;
	public UserCompetition(long userId, LocalDateTime dateTimeBeginningChemp) {
		super();
		this.userId = userId;
		this.dateTimeBeginningChemp = dateTimeBeginningChemp;
	}
}
