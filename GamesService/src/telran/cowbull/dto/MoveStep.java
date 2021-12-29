package telran.cowbull.dto;

import java.io.Serializable;

public class MoveStep implements Serializable{

	public MoveStep(long gameId, String number) {
		super();
		this.gameId = gameId;
		this.number = number;
	}

	private static final long serialVersionUID = 1L;
	
	public long gameId; 
	public String number;

}
