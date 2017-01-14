package com.gerken.xaa.mpe.core;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ModelFormatter {

	private static ModelFormatter instance = null;
	
	private String indentString = "\t";
	
	private ModelFormatter() {
		
	}

	public static ModelFormatter getInstance() {
		if (instance == null) {
			instance = new ModelFormatter();
		}
		return instance;
	}
	
	public String format(Document doc) {
		doc.normalizeDocument();
		StringBuffer sb = new StringBuffer();
		String indent = "\r\n";
		NodeList nlist = doc.getChildNodes();
		for (int i = 0; i < nlist.getLength(); i++) {
			Node kid = nlist.item(i);
			if (kid.getNodeType() == Node.ELEMENT_NODE) {
				write((Element)kid,sb,indent);
			}
		}
		return sb.toString();
	}
	
	public void write(Element element, StringBuffer sb, String indent) {
		if (!hasKids(element) & !hasContent(element)) {
			sb.append(indent).append("<"+element.getNodeName());
			NamedNodeMap map = element.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node n = map.item(i);
				sb.append(" "+n.getNodeName()+"=\""+deEntify(n.getNodeValue())+"\"");
			}
			sb.append("/>");
		} else {
			String nodeName = element.getNodeName();
			sb.append(indent).append("<"+nodeName);
			NamedNodeMap map = element.getAttributes();
			for (int i = 0; i < map.getLength(); i++) {
				Node n = map.item(i);
				String attrName = n.getNodeName();
				String attrValue = n.getNodeValue();
				sb.append(" "+attrName+"=\""+deEntify(attrValue)+"\"");
			}
			sb.append(">");
			NodeList nlist = element.getChildNodes();
			for (int i = 0; i < nlist.getLength(); i++) {
				Node kid = nlist.item(i);
				int nodeType = kid.getNodeType();
				if (nodeType == Node.ELEMENT_NODE) {
					write((Element)kid,sb,indent+indentString);
				} else {
					note(kid);
				}
			}		
			if (hasContent(element)) {
				sb.append(getContent(element));
				sb.append("</"+element.getNodeName()+">");
			} else {
				sb.append(indent).append("</"+element.getNodeName()+">");
			}
		}
	}

	private void note(Node kid) {
		
	}

	private String deEntify(String buffer) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buffer.length(); i++) {
			char c = buffer.charAt(i);
			if (c == '&') { sb.append("&amp;"); } 
			else if (c == '<') { sb.append("&lt;"); }
			else if (c == '>') { sb.append("&gt;"); }
			else if (c == '\'') { sb.append("&apos;"); }
			else if (c == '"') { sb.append("&quot;"); }
//			else if (c == '%') { sb.append("&pct;"); }
			else {sb.append(c); }
		}
		return sb.toString();
	}

	private String getContent(Element element) {
		NodeList nlist = element.getChildNodes();
		for (int i = 0; i < nlist.getLength(); i++) {
			Node kid = nlist.item(i);
			if (kid.getNodeType() == Node.TEXT_NODE) {
				return ((Text)kid).getNodeValue();
			}
		}
		return "";
	}

	private boolean hasContent(Element element) {
		NodeList nlist = element.getChildNodes();
		for (int i = 0; i < nlist.getLength(); i++) {
			Node kid = nlist.item(i);
			if (kid.getNodeType() == Node.TEXT_NODE) {
				return true;
			}
		}
		return false;
	}

	private boolean hasKids(Element element) {
		NodeList nlist = element.getChildNodes();
		for (int i = 0; i < nlist.getLength(); i++) {
			Node kid = nlist.item(i);
			if (kid.getNodeType() == Node.ELEMENT_NODE) {
				return true;
			}
		}
		return false;
	}
}
