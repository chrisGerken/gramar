package com.gerken.xaa.mpe.constraint;

import org.w3c.dom.Node;

public class ConstraintFailure {

	public static int	SEVERITY_WARNING = 2;
	public static int	SEVERITY_ERROR = 3;
	
	private Node	target;
	private String	field;
	private String	message;
	private int		severity;
	private int     reportingConstraintSet;
	private String  pageName;
	private String  pageID;
	
	private ConstraintFailure(Node target, String field, String message, int severity) {
		this.target = target;
		this.field = field;
		this.message = message;
		this.severity = severity;
	}

	public static ConstraintFailure error(Node target, String field, String message) {
		return new ConstraintFailure(target, field, message, SEVERITY_ERROR);
	}

	public static ConstraintFailure error(Node target, String message) {
		return new ConstraintFailure(target, null, message, SEVERITY_ERROR);
	}

	public static ConstraintFailure warning(Node target, String field, String message) {
		return new ConstraintFailure(target, field, message, SEVERITY_WARNING);
	}

	public static ConstraintFailure warning(Node target, String message) {
		return new ConstraintFailure(target, null, message, SEVERITY_WARNING);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		this.severity = severity;
	}
	
	public boolean isWarning() {
		return getSeverity() == SEVERITY_WARNING;
	}
	
	public boolean isError() {
		return getSeverity() == SEVERITY_ERROR;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public String getNodename() {
		return target.getNodeName();
	}

	public int getReportingConstraintSet() {
		return reportingConstraintSet;
	}

	public void setReportingConstraintSet(int reportingConstraintSet) {
		this.reportingConstraintSet = reportingConstraintSet;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageID() {
		return pageID;
	}

	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

}
