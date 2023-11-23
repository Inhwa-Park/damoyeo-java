package com.damoyeo.healthyLife.exception;

public class NotUserException extends Exception{	
	private static final long serialVersionUID = 1L;
	
	public NotUserException() {
        super("NotUserException");
    }
    public NotUserException(String msg) {
        super(msg);
    }
}
