package com.gerken.xaa.model.refimpl;

import java.util.ArrayList;

import org.w3c.dom.Node;

public class ExemplarProject extends ModelElement {

	private String			name="";
	private String 			location;
	private String			id;
	private Object			source;
	
	private ArrayList<ExemplarFile> 	exemplarFiles = new ArrayList<ExemplarFile>();
	private ArrayList<ExemplarFolder>	exemplarFolders = new ArrayList<ExemplarFolder>();
	private ExemplarFile[]				exemplarFile = null;
	private ExemplarFolder[] 			exemplarFolder = null;
	
	private ReferenceImplementation refImpl;
	
	public ExemplarProject(ReferenceImplementation refImpl, String name, String location) {
		super();
		this.refImpl = refImpl;
		this.name = name;
		this.location = location;
	}

	public ExemplarProject(Node node) {
		this.name = getAttr(node,"name");
		this.id = getAttr(node,"id");
		this.location = getAttr(node,"location");
		Node kid[] = getChildren(node);
		for (int k = 0; k < kid.length; k++) {
			String name = kid[k].getNodeName();
			if (name.equals("file")) { addExemplarFile(new ExemplarFile(kid[k])); }
			if (name.equals("folder")) { addExemplarFolder(new ExemplarFolder(kid[k])); }
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExemplarFile[] getExemplarFile() {
		if (exemplarFile == null) {
			exemplarFile = new ExemplarFile[exemplarFiles.size()];
			exemplarFiles.toArray(exemplarFile);
		}
		return exemplarFile;
	}

	public void addExemplarFile(ExemplarFile anExemplarFile) {
		exemplarFiles.add(anExemplarFile);
		exemplarFile = null;
	}

	public ExemplarFolder[] getExemplarFolder() {
		if (exemplarFolder == null) {
			exemplarFolder = new ExemplarFolder[exemplarFolders.size()];
			exemplarFolders.toArray(exemplarFolder);
		}
		return exemplarFolder;
	}

	public void addExemplarFolder(ExemplarFolder anExemplarFolder) {
		exemplarFolders.add(anExemplarFolder);
		exemplarFolder = null;
	}

	public ReferenceImplementation getRefImpl() {
		return refImpl;
	}

	public void setRefImpl(ReferenceImplementation refImpl) {
		this.refImpl = refImpl;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<project");
		writeAttr(sb,"name",name);
		writeAttr(sb,"id",id);
		writeAttr(sb,"location",location);
		sb.append(">\r\n");
		for (int f = 0; f < getExemplarFile().length; f++) {
			getExemplarFile()[f].writeTo(sb);
		}
		for (int f = 0; f < getExemplarFolder().length; f++) {
			getExemplarFolder()[f].writeTo(sb);
		}
		sb.append("\t</project>\r\n");
	}

	public String nextAvailEntryName() {
		return refImpl.nextAvailEntryName();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}