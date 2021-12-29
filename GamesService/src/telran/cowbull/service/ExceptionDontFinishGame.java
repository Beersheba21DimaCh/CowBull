package telran.cowbull.service;

public class ExceptionDontFinishGame extends Exception {

	private static final long serialVersionUID = 1L;

	public ExceptionDontFinishGame(String string) {
		super(string);
	}

}
