package com.gerken.xaa.mpe.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.eclipse.text.edits.ReplaceEdit;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ModelAccess {

	
	public static Node[] getNodes(Node sourceNode, String xpathNodesExpresion) {
		if (sourceNode == null) { return new Node[0]; }
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			NodeList nodes = (NodeList) xpath.evaluate(xpathNodesExpresion,sourceNode,XPathConstants.NODESET);
			Vector<Node> v = new Vector<Node>();
			for(int itr = 0; itr < nodes.getLength(); itr++) {
				Node node = nodes.item(itr);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					v.addElement(node);
				}
			}
			Node result[] = new Node[v.size()];
			v.copyInto(result);
			return result;
		} catch (Exception e) {}
		return new Node[0];
	}
	
	public static String getAttribute(Node sourceNode, String xpathAttributeExpression) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			NodeList nodes = (NodeList) xpath.evaluate(xpathAttributeExpression,sourceNode,XPathConstants.NODESET);
			for(int itr = 0; itr < nodes.getLength(); itr++) {
				Node node = nodes.item(itr);
				if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
					return String.valueOf(node.getNodeValue());
				}
			}
		} catch (Exception e) {}
		return "";
	}
	
	public static String getText(Node sourceNode, String xpathNodeExpression) {
		try {
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
			NodeList nodes = (NodeList) xpath.evaluate(xpathNodeExpression,sourceNode,XPathConstants.NODESET);
			for(int itr = 0; itr < nodes.getLength(); itr++) {
				Node node = nodes.item(itr);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					return node.getTextContent();
				}
			}
		} catch (Exception e) {}
		return "";
	}
	
	public static String bind(Node target,String pattern) {
		String buffer = pattern;
		int i = buffer.indexOf("{");
		while (i > -1) {
			int j = buffer.indexOf("}",i);
			
			if (j > -1) {
				String before = buffer.substring(0,i);
				String expr   = buffer.substring(i+1,j);
				String after  = buffer.substring(j+1);
				buffer = before + getAttribute(target,expr) + after;
				i = buffer.indexOf("{");
			} else {
				i = -1;
			}
		}
		return buffer;
	}

	public static void setAttribute(Node node, String name, String value) {
		if (node == null) { return; }
		((Element) node).setAttribute(name, value);  
	}

	public static void setText(Node node, String name, String value) {
		if (node == null) { return; }
		Element el = (Element) node;
		Node kid[] = ModelAccess.getNodes(node,name);
		for (int k = 0; k < kid.length; k++) {
			el.removeChild(kid[k]);
		}
		el = (Element) el.appendChild(node.getOwnerDocument().createElement(name));
		el.appendChild(node.getOwnerDocument().createTextNode(value));
	}

	public static String[] markupProposals(Node xform, String original, String current) {
		Node[] reps = getNodes(xform, "xform/replacement");
		Hashtable<String, String> ht = new Hashtable<String, String>();
		for (int n = 0; n < reps.length; n++) {
			String r = getAttribute(reps[n],"@oldString");
			String w = getAttribute(reps[n],"@newString");
			if (r.length() > 0) {
				ht.put(r,w);
			}
		}
		String replace[] = new String[ht.size()];
		String with[]    = new String[ht.size()];
		
		int i = 0;
		for (Enumeration<String> mune = ht.keys(); mune.hasMoreElements(); ) {
			replace[i] = mune.nextElement();
			i++;
		}
		Arrays.sort(replace,new KeySorter());
		for (i = 0; i < replace.length; i++) {
			with[i] = ht.get(replace[i]);
		}
		ArrayList<String> proposals = new ArrayList<String>();
		if ((current != null) && (current.trim().length() > 0)) { proposals.add(current); }
		if ((original != null) && (original.trim().length() > 0) && (!original.equals(current))) { proposals.add(original); }
		calculateProposals(current, proposals, 0, replace, with);
		
		String result[] = new String[proposals.size()];
		proposals.toArray(result);
		return result;
	}
	
	private static ArrayList<String> calculateProposals(String partially, ArrayList<String> proposals, int unused, String[] replace, String[] with) {

		for (int r = unused; r < replace.length; r++) {
			int offset = partially.indexOf(replace[r]);
			while (offset > -1) {
				String before = partially.substring(0,offset);
				String after = partially.substring(offset+replace[r].length());
				
				ArrayList<String> befores = new ArrayList<String>();
				calculateProposals(before, befores, r+1,replace,with);
				befores.add(before);
				
				ArrayList<String> afters = new ArrayList<String>();
				calculateProposals(after, afters, r, replace, with);
				afters.add(after);
				
				for (int b = 0; b < befores.size(); b++) {
					for (int a = 0; a < afters.size(); a++) {
						proposals.add(befores.get(a)+with[r]+afters.get(a));
					}
				}
				
				offset = offset + replace[r].length();
				if (offset > partially.length()) {
					offset = -1;
				} else {
					offset = partially.indexOf(replace[r],offset);
				}
			}
		}
		return proposals;
	}

	public static Node addNewChild(Node parent, String childName) {
		Element kid = parent.getOwnerDocument().createElement(childName);
		parent.appendChild(kid);
		return kid;
	}

	public static Element createToken(Document ownerDocument, String tokenName,
			String groupId, boolean derived, String formula) {

		
		Element token = ownerDocument.createElement("token");
		String xpath = "/xform/group[@id=\""+groupId+"\"]";
		Node group = getNodes(ownerDocument,xpath)[0];
		group.appendChild(token);
		token.setAttribute("name",tokenName);
		token.setAttribute("derived",String.valueOf(derived));
		token.setAttribute("formula",formula);
		return token;
		
	}

	public static Element createReplacement(Document ownerDocument, String oldString, String newString) {

		Element replacement = ownerDocument.createElement("replacement");
		Node xform = getNodes(ownerDocument,"/xform")[0];
		xform.appendChild(replacement);
		replacement.setAttribute("oldString",oldString);
		replacement.setAttribute("newString",newString);
		return replacement;
		
	}
	
}
