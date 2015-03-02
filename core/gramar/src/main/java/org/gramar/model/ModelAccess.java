package org.gramar.model;

import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.gramar.IGramarContext;
import org.gramar.context.XPathResolver;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class ModelAccess {

	private static ModelAccess instance = new ModelAccess();
	
	private ModelAccess() {

	}

	public static ModelAccess getDefault() {
		return instance;
	}
	
	public Node[] getRawResult(Node sourceNode, String xpathExpresion, IGramarContext context) throws XPathExpressionException {
		if (sourceNode == null) { return new Node[0]; }
		XPath xpath = getXpath(context);
		NodeList nodes = (NodeList) xpath.evaluate(xpathExpresion,sourceNode,XPathConstants.NODESET);
		ArrayList<Node> v = new ArrayList<Node>();
		for(int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			v.add(node);
		}
		Node node[] = new Node[v.size()];
		v.toArray(node);
		return node;
	}
	
	public Node[] getNodes(Node sourceNode, String xpathExpresion, boolean elementsOnly, IGramarContext context) throws XPathExpressionException {
		if (sourceNode == null) { return new Node[0]; }
		Node node[] = getRawResult(sourceNode, xpathExpresion, context);
		ArrayList<Node> v = new ArrayList<Node>();
		for(int i = 0; i < node.length; i++) {
			if ((node[i].getNodeType() == Node.ELEMENT_NODE) || (!elementsOnly)) {
				v.add(node[i]);
			}
		}
		node = new Node[v.size()];
		v.toArray(node);
		return node;
	}
	
	public Node getNode(Node sourceNode, String xpathExpresion, boolean elementsOnly, IGramarContext context) throws XPathExpressionException {
		if (sourceNode == null) { return null; }
		XPath xpath = getXpath(context);
		Node node = (Node) xpath.evaluate(xpathExpresion,sourceNode,XPathConstants.NODE);
		return node;
	}
	
	public String getText(Node sourceNode, String xpathExpression, IGramarContext context) throws XPathExpressionException {
		XPath xpath = getXpath(context);
		NodeList nodes = (NodeList) xpath.evaluate(xpathExpression,sourceNode,XPathConstants.NODESET);
		for(int itr = 0; itr < nodes.getLength(); itr++) {
			Node node = nodes.item(itr);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				return text(node);
			}
		}
		return null;
	}
	
	public String getText(Node sourceNode, String xpathExpression) throws XPathExpressionException {
		return getText(sourceNode, xpathExpression, null);
	}
	
	private String text(Node node) {
		StringBuffer sb = new StringBuffer(1000);
		text(node,sb,false);
		return sb.toString();
	}

	private void text(Node node, StringBuffer sb, boolean includeComments) {
		if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
			sb.append(node.getNodeValue());
		} else if ((includeComments) && (node.getNodeType() == Node.COMMENT_NODE)) {
			sb.append(node.getNodeValue());
		} else if (node.getNodeType() == Node.TEXT_NODE) {
			sb.append(node.getNodeValue());
		} else if (node.getNodeType() == Node.ELEMENT_NODE) {
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				text(list.item(i), sb, includeComments);
			}
		}
	}

//	public String getText(Node sourceNode, String xpathExpression) throws XPathExpressionException {
//		return getText(sourceNode, xpathExpression);
//	}
	
	public String getAttribute(Node sourceNode, String xpathExpression, IGramarContext context) throws XPathExpressionException {
		XPath xpath = getXpath(context);
		NodeList nodes = (NodeList) xpath.evaluate(xpathExpression,sourceNode,XPathConstants.NODESET);
		for(int itr = 0; itr < nodes.getLength(); itr++) {
			Node node = nodes.item(itr);
			if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
				return String.valueOf(node.getNodeValue());
			}
		}
		return null;
	}
	
	public String getAttribute(Node sourceNode, String xpathExpression) throws XPathExpressionException {
		return getAttribute(sourceNode, xpathExpression, null);
	}
	
	public Object resolve(Node sourceNode, String xpathExpression, IGramarContext context, QName type) throws XPathExpressionException {
		XPath xpath = getXpath(context);
		return xpath.evaluate(xpathExpression, sourceNode, type);
	}

	public void setAttribute(Node sourceNode, String attributeName, String value) {
		if (sourceNode == null) { return; }
		try { ((Element) sourceNode).setAttribute(attributeName, value); }
		catch (Throwable t) {
			System.out.println(t);
		}
	}
	
	public Node newNode(Node sourceNode, String childName) {
		Element newNode = sourceNode.getOwnerDocument().createElement(childName);
		sourceNode.appendChild(newNode);
		return newNode;
	}
	
	private XPath getXpath(IGramarContext context) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		if (context != null) {
			xpath.setXPathFunctionResolver(new XPathResolver(context));
			xpath.setXPathVariableResolver(new XPathResolver(context));
		}
		return xpath;
	}

	
}
