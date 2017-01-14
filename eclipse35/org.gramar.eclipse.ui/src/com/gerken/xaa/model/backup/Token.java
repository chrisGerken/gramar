package com.gerken.xaa.model.backup;

		// Begin imports
import java.util.ArrayList;

import org.w3c.dom.Node;
		// End imports

public class Token extends ModelElement {
	
	private String		name;
	private String		formula;
	private boolean		derived;
	private String		desc;
	

		// Begin custom variables

		// End custom variables

		// Begin custom methods

		// End custom methods

	public Token() {
		super();
	}

	public Token(Node node) {
		this.name = getAttr(node,"name");
		this.formula = getAttr(node,"formula");
		this.derived = getBooleanAttr(node,"derived");
		this.desc = getCdata(node,"desc");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public boolean getDerived() {
		return derived;
	}

	public void setDerived(boolean derived) {
		this.derived = derived;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<token");
		writeAttr(sb,"name",name);
		writeAttr(sb,"formula",formula);
		writeAttr(sb,"derived",derived);
		// Begin custom attributes

		// End custom attributes
		sb.append(" >\r\n");
		writeCdata(sb,"desc",desc);
		sb.append("</token>\r\n");
	}

	public void removeChild(ModelElement child) {
	}

}
