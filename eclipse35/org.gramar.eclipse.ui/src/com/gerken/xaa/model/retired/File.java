package com.gerken.xaa.model.retired;

import org.w3c.dom.Node;

public class File extends Artifact {

	private String 		originalPath;
	private String 		nameExpression;
	private String		folderExpression;
	
	private String		projectID;

	public File(Generation generation) {
		super(generation);
	}

	public File(Node node) {
		super(node);
		this.originalPath = getAttr(node,"originalPath");
		this.nameExpression = getAttr(node,"nameExpression");
		this.folderExpression = getAttr(node,"folderExpression");
	}

	public String getOriginalPath() {
		return originalPath;
	}

	public void setOriginalPath(String originalPath) {
		this.originalPath = originalPath;
	}

	public String getNameExpression() {
		return nameExpression;
	}

	public void setNameExpression(String nameExpression) {
		this.nameExpression = nameExpression;
	}

	public String getFolderExpression() {
		return folderExpression;
	}

	public void setFolderExpression(String folderExpression) {
		this.folderExpression = folderExpression;
	}
	
	public String getProjectNameExpression() {
		return getProject().getNameExpression();
	}

	private Project getProject() {
		return getGeneration().getProject(projectID);
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t\t<file");
		writeArtifactAttributes(sb);

		writeAttr(sb,"originalPath",originalPath);
		writeAttr(sb,"nameExpression",nameExpression);
		writeAttr(sb,"folderExpression",folderExpression);
		sb.append(" />\r\n");
	}

	public String parseName(String path) {
		int offset = path.lastIndexOf('/');
		if (offset == -1) {
			return path;
		}
		return path.substring(offset+1);
	}
	
	public String parseFolder(String path) {
		int offset = path.lastIndexOf('/');
		if (offset == -1) {
			return "";
		}
		return path.substring(0,offset);
	}

}
