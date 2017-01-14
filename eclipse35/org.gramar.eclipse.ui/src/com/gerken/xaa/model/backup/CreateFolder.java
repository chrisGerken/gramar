package com.gerken.xaa.model.backup;

		// Begin imports
import java.util.ArrayList;

import org.w3c.dom.Node;
		// End imports

public class CreateFolder extends ModelElement {
	
	private String		id;
	private String		oPath;
	private String		projectId;
	private String		purpose;
	private String		projectExpr;
	private String		folderExpr;
	

		// Begin custom variables

		// End custom variables

		// Begin custom methods

		// End custom methods

	public CreateFolder() {
		super();
	}

	public CreateFolder(Node node) {
		this.id = getAttr(node,"id");
		this.oPath = getAttr(node,"oPath");
		this.projectId = getAttr(node,"projectId");
		this.purpose = getAttr(node,"purpose");
		this.projectExpr = getCdata(node,"projectExpr");
		this.folderExpr = getCdata(node,"folderExpr");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOPath() {
		return oPath;
	}

	public void setOPath(String oPath) {
		this.oPath = oPath;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getProjectExpr() {
		return projectExpr;
	}

	public void setProjectExpr(String projectExpr) {
		this.projectExpr = projectExpr;
	}

	public String getFolderExpr() {
		return folderExpr;
	}

	public void setFolderExpr(String folderExpr) {
		this.folderExpr = folderExpr;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<createFolder");
		writeAttr(sb,"id",id);
		writeAttr(sb,"oPath",oPath);
		writeAttr(sb,"projectId",projectId);
		writeAttr(sb,"purpose",purpose);
		// Begin custom attributes

		// End custom attributes
		sb.append(" >\r\n");
		writeCdata(sb,"projectExpr",projectExpr);
		writeCdata(sb,"folderExpr",folderExpr);
		sb.append("</createFolder>\r\n");
	}

	public void removeChild(ModelElement child) {
	}

}
