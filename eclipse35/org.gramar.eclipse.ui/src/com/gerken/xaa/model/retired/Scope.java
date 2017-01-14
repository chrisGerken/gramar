package com.gerken.xaa.model.retired;

import org.w3c.dom.Node;

public class Scope extends ModelElement {

	private String 		name;
	private String		superScope;
	
	public Scope() {
	}

	public Scope(Node node) {
		this.name = getAttr(node,"name");
		this.superScope = getAttr(node,"superScope");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<scope");
		writeAttr(sb,"name",name);
		writeAttr(sb,"superScope",superScope);
		sb.append(" />\r\n");
	}

	public String getSuperScope() {
		return superScope;
	}

	public void setSuperScope(String superScope) {
		this.superScope = superScope;
	}

}
