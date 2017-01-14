package com.gerken.xaa.model.xform;

		// Begin imports
import java.util.ArrayList;

import org.w3c.dom.Node;
		// End imports

public class Token extends ModelElement {
	
	private String		name;
	private String		id;
	private String		formula;
	private boolean		derived;
	private String		desc;

		// Begin custom variables

		// End custom variables

	public Token() {
		super();
	}

	public Token(Node node) {
		this.name = getAttr(node,"name");
		this.id = getAttr(node,"id");
		this.formula = getAttr(node,"formula");
		derived = getBooleanAttr(node,"derived");
		this.desc = getAttr(node,"desc");

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
		writeAttr(sb,"id",id);
		writeAttr(sb,"formula",formula);
		writeAttr(sb,"derived",derived);
		writeAttr(sb,"desc",desc);

		// Begin write custom attributes

		// End write custom attributes

		sb.append(" >\r\n");

		sb.append("\t\t</token>\r\n");
	}

	public void removeChild(ModelElement child) {
	}

		// Begin custom methods

		// End custom methods

}
