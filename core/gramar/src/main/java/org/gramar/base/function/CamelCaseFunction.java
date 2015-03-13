package org.gramar.base.function;

import java.util.List;
import java.util.StringTokenizer;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.NodeList;

public class CamelCaseFunction implements XPathFunction {

	public CamelCaseFunction() {

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
		
		StringTokenizer st = new StringTokenizer(original);
		String result = "";
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			result = result + token.substring(0, 1).toUpperCase() + token.substring(1);
		}

		return result;
	}

}
