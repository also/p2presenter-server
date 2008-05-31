/* $Id$ */

package edu.uoregon.cs.presenter.dao;

public class InvalidAccessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidAccessException() {
		super();
	}

	public InvalidAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidAccessException(String message) {
		super(message);
	}

	public InvalidAccessException(Throwable cause) {
		super(cause);
	}
	
}
