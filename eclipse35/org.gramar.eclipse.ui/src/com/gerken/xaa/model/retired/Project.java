package com.gerken.xaa.model.retired;

import java.util.ArrayList;

import org.w3c.dom.Node;

public class Project extends Artifact {
	
	private String		originalName;
	private String		nameExpression;
	private String 		projectID;
	
	private ArrayList<File> files = new ArrayList<File>();
	private File[]		file;

	public Project(Generation generation) {
		super(generation);
	}

	public Project(Node node) {
		super(node);
		this.originalName = getAttr(node,"originalName");
		this.nameExpression = getAttr(node,"nameExpression");
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getNameExpression() {
		return nameExpression;
	}

	public void setNameExpression(String nameExpression) {
		this.nameExpression = nameExpression;
	}

	public File[] getFiles() {
		if (file == null) {
			file = new File[files.size()];
			files.toArray(file);
		}
		return file;
	}

	public void addFile(File aFile) {
		files.add(aFile);
		file = null;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<project");
		writeArtifactAttributes(sb);
		
		writeAttr(sb,"originalName",originalName);
		writeAttr(sb,"nameExpression",nameExpression);
		sb.append(" >\r\n");
		for (int f = 0; f < getFiles().length; f++) {
			getFiles()[f].writeTo(sb);
		}
		sb.append("</project>\r\n");
	}

	public String getProjectID() {
		return projectID;
	}

	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}

}
