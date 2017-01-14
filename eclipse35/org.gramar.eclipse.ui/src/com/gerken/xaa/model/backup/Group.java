package com.gerken.xaa.model.backup;

		// Begin imports
import java.util.ArrayList;
import java.util.Collection;

import org.w3c.dom.Node;
		// End imports

public class Group extends ModelElement {
	
	private String		name;
	private String		id;
	private String		parentId;
	private boolean		root;
	
	private ArrayList<CreateProject> createProjects = new ArrayList<CreateProject>();
	private CreateProject[]		createProject;
	private ArrayList<CreateFolder> createFolders = new ArrayList<CreateFolder>();
	private CreateFolder[]		createFolder;
	private ArrayList<CreateFile> createFiles = new ArrayList<CreateFile>();
	private CreateFile[]		createFile;
	private ArrayList<Token> tokens = new ArrayList<Token>();
	private Token[]		token;

		// Begin custom variables

		// End custom variables

		// Begin custom methods

		// End custom methods

	public Group() {
		super();
	}

	public Group(Node node) {
		this.name = getAttr(node,"name");
		this.id = getAttr(node,"id");
		this.parentId = getAttr(node,"parentId");
		this.root = getBooleanAttr(node,"root");
		Node kid[] = getChildren(node);
		for (int k = 0; k < kid.length; k++) {
			String name = kid[k].getNodeName();
			if (name.equals("createProject")) { addCreateProject(new CreateProject(kid[k])); }
			if (name.equals("createFolder")) { addCreateFolder(new CreateFolder(kid[k])); }
			if (name.equals("createFile")) { addCreateFile(new CreateFile(kid[k])); }
			if (name.equals("token")) { addToken(new Token(kid[k])); }
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean getRoot() {
		return root;
	}

	public void setRoot(boolean root) {
		this.root = root;
	}

	public CreateProject[] getCreateProjects() {
		if (createProject == null) {
			createProject = new CreateProject[createProjects.size()];
			createProjects.toArray(createProject);
		}
		return createProject;
	}

	public void addCreateProject(CreateProject _another) {
		createProjects.add(_another);
		createProject = null;
		_another.setParent(this);
	}

	public void removeCreateProject(CreateProject child) {
		createProjects.remove(child);
		createProject = null;
	}

	public CreateFolder[] getCreateFolders() {
		if (createFolder == null) {
			createFolder = new CreateFolder[createFolders.size()];
			createFolders.toArray(createFolder);
		}
		return createFolder;
	}

	public void addCreateFolder(CreateFolder _another) {
		createFolders.add(_another);
		createFolder = null;
		_another.setParent(this);
	}

	public void removeCreateFolder(CreateFolder child) {
		createFolders.remove(child);
		createFolder = null;
	}

	public CreateFile[] getCreateFiles() {
		if (createFile == null) {
			createFile = new CreateFile[createFiles.size()];
			createFiles.toArray(createFile);
		}
		return createFile;
	}

	public void addCreateFile(CreateFile _another) {
		createFiles.add(_another);
		createFile = null;
		_another.setParent(this);
	}

	public void removeCreateFile(CreateFile child) {
		createFiles.remove(child);
		createFile = null;
	}

	public Token[] getTokens() {
		if (token == null) {
			token = new Token[tokens.size()];
			tokens.toArray(token);
		}
		return token;
	}

	public void addToken(Token _another) {
		tokens.add(_another);
		token = null;
		_another.setParent(this);
	}

	public void removeToken(Token child) {
		tokens.remove(child);
		token = null;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<group");
		writeAttr(sb,"name",name);
		writeAttr(sb,"id",id);
		writeAttr(sb,"parentId",parentId);
		writeAttr(sb,"root",root);
		// Begin custom attributes

		// End custom attributes
		sb.append(" >\r\n");
		for (int i = 0; i < getCreateProjects().length; i++) {
			getCreateProjects()[i].writeTo(sb);
		}
		for (int i = 0; i < getCreateFolders().length; i++) {
			getCreateFolders()[i].writeTo(sb);
		}
		for (int i = 0; i < getCreateFiles().length; i++) {
			getCreateFiles()[i].writeTo(sb);
		}
		for (int i = 0; i < getTokens().length; i++) {
			getTokens()[i].writeTo(sb);
		}
		sb.append("</group>\r\n");
	}

	public void removeChild(ModelElement child) {
		if (child instanceof CreateProject) {
			removeCreateProject((CreateProject)child);
		}
		if (child instanceof CreateFolder) {
			removeCreateFolder((CreateFolder)child);
		}
		if (child instanceof CreateFile) {
			removeCreateFile((CreateFile)child);
		}
		if (child instanceof Token) {
			removeToken((Token)child);
		}
	}

}
