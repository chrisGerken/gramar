package com.gerken.xaa.model.xform;

		// Begin imports
import java.util.ArrayList;

import org.w3c.dom.Node;
		// End imports

public class Replacement extends ModelElement {
	
	private String		oldString;
	private String		newString;

		// Begin custom variables

		// End custom variables

	public Replacement() {
		super();
	}

	public Replacement(Node node) {
		this.oldString = getAttr(node,"oldString");
		this.newString = getAttr(node,"newString");

		// Begin read custom attributes

		// End read custom attributes

		Node kid[] = getChildren(node);
		for (int k = 0; k < kid.length; k++) {
			String name = kid[k].getNodeName();
		}
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

	public void writeTo(StringBuffer sb) {
		sb.append("\t<replacement");
		writeAttr(sb,"oldString",oldString);
		writeAttr(sb,"newString",newString);

		// Begin write custom attributes

		// End write custom attributes

		sb.append(" >\r\n");

		sb.append("\t\t</replacement>\r\n");
	}

	public void removeChild(ModelElement child) {
	}

		// Begin custom methods

		// End custom methods

}
