package com.cg.mypaymentapp.Exception;

public class InsufficientBalanceException extends RuntimeException{
	private static final long serialVersionUID = 9093426535721786566L;

	public InsufficientBalanceException(String msg) {
		super(msg);
	}

	public InsufficientBalanceException() {
		super();
		
	}

	public InsufficientBalanceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		
	}

	public InsufficientBalanceException(String message, Throwable cause) {
		super(message, cause);
		
	}

	public InsufficientBalanceException(Throwable cause) {
		super(cause);
		
	}
	
}
