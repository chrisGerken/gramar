package org.gramar.platform;

import org.gramar.IGramarStatus;

public class GramarStatus implements IGramarStatus {

	private Throwable cause;
	private String message;
	private int severity;
	
	public GramarStatus(Throwable cause, String message, int severity) {
		this.cause = cause;
		this.message = message;
		this.severity = severity;
	}
	
	public static IGramarStatus error(Throwable t) {
		return new GramarStatus(t, t.getMessage(), SEVERITY_ERROR );
	}
	
	public static IGramarStatus error(String message) {
		return new GramarStatus(null, message, SEVERITY_ERROR );
	}
	
	public static IGramarStatus severe(String message) {
		return new GramarStatus(null, message, SEVERITY_SEVERE );
	}
	
	public static IGramarStatus warning(Throwable t) {
		return new GramarStatus(t, t.getMessage(), SEVERITY_WARN );
	}

	public static IGramarStatus warning(String message) {
		return new GramarStatus(null, message, SEVERITY_WARN );
	}
	
	public static IGramarStatus info(String message) {
		return new GramarStatus(null, message, SEVERITY_INFO );
	}

	public static IGramarStatus debug(String message) {
		return new GramarStatus(null, message, SEVERITY_DEBUG );
	}

	@Override
	public int getSeverity() {
		return severity;
	}

	@Override
	public Throwable getCause() {
		return cause;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
	public String toString() {
		return statusFor(severity)+" : "+message;
	}
	
	private String statusFor(int severity) {

		if (severity == SEVERITY_DEBUG) 	{ return "DEBUG"; }
		if (severity == SEVERITY_INFO) 		{ return "INFO"; }
		if (severity == SEVERITY_WARN) 		{ return "WARN"; }
		if (severity == SEVERITY_ERROR) 	{ return "ERROR"; }
		if (severity == SEVERITY_SEVERE) 	{ return "SEVERE"; }
		return "";
		
	}

}
