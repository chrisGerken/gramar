package com.gerken.xaa.model.xform;

		// Begin imports
import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Node;
		// End imports

public class JavaPkg extends ModelElement {
	
	private String		name;
	private String		purpose;

		// Begin custom variables

		// End custom variables

	public JavaPkg() {
		super();
	}

	public JavaPkg(Node node) {
		this.name = getAttr(node,"name");
		this.purpose = getAttr(node,"purpose");

		// Begin read custom attributes

		// End read custom attributes

		Node kid[] = getChildren(node);
		for (int k = 0; k < kid.length; k++) {
			String name = kid[k].getNodeName();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<javaPkg");
		writeAttr(sb,"name",name);
		writeAttr(sb,"purpose",purpose);

		// Begin write custom attributes

		// End write custom attributes

		sb.append(" >\r\n");

		sb.append("\t\t</javaPkg>\r\n");
	}

	public void removeChild(ModelElement child) {
	}

		// Begin custom methods

		// End custom methods

}
