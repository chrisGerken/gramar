package org.gramar.base.function;

import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.NodeList;

public class UppercaseFirstFunction implements XPathFunction {

	public UppercaseFirstFunction() {

	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		String original = null;
		Object val = args.get(0);
		if (val instanceof String) {
			original = (String) val;
		} else if (val instanceof NodeList) {
			NodeList nl = (NodeList) val;
			original = nl.item(0).getNodeValue();
		}
		return original.substring(0, 1).toUpperCase() + original.substring(1);
	}

}
