package org.gramar.base.function;

import java.util.List;
import java.util.StringTokenizer;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class EscapeJsonFunction implements XPathFunction {

	public EscapeJsonFunction() {

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
				throw new XPathFunctionException("argument for escapeJson() is null");
			} else if ((item.hasChildNodes() && (item.getChildNodes().item(0).getNodeType()==Node.TEXT_NODE))) {
				Text child = (Text) item.getChildNodes().item(0);
				original = child.getNodeValue();
			} else {
				original = item.getNodeValue();
			}
		}
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < original.length(); i++) {
			char c = original.charAt(i);
			if (c == '"') {
				sb.append("\\\"");
			} else if (c == '\\') {
				sb.append("\\\\");
			} else if (c == '/') {
				sb.append("\\/");
			} else if (c == '\f') {
				sb.append("\\f");
			} else if (c == '\n') {
				sb.append("\\n");
			} else if (c == '\r') {
				sb.append("\\r");
			} else if (c == '\t') {
				sb.append("\\t");
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
		
	}

}
