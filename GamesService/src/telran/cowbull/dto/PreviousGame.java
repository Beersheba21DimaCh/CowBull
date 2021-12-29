package telran.cowbull.dto;

import java.io.Serializable;

public class PreviousGame implements Serializable{

	public PreviousGame(String from, String to) {
		this.from = from;
		this.to = to;
	}
	private static final long serialVersionUID = 1L;
	public String from;
	public String to;
	


}
