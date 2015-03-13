package org.gramar.function;

import javax.xml.xpath.XPathFunction;

import org.gramar.IGramarContext;
import org.w3c.dom.NodeList;

public abstract class GramarFunction implements XPathFunction, IGramarFunction {

	protected IGramarContext context;

	public GramarFunction() {
	}

	@Override
	public void setContext(IGramarContext context) {
		this.context = context;
	}
	
	public String asStringArg(Object val) {
		String value = null;
		if (val instanceof String) {
			value = (String) val;
		} else if (val instanceof NodeList) {
			NodeList nl = (NodeList) val;
			value = nl.item(0).getNodeValue();
		}
		return value;
	}

}
