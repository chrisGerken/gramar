package org.gramar;

/**
 * Represents an issue (or set of issues) of interest raised during the application of a gramar to a model.
 * An issue has a severity and various related pieces of information.
 * 
 * @author chrisgerken
 *
 */
public interface IGramarStatus {
	
	public static final int SEVERITY_NONE 	= 0;
	public static final int SEVERITY_DEBUG	= 1;
	public static final int SEVERITY_INFO 	= 2;
	public static final int SEVERITY_WARN 	= 3;
	public static final int SEVERITY_ERROR 	= 4;
	public static final int SEVERITY_SEVERE	= 5;

	/**
	 * Returns the severity of the issue, or maximum severity if the
	 * receiver represents multiple issues.
	 */
	public int getSeverity();
	
}
