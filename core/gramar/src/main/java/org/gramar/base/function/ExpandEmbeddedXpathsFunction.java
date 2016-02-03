package org.gramar.base.function;

import java.util.List;
import java.util.StringTokenizer;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ExpandEmbeddedXpathsFunction implements XPathFunction {

	public ExpandEmbeddedXpathsFunction() {

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
				throw new XPathFunctionException("argument for expandEmbeddedXpaths() is null");
			}
			original = item.getNodeValue();
		}
		
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(original,"{}",true);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals("{")) {
				sb.append("<c:get select=\"");
			} else if (token.equals("}")) {
				sb.append("\" />");
			} else {
				sb.append(token);
			}
		}
		
		return sb.toString();

	}

}
