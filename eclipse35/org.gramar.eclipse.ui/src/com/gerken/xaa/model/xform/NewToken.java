package com.gerken.xaa.model.xform;

		// Begin imports
import java.util.ArrayList;

import org.w3c.dom.Node;
		// End imports

public class NewToken extends ModelElement {
	
	private String		name;
	private String		formula;
	private String		group;
	private String		replaces;
	private boolean		derived;

		// Begin custom variables

		// End custom variables

	public NewToken() {
		super();
	}

	public NewToken(Node node) {
		this.name = getAttr(node,"name");
		this.formula = getAttr(node,"formula");
		this.group = getAttr(node,"group");
		this.replaces = getAttr(node,"replaces");
		derived = getBooleanAttr(node,"derived");

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

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getReplaces() {
		return replaces;
	}

	public void setReplaces(String replaces) {
		this.replaces = replaces;
	}

	public boolean getDerived() {
		return derived;
	}

	public void setDerived(boolean derived) {
		this.derived = derived;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<newToken");
		writeAttr(sb,"name",name);
		writeAttr(sb,"formula",formula);
		writeAttr(sb,"group",group);
		writeAttr(sb,"replaces",replaces);
		writeAttr(sb,"derived",derived);

		// Begin write custom attributes

		// End write custom attributes

		sb.append(" >\r\n");

		sb.append("\t\t</newToken>\r\n");
	}

	public void removeChild(ModelElement child) {
	}

		// Begin custom methods


	public void process() {
		
		Token token = new Token();
		token.setName(getName());
		token.setDerived(getDerived());
		token.setDesc("Token "+getName());
		String form = "";
		if (getDerived()) {
			form = token.getFormula();
		}
		token.setFormula(form);
		getXform().groupNamed(getGroup()).addToken(token);
		
		if (getDerived() & (getReplaces().trim().length() > 0)){
			Replacement replacement = new Replacement();
			replacement.setOldString(getReplaces());
//			replacement.setNewString(getXform().toJetTags("{$"+getGroup()+"/@"+getName()+"}"));
			replacement.setNewString("{$"+getGroup()+"/@"+getName()+"}");
			getXform().addReplacement(replacement);
		}
	}
	
		// End custom methods

}
