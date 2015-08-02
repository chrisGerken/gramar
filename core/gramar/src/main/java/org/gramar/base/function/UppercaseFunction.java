package org.gramar.base.function;

import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UppercaseFunction implements XPathFunction {

	public UppercaseFunction() {

	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		String original = null;
		Object val = args.get(0);
		if (val instanceof String) {
			original = (String) val;
		} else if (val instanceof NodeList) {
			NodeList nl = (NodeList) val;
			Node item = nl.item(0);
			if (item == null) {
				throw new XPathFunctionException("argument for upper-case() is null");
			}
			original = item.getNodeValue();
		}
		return original.toUpperCase();
	}

}
