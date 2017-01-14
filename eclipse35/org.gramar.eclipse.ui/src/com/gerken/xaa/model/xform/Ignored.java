package com.gerken.xaa.model.xform;

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

public class Ignored extends ModelElement {
	
	private ArrayList<CreateProject> createProjectList = new ArrayList<CreateProject>();
	private CreateProject[]		createProject;
	private ArrayList<CreateFolder> createFolderList = new ArrayList<CreateFolder>();
	private CreateFolder[]		createFolder;
	private ArrayList<CreateFile> createFileList = new ArrayList<CreateFile>();
	private CreateFile[]		createFile;
	private ArrayList<Token> tokenList = new ArrayList<Token>();
	private Token[]		token;

		// Begin custom variables

		// End custom variables

	public Ignored() {
		super();
	}

	public Ignored(Node node) {

		// Begin read custom attributes

		// End read custom attributes

		Node kid[] = getChildren(node);
		for (int k = 0; k < kid.length; k++) {
			String name = kid[k].getNodeName();
			if ("createProject".equals(name)) { addCreateProject(new CreateProject(kid[k])); }
			if ("createFolder".equals(name)) { addCreateFolder(new CreateFolder(kid[k])); }
			if ("createFile".equals(name)) { addCreateFile(new CreateFile(kid[k])); }
			if ("token".equals(name)) { addToken(new Token(kid[k])); }
		}
	}

	public CreateProject[] getCreateProjectArray() {
		if (createProject == null) {
			createProject = new CreateProject[createProjectList.size()];
			createProjectList.toArray(createProject);
		}
		return createProject;
	}

	public void addCreateProject(CreateProject _another) {
		createProjectList.add(_another);
		createProject = null;
		_another.setParent(this);
	}

	public void removeCreateProject(CreateProject child) {
		createProjectList.remove(child);
		createProject = null;
	}

	public CreateFolder[] getCreateFolderArray() {
		if (createFolder == null) {
			createFolder = new CreateFolder[createFolderList.size()];
			createFolderList.toArray(createFolder);
		}
		return createFolder;
	}

	public void addCreateFolder(CreateFolder _another) {
		createFolderList.add(_another);
		createFolder = null;
		_another.setParent(this);
	}

	public void removeCreateFolder(CreateFolder child) {
		createFolderList.remove(child);
		createFolder = null;
	}

	public CreateFile[] getCreateFileArray() {
		if (createFile == null) {
			createFile = new CreateFile[createFileList.size()];
			createFileList.toArray(createFile);
		}
		return createFile;
	}

	public void addCreateFile(CreateFile _another) {
		createFileList.add(_another);
		createFile = null;
		_another.setParent(this);
	}

	public void removeCreateFile(CreateFile child) {
		createFileList.remove(child);
		createFile = null;
	}

	public Token[] getTokenArray() {
		if (token == null) {
			token = new Token[tokenList.size()];
			tokenList.toArray(token);
		}
		return token;
	}

	public void addToken(Token _another) {
		tokenList.add(_another);
		token = null;
		_another.setParent(this);
	}

	public void removeToken(Token child) {
		tokenList.remove(child);
		token = null;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<ignored");

		// Begin write custom attributes

		// End write custom attributes

		sb.append(" >\r\n");
		for (int i = 0; i < getCreateProjectArray().length; i++) {
			getCreateProjectArray()[i].writeTo(sb);
		}
		for (int i = 0; i < getCreateFolderArray().length; i++) {
			getCreateFolderArray()[i].writeTo(sb);
		}
		for (int i = 0; i < getCreateFileArray().length; i++) {
			getCreateFileArray()[i].writeTo(sb);
		}
		for (int i = 0; i < getTokenArray().length; i++) {
			getTokenArray()[i].writeTo(sb);
		}

		sb.append("\t\t</ignored>\r\n");
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

		// Begin custom methods

		// End custom methods

}
