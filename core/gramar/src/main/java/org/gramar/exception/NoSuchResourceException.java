package org.gramar.exception;

public class NoSuchResourceException extends GramarException {

	private static final long serialVersionUID = 1L;

	// Force users to provide the missing resource ID
	
//	public NoSuchResourceException() {
//	}

	public NoSuchResourceException(String resourceId) {
		super("Missing resource: "+resourceId);
	}

	// Force users to provide the missing resource ID

//	public NoSuchResourceException(Throwable cause) {
//		super(cause);
//	}

	public NoSuchResourceException(String resourceId, Throwable cause) {
		super("Missing resource: "+resourceId+" due to "+cause.toString(), cause);
	}

	public NoSuchResourceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
