package telran.cowbull.dto;

import java.io.Serializable;

public class CowsBulls implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public int cow;
	public int bull;
	public String number;
	
	public CowsBulls(String number, int cow, int bull) {
		this.cow = cow;
		this.bull = bull;
		this.number = number;
	}

	@Override
	public String toString() {
		return "cow=" + cow + ", bull=" + bull + ", number=" + number;
	}
}
