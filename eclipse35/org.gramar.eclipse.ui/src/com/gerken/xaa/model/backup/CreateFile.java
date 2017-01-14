package com.gerken.xaa.model.backup;

		// Begin imports
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
		// End imports

public class CreateFile extends ModelElement {
	
	private String		id;
	private String		projectId;
	private String		oPath;
	private String		src;
	private boolean		replace;
	private boolean		binary;
	private boolean		changeableName;
	private boolean		purposeAsTokenName;
	private String		purpose;
	private String		projectExpr;
	private String		folderExpr;
	private String		nameExpr;
	

		// Begin custom variables

		// End custom variables

		// Begin custom methods

	public String getContents() {
		InputStream is = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			IFile file = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(new Path("/"+getSrc()));
			is = file.getContents();
			byte b[] = new byte[4000];
			int read = is.read(b);
			while (read > -1) {
				baos.write(b,0,read);
				read = is.read();
			}
		} catch (Throwable t) {
		} finally  {
			try { is.close(); } catch (Throwable t) {}
		}
		return baos.toString();
	}

	public Document getDocument() {
		InputStream is = null;
		Document doc = null;
		try {
			IFile file = (IFile) ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(getSrc()));
			is = file.getContents();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			doc = parser.parse(is);
			doc.normalize();
		} catch (Throwable t) {
		} finally  {
			try { is.close(); } catch (Throwable t) {}
		}
		return doc;
	}

		// End custom methods

	public CreateFile() {
		super();
	}

	public CreateFile(Node node) {
		this.id = getAttr(node,"id");
		this.projectId = getAttr(node,"projectId");
		this.oPath = getAttr(node,"oPath");
		this.src = getAttr(node,"src");
		this.replace = getBooleanAttr(node,"replace");
		this.binary = getBooleanAttr(node,"binary");
		this.changeableName = getBooleanAttr(node,"changeableName");
		this.purposeAsTokenName = getBooleanAttr(node,"purposeAsTokenName");
		this.purpose = getAttr(node,"purpose");
		this.projectExpr = getCdata(node,"projectExpr");
		this.folderExpr = getCdata(node,"folderExpr");
		this.nameExpr = getCdata(node,"nameExpr");
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

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public boolean getReplace() {
		return replace;
	}

	public void setReplace(boolean replace) {
		this.replace = replace;
	}

	public boolean getBinary() {
		return binary;
	}

	public void setBinary(boolean binary) {
		this.binary = binary;
	}

	public boolean getChangeableName() {
		return changeableName;
	}

	public void setChangeableName(boolean changeableName) {
		this.changeableName = changeableName;
	}

	public boolean getPurposeAsTokenName() {
		return purposeAsTokenName;
	}

	public void setPurposeAsTokenName(boolean purposeAsTokenName) {
		this.purposeAsTokenName = purposeAsTokenName;
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

	public String getNameExpr() {
		return nameExpr;
	}

	public void setNameExpr(String nameExpr) {
		this.nameExpr = nameExpr;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<createFile");
		writeAttr(sb,"id",id);
		writeAttr(sb,"projectId",projectId);
		writeAttr(sb,"oPath",oPath);
		writeAttr(sb,"src",src);
		writeAttr(sb,"replace",replace);
		writeAttr(sb,"binary",binary);
		writeAttr(sb,"changeableName",changeableName);
		writeAttr(sb,"purposeAsTokenName",purposeAsTokenName);
		writeAttr(sb,"purpose",purpose);
		// Begin custom attributes

		// End custom attributes
		sb.append(" >\r\n");
		writeCdata(sb,"projectExpr",projectExpr);
		writeCdata(sb,"folderExpr",folderExpr);
		writeCdata(sb,"nameExpr",nameExpr);
		sb.append("</createFile>\r\n");
	}

	public void removeChild(ModelElement child) {
	}

}
