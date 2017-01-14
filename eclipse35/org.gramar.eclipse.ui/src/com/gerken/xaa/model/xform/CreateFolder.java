package com.gerken.xaa.model.xform;

		// Begin imports
import java.util.ArrayList;

import org.w3c.dom.Node;
		// End imports

public class CreateFolder extends ModelElement {
	
	private String		id;
	private String		projectId;
	private String		oPath;
	private String		purpose;
	private String		projectExpr;
	private String		folderExpr;

		// Begin custom variables

		// End custom variables

	public CreateFolder() {
		super();
	}

	public CreateFolder(Node node) {
		this.id = getAttr(node,"id");
		this.projectId = getAttr(node,"projectId");
		this.oPath = getAttr(node,"oPath");
		this.purpose = getAttr(node,"purpose");
		this.projectExpr = getAttr(node,"projectExpr");
		this.folderExpr = getAttr(node,"folderExpr");

		// Begin read custom attributes

		// End read custom attributes

		Node kid[] = getChildren(node);
		for (int k = 0; k < kid.length; k++) {
			String name = kid[k].getNodeName();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getOPath() {
		return oPath;
	}

	public void setOPath(String oPath) {
		this.oPath = oPath;
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
		writeAttr(sb,"projectId",projectId);
		writeAttr(sb,"oPath",oPath);
		writeAttr(sb,"purpose",purpose);
		writeAttr(sb,"projectExpr",projectExpr);
		writeAttr(sb,"folderExpr",folderExpr);

		// Begin write custom attributes

		// End write custom attributes

		sb.append(" >\r\n");

		sb.append("\t\t</createFolder>\r\n");
	}

	public void removeChild(ModelElement child) {
	}

		// Begin custom methods

		// End custom methods

}
