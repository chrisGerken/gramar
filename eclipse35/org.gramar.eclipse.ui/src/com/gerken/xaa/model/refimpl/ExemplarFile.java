package com.gerken.xaa.model.refimpl;

import org.w3c.dom.Node;

public class ExemplarFile extends ModelElement {
	
	private ExemplarProject		exemplarProject;
	private String				path;
	private String				entry;
	private byte[]				content;

	public ExemplarFile(ExemplarProject exemplarProject, String path, String entry, byte[] content) {
		super();
		this.exemplarProject = exemplarProject;
		this.path = path;
		this.entry = entry;
		this.content = content;
	}

	public ExemplarFile(Node node) {
		this.path = getAttr(node,"path");
		this.entry = getAttr(node,"oEntry");
	}

	public ExemplarProject getExemplarProject() {
		return exemplarProject;
	}

	public void setExemplarProject(ExemplarProject exemplarProject) {
		this.exemplarProject = exemplarProject;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<file");
		writeAttr(sb,"path",path);
		writeAttr(sb,"oEntry",entry);
		writeAttr(sb,"refimpl","refimpl/"+entry);
		writeAttr(sb,"entry",getFile().replace('.','_'));
		writeAttr(sb,"folder",getFolder());
		writeAttr(sb,"file",getFile());
		sb.append(" />\r\n");
	}

	private String getFile() {
		if (path == null) { return null; }
		String buf = path.replace('\\','/');
		int offset = buf.lastIndexOf('/');
		if (offset < 0) { return path; }
		return path.substring(offset+1);
	}

	private String getFolder() {
		if (path == null) { return null; }
		String buf = path.replace('\\','/');
		int offset = buf.lastIndexOf('/');
		if (offset < 0) { return ""; }
		String rel = path.substring(0,offset);
		if (!buf.startsWith("/")) {
			rel = "/" + rel;
		}
		return rel;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}