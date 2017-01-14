package com.gerken.xaa.model.xform;

		// Begin imports
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.w3c.dom.NodeList;

		// End imports

public class Xform extends ModelElement {
	
	private String		xformId;
	private String		id;
	private String		nextID;
	private ArrayList<Group> groupList = new ArrayList<Group>();
	private Group[]		group;
	private ArrayList<Replacement> replacementList = new ArrayList<Replacement>();
	private Replacement[]		replacement;
	private ArrayList<NewToken> newTokenList = new ArrayList<NewToken>();
	private NewToken[]		newToken;
	private ArrayList<Ignored> ignoredList = new ArrayList<Ignored>();
	private Ignored[]		ignored;

		// Begin custom variables

	private Group		rootGroup = null;
	
		// End custom variables

	public Xform() {
		super();
	}

	public Xform(Node node) {
		this.xformId = getAttr(node,"xformId");
		this.id = getAttr(node,"id");
		this.nextID = getAttr(node,"nextID");

		// Begin read custom attributes

		// End read custom attributes

		Node kid[] = getChildren(node);
		for (int k = 0; k < kid.length; k++) {
			String name = kid[k].getNodeName();
			if ("group".equals(name)) { addGroup(new Group(kid[k])); }
			if ("replacement".equals(name)) { addReplacement(new Replacement(kid[k])); }
			if ("newToken".equals(name)) { addNewToken(new NewToken(kid[k])); }
			if ("ignored".equals(name)) { addIgnored(new Ignored(kid[k])); }
		}
	}

	public String getXformId() {
		return xformId;
	}

