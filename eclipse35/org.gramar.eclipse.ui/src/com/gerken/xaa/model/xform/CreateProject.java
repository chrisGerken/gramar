package com.gerken.xaa.model.xform;

		// Begin imports
import java.util.ArrayList;

import org.w3c.dom.Node;
		// End imports

public class CreateProject extends ModelElement {
	
	private String		id;
	private String		oPath;
	private String		oLocation;
	private String		purpose;
	private String		projectExpr;
	private String		locationExpr;

		// Begin custom variables

		// End custom variables

	public CreateProject() {
		super();
	}

	public CreateProject(Node node) {
		this.id = getAttr(node,"id");
		this.oPath = getAttr(node,"oPath");
		this.oLocation = getAttr(node,"oLocation");
		this.purpose = getAttr(node,"purpose");
		this.projectExpr = getAttr(node,"projectExpr");
		this.locationExpr = getAttr(node,"locationExpr");

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

	public String getOPath() {
		return oPath;
	}

	public void setOPath(String oPath) {
		this.oPath = oPath;
	}

	public String getOLocation() {
		return oLocation;
	}

	public void setOLocation(String oLocation) {
		this.oLocation = oLocation;
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

	public String getLocationExpr() {
		return locationExpr;
	}

	public void setLocationExpr(String locationExpr) {
		this.locationExpr = locationExpr;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<createProject");
		writeAttr(sb,"id",id);
		writeAttr(sb,"oPath",oPath);
		writeAttr(sb,"oLocation",oLocation);
		writeAttr(sb,"purpose",purpose);
		writeAttr(sb,"projectExpr",projectExpr);
		writeAttr(sb,"locationExpr",locationExpr);

		// Begin write custom attributes

		// End write custom attributes

		sb.append(" >\r\n");

		sb.append("\t\t</createProject>\r\n");
	}

	public void removeChild(ModelElement child) {
	}

		// Begin custom methods

	public boolean hasNature(String natureId) {
		String content = "";
		try {
			content = getXform().getFile(getId(),".project").getContents();
		} catch (Throwable t) {}
		return (content.indexOf(natureId) > -1);
	}
	
		// End custom methods

}
