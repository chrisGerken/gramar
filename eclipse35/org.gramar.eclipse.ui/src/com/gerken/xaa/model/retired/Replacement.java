package com.gerken.xaa.model.retired;

import org.w3c.dom.Node;

public class Replacement extends ModelElement {

	private String 		oldString;
	private String		newString;
	
	public Replacement() {
	}

	public Replacement(Node node) {
		this.newString = getAttr(node,"newString");
		this.oldString = getAttr(node,"oldString");
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<scope");
		writeAttr(sb,"oldString",oldString);
		writeAttr(sb,"newString",newString);
		sb.append(" />\r\n");
	}

	public String getOldString() {
		return oldString;
	}

	public void setOldString(String oldString) {
		this.oldString = oldString;
	}

	public String getNewString() {
		return newString;
	}

	public void setNewString(String newString) {
		this.newString = newString;
	}

}
