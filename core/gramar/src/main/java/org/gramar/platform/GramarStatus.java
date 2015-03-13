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
	
	public static IGramarStatus warning(Throwable t) {
		return new GramarStatus(t, t.getMessage(), SEVERITY_WARN );
	}
	
	public static IGramarStatus info(String message) {
		return new GramarStatus(null, message, SEVERITY_INFO );
	}

	@Override
	public int getSeverity() {
		return severity;
	}

	public Throwable getCause() {
		return cause;
	}

	public String getMessage() {
		return message;
	}

}