	public void setXformId(String xformId) {
		this.xformId = xformId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNextID() {
		return nextID;
	}

	public void setNextID(String nextID) {
		this.nextID = nextID;
	}

	public Group[] getGroupArray() {
		if (group == null) {
			group = new Group[groupList.size()];
			groupList.toArray(group);
		}
		return group;
	}

	public void addGroup(Group _another) {
		groupList.add(_another);
		group = null;
		_another.setParent(this);
	}

	public void removeGroup(Group child) {
		groupList.remove(child);
		group = null;
	}

	public Replacement[] getReplacementArray() {
		if (replacement == null) {
			replacement = new Replacement[replacementList.size()];
			replacementList.toArray(replacement);
		}
		return replacement;
	}

	public void addReplacement(Replacement _another) {
		replacementList.add(_another);
		replacement = null;
		_another.setParent(this);
	}

	public void removeReplacement(Replacement child) {
		replacementList.remove(child);
		replacement = null;
	}

	public NewToken[] getNewTokenArray() {
		if (newToken == null) {
			newToken = new NewToken[newTokenList.size()];
			newTokenList.toArray(newToken);
		}
		return newToken;
	}

	public void addNewToken(NewToken _another) {
		newTokenList.add(_another);
		newToken = null;
		_another.setParent(this);
	}

	public void removeNewToken(NewToken child) {
		newTokenList.remove(child);
		newToken = null;
	}

	public Ignored[] getIgnoredArray() {
		if (ignored == null) {
			ignored = new Ignored[ignoredList.size()];
			ignoredList.toArray(ignored);
		}
		return ignored;
	}

	public void addIgnored(Ignored _another) {
		ignoredList.add(_another);
		ignored = null;
		_another.setParent(this);
	}

	public void removeIgnored(Ignored child) {
		ignoredList.remove(child);
		ignored = null;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("\t<xform");
		writeAttr(sb,"xformId",xformId);
		writeAttr(sb,"id",id);
		writeAttr(sb,"nextID",nextID);

		// Begin write custom attributes

		// End write custom attributes

		sb.append(" >\r\n");
		for (int i = 0; i < getGroupArray().length; i++) {
			getGroupArray()[i].writeTo(sb);
		}
		for (int i = 0; i < getReplacementArray().length; i++) {
			getReplacementArray()[i].writeTo(sb);
		}
		for (int i = 0; i < getNewTokenArray().length; i++) {
			getNewTokenArray()[i].writeTo(sb);
		}
		for (int i = 0; i < getIgnoredArray().length; i++) {
			getIgnoredArray()[i].writeTo(sb);
		}

		sb.append("\t\t</xform>\r\n");
	}

	public void removeChild(ModelElement child) {
		if (child instanceof Group) {
			removeGroup((Group)child);
		}
		if (child instanceof Replacement) {
			removeReplacement((Replacement)child);
		}
		if (child instanceof NewToken) {
			removeNewToken((NewToken)child);
		}
		if (child instanceof Ignored) {
			removeIgnored((Ignored)child);
		}
	}

		// Begin custom methods

	public String nextId() {
		int val = Integer.parseInt(nextID);
		val++;
		nextID = String.valueOf(val);
		return nextID;
	}

	public static Xform loadFrom(IProject project) {
		IFile file = (IFile) project.findMember("xform.xform");
		InputStream is = null;
		try {
			is = file.getContents();
			Document doc = readData(is);
			Node root = doc.getFirstChild();
			String name = root.getNodeName();
			Xform xform = null;
			if (name.equals("xform")) { xform = new Xform(root); }
			try { is.close(); } catch (IOException e) {}
			if (xform.processNewTokens()) {
				xform.persistTo(project);
			}
			return xform;
		} catch (CoreException e) {
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return null;
	}

	private boolean processNewTokens() {
		NewToken nt[] = getNewTokenArray();
		if (nt.length == 0) {
			return false;
		}
		for (int i = 0; i < nt.length; i++) {
			nt[i].process();
			nt[i].remove();
		}
		return true;
	}

	public static Document readData(InputStream is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document newDocument = parser.parse(is);
		newDocument.normalize();
		return newDocument;
	}

	public void persistTo(IProject project) {
		IFile file = (IFile) project.findMember("xform.xml");
		String content = xmlRepresentation();
		ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes());
		try {
			file.setContents(bais, true, true, new NullProgressMonitor());
		} catch (CoreException e) {}
	}

	public ArrayList<CreateFile> getFilesEndingWith(String projectId, String endString) { 
		ArrayList<CreateFile> result = new ArrayList<CreateFile>();
		Iterator<CreateFile> iter = allFiles().iterator();
		while (iter.hasNext()) {
			CreateFile candidate = iter.next();
			if ((candidate.getProjectId().equals(projectId)) && (candidate.getOPath().endsWith(endString))) {
				result.add(candidate);
			}
		}
		return result;
	}

	public ArrayList<CreateFile> getFilesStartingWith(String projectId,String startString) {
		ArrayList<CreateFile> result = new ArrayList<CreateFile>();
		Iterator<CreateFile> iter = allFiles().iterator();
		while (iter.hasNext()) {
			CreateFile candidate = iter.next();
			String oPath = candidate.getOPath();
			if ((candidate.getProjectId().equals(projectId)) && oPath.startsWith(startString)) {
				result.add(candidate);
			}
		}
		return result;
	}

	public ArrayList<CreateFolder> getFoldersStartingWith(String projectId,String startString) {
		ArrayList<CreateFolder> result = new ArrayList<CreateFolder>();
		Iterator<CreateFolder> iter = allFolders().iterator();
		while (iter.hasNext()) {
			CreateFolder candidate = iter.next();
			String oPath = candidate.getOPath();
			if ((candidate.getProjectId().equals(projectId)) && oPath.startsWith(startString)) {
				result.add(candidate);
			}
		}
		return result;
	}
	
	public String toJetTags(String buffer) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(buffer,"{}",true);
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.equals("{")) {
				sb.append("<c:get select=\"");
			} else if (token.equals("}")) {
				sb.append("\" />");
			} else {
				sb.append(token);
			}
		}
		return sb.toString();
	}	

	public ArrayList<CreateProject> projectsWithNature(String natureId) {
		ArrayList<CreateProject> result = new ArrayList<CreateProject>();
		for (int i = 0; i < getGroupArray().length; i++) {
			CreateProject cp[] = getGroupArray()[i].getCreateProjectArray();
			for (int p = 0; p < cp.length; p++) {
				if (cp[p].hasNature(natureId)) {
					result.add(cp[p]);
				}
			}
		}
		return result;
	}

	public ArrayList<CreateProject> projects() {
		ArrayList<CreateProject> result = new ArrayList<CreateProject>();
		for (int i = 0; i < getGroupArray().length; i++) {
			CreateProject cp[] = getGroupArray()[i].getCreateProjectArray();
			for (int p = 0; p < cp.length; p++) {
				result.add(cp[p]);
			}
		}
		return result;
	}
	
	public Xform getXform() {
		return this;
	}

	public CreateFile getFile(String projectId, String path) {
		ArrayList<CreateFile> allFiles = allFiles();
		Iterator<CreateFile> iter = allFiles.iterator();
		while (iter.hasNext()) {
			CreateFile candidate = iter.next();
			if ((candidate.getProjectId().equals(projectId)) && (candidate.getOPath().equals(path))) {
				return candidate;
			}
		}
		return null;
	}

	public ArrayList<CreateFile> allFiles() {
		ArrayList<CreateFile> result = new ArrayList<CreateFile>();
		for (int i = 0; i < getGroupArray().length; i++) {
			Group group = getGroupArray()[i];
			for (int f = 0; f < group.getCreateFileArray().length; f++) {
				result.add(group.getCreateFileArray()[f]);
			}
		}
		return result;
	}

	public ArrayList<CreateFolder> allFolders() {
		ArrayList<CreateFolder> result = new ArrayList<CreateFolder>();
		for (int i = 0; i < getGroupArray().length; i++) {
			Group group = getGroupArray()[i];
			for (int f = 0; f < group.getCreateFolderArray().length; f++) {
				result.add(group.getCreateFolderArray()[f]);
			}
		}
		return result;
	}

	public ArrayList<CreateProject> allProjects() {
		ArrayList<CreateProject> result = new ArrayList<CreateProject>();
		for (int i = 0; i < getGroupArray().length; i++) {
			Group group = getGroupArray()[i];
			for (int f = 0; f < group.getCreateProjectArray().length; f++) {
				result.add(group.getCreateProjectArray()[f]);
			}
		}
		return result;
	}

	public CreateProject getProject(String anId) {
		Iterator<CreateProject> iter = allProjects().iterator();
		while (iter.hasNext()) {
			CreateProject candidate = iter.next();
			if (candidate.getId().equals(anId)) {
				return candidate;
			}
		}
		return null;
	}

	public Node getFileRootElement(String anId, String path) {
		CreateFile file = getFile(anId,path);
		NodeList list = file.getDocument().getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
				return list.item(i);
			}
		}
		return null;
	}

	public Group getRootGroup() {
		for (int g = 0; g < getGroupArray().length; g++ ) {
			if (group[g].getRoot()) {
				return group[g];
			}
		}
		return null;
	}

	public void removeFilesStartingWith(String prjId, String string) {
		Iterator<CreateFile> iter = getFilesStartingWith(prjId,string).iterator();
		while (iter.hasNext()) {
			iter.next().remove();
		}
	}

	public void removeFilesEndingWith(String prjId, String string) {
		Iterator<CreateFile> iter = getFilesEndingWith(prjId,string).iterator();
		while (iter.hasNext()) {
			iter.next().remove();
		}
	}

	public Group groupNamed(String groupName) {
		getGroupArray();
		for (int i = 0; i < group.length; i++) {
			if (group[i].getName().equals(groupName)) {
				return group[i];
			}
		}
		return null;
	}
	
		// End custom methods

}
