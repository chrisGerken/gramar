package com.gerken.xaa.model.refimpl;

import org.w3c.dom.Node;

public class ExemplarFolder extends ModelElement {
	
	private ExemplarProject		exemplarProject;
	private String				path;

	public ExemplarFolder(ExemplarProject exemplarProject, String path) {
		super();
		this.exemplarProject = exemplarProject;
		this.path = path;
	}

	public ExemplarFolder(Node node) {
		this.path = getAttr(node,"path");
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
		sb.append("\t<folder");
		writeAttr(sb,"path",path);
		sb.append(" />\r\n");
	}

}