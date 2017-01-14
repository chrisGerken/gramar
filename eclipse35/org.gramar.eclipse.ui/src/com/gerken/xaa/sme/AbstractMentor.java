package com.gerken.xaa.sme;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gerken.xaa.model.xform.CreateFile;


public abstract class AbstractMentor implements IXaaMentor {

	private boolean enabled = false;
	
	public AbstractMentor() {
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Node retrieveNode(Node snode, String expr) {
		Node[] result = retrieveNodes(snode, expr);
		if (result.length == 0) {
			return null;
		}
		return result[0];
	}
	
	public Node[] retrieveNodes(Node snode, String expr) {
		if (snode == null) { return new Node[0]; }
		ArrayList<Node> nal = new ArrayList<Node>();
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			NodeList nodes = (NodeList) xpath.evaluate(expr,snode,XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					nal.add(node);
				}
			}
		} catch (XPathExpressionException e) {
			e.toString();
		}
		Node result[] = new Node[nal.size()];
		nal.toArray(result);
		return result;
	}
	
	public String retrieveString(Node node, String expr) {
		String result = "";
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			result = (String) xpath.evaluate(expr,node,XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			e.toString();
		}
		return result;
	}

	public String camelizePackage(String name) {
		String buffer = name.toLowerCase();
		if (buffer.startsWith(".")) {
			buffer = buffer.substring(1);
		}
		StringTokenizer st = new StringTokenizer(buffer,"./");
		StringBuffer sb = new StringBuffer();
		boolean first = true;
		while (st.hasMoreTokens()) {
			sb.append(upper(st.nextToken(),first));
			first = false;
		}
		return sb.toString();
	}

	private String upper(String buffer, boolean first) {
		if (first) { return buffer; }
		if (buffer.length() < 2) { return buffer.toUpperCase(); }
		return buffer.substring(0,1).toUpperCase() + buffer.substring(1);
	}

}
