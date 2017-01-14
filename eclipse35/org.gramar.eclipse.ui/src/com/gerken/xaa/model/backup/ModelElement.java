package com.gerken.xaa.model.backup;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class ModelElement {

	private ModelElement parent;
	
	public ModelElement() {
	}
	
	public String xmlRepresentation() {
		StringBuffer sb = new StringBuffer();
		writeTo(sb);		
		return sb.toString();
	}
	
	public void persistTo(String projectName) {
		IProject project = projectNamed(projectName);
		IFile file = (IFile) project.findMember(".xform");
		if (file == null) {
			file = project.getFile(".xform");
		}
		persistTo(file);
	}
	
	private static IProject projectNamed(String projectName) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	}

	public void persistTo(IFile file) {
		String content = xmlRepresentation();
		ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
		try {
			file.setContents(bais, true, true, new NullProgressMonitor());
		} catch (CoreException e) {}
	}

//	public Document buildModel(String content) throws Exception {
//		return readData(new ByteArrayInputStream(content.getBytes()));
//	}

	protected Node[] getChildren(Node node) {
		NodeList list = node.getChildNodes();
		Node kid[] = new Node[list.getLength()];
		for (int i = 0; i < kid.length; i++) {
			kid[i] = list.item(i);
		}
		return kid;
	}

	protected String getAttr(Node node, String name) {
		String value = "";
		try {
			value = node.getAttributes().getNamedItem(name).getNodeValue();
			if (value == null) {
				value = "";
			}
		} catch (Throwable t) {}
		return value;
	}

	protected String getCdata(Node node, String name) {
		String value = "";
		try {
			NodeList list = node.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				if (name.equals(list.item(i).getNodeName())) {
					value =  list.item(i).getTextContent();
				}
			}
		} catch (Throwable t) {}
		return value;
	}
	
	protected int getIntAttr(Node node, String name) {
		return Integer.parseInt(getAttr(node,name));
	}
	
	protected boolean getBooleanAttr(Node node, String name) {
		return Boolean.parseBoolean(getAttr(node,name));
	}

	public abstract void writeTo(StringBuffer sb);

	protected void writeAttr(StringBuffer sb, String name, String value) {
		sb.append(" ");
		sb.append(name);
		sb.append("=\"");
		sb.append(deEntify(value));
		sb.append("\"");
	}

	protected void writeAttr(StringBuffer sb, String name, int value) {
		writeAttr(sb, name, String.valueOf(value));
	}

	protected void writeAttr(StringBuffer sb, String name, boolean value) {
		writeAttr(sb, name, String.valueOf(value));
	}

	protected void writeCdata(StringBuffer sb, String name, String value) {
		sb.append("\t\t<"+name+">");
//		sb.append("<![CDATA["+value+"]]>");
		sb.append(deEntify(value));
		sb.append("</"+name+">");
	}

	private String deEntify(String buffer) {
		if (buffer == null) { return ""; }
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buffer.length(); i++) {
			char c = buffer.charAt(i);
			if (c == '&') { sb.append("&amp;"); } 
			else if (c == '<') { sb.append("&lt;"); }
			else if (c == '>') { sb.append("&gt;"); }
			else if (c == '\'') { sb.append("&apos;"); }
			else if (c == '"') { sb.append("&quot;"); }
			else {sb.append(c); }
		}
		return sb.toString();
	}

	public void setParent(ModelElement parent) {
		this.parent = parent;
	}

	public ModelElement getParent() {
		return parent;
	}
	
	public void remove() {
		if (parent != null) {
			parent.removeChild(this);
		}
	}
	
	public abstract void removeChild(ModelElement child);
	
	public Xform getXform() {
		if (getParent() != null) {
			return getParent().getXform();
		}
		return null;
	}

}