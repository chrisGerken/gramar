package org.gramar.function;

import javax.xml.xpath.XPathFunction;

import org.gramar.IGramarContext;
import org.w3c.dom.Node;
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
	
	public Double asNumberArg(Object val) {
		Double value = null;
		if (val instanceof Double) {
			value = (Double) val;
		} else if (val instanceof String) {
			value = Double.parseDouble((String)val);
		} else if (val instanceof NodeList) {
			NodeList nl = (NodeList) val;
			value = Double.valueOf(nl.item(0).getNodeValue());
		}
		return value;
	}
	
	public Node[] asNodesArg(Object val) {
		if (val instanceof NodeList) {
			NodeList nl = (NodeList) val;
			Node result[] = new Node[nl.getLength()];
			for (int i = 0; i < result.length; i++) {
				result[i] = nl.item(i);
			}
			return result;
		} 
		return new Node[0];
	}

}
