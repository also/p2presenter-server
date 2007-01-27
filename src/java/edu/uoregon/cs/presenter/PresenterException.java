package edu.uoregon.cs.presenter;

public class PresenterException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PresenterException() {}

	public PresenterException(String arg0) { super(arg0); }

	public PresenterException(Throwable arg0) { super(arg0); }

	public PresenterException(String arg0, Throwable arg1) { super(arg0, arg1); }

}
