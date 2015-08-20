package org.gramar.base.function;

import java.util.List;
import java.util.StringTokenizer;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SingleTokenFunction implements XPathFunction {

	public SingleTokenFunction() {

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
				throw new XPathFunctionException("argument for camelCase() is null");
			}
			original = item.getNodeValue();
		}
		
		StringTokenizer st = new StringTokenizer(original);
		String result = "";
		boolean first = true;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			
			boolean allUpper = true;
			for (char c: token.toCharArray()) {
				if (!Character.isUpperCase(c)) {
					allUpper = false;
				}
			}
			
			if (allUpper) {
				if (first) {
					token = token.toLowerCase();
				} else {
					// token stays all uppercase
				}
			} else {
				token = token.substring(0, 1).toUpperCase() + token.substring(1);
			}
			
			result = result + token;

			first = false;
		}

		return result;
	}

}
