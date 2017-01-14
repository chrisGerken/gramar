package com.gerken.xaa.model.refimpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.MultiStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.gerken.xaa.model.xform.Group;
import com.gerken.xaa.model.xform.Replacement;
import com.gerken.xaa.model.xform.Xform;


public class ReferenceImplementation extends ModelElement {
	
	private ArrayList<ExemplarProject>		exemplarProjects = new ArrayList<ExemplarProject>();
	private ExemplarProject[]				exemplarProject;
	private int						nextID = 0;
	private String					exemplarID;
	private String					pathSeparator;
	private String					refImplProjectName;
	private String					refImplFolder;

	public ReferenceImplementation() {
		super();
	}
	
	public ReferenceImplementation(Node node) {
		this.exemplarID = getAttr(node,"exemplarID");
		this.nextID = getIntAttr(node,"nextID");
		this.pathSeparator = getAttr(node,"pathSeparator");
		this.refImplProjectName = getAttr(node,"refImplProjectName");
		this.refImplFolder = getAttr(node,"refImplFolder");
		Node kid[] = getChildren(node);
		for (int k = 0; k < kid.length; k++) {
			String name = kid[k].getNodeName();
			if (name.equals("project")) { addExemplarProject(new ExemplarProject(kid[k])); }
		}
	}

	public static ReferenceImplementation loadFrom(IProject project) {
		try {
			IFile file = (IFile) project.findMember("manifest.xml");
			InputStream is = null;
			is = file.getContents();
			return loadFrom(is);
		} catch (CoreException e) {
			e.toString();
		}
		return null;
	}

	public static ReferenceImplementation loadFrom(InputStream is) {
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = factory.newDocumentBuilder();
			doc = parser.parse(is);
			doc.normalize();
			Node root = doc.getFirstChild();
			String name = root.getNodeName();
			if (name.equals("referenceImplementation")) { return new ReferenceImplementation(root); }
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try { is.close(); } catch (IOException e) {}
		}
		return null;
	}

	public ExemplarProject[] getExemplarProject() {
		if (exemplarProject == null) {
			exemplarProject = new ExemplarProject[exemplarProjects.size()];
			exemplarProjects.toArray(exemplarProject);
		}
		return exemplarProject;
	}

	public void addExemplarProject(ExemplarProject aExemplarProject) {
		exemplarProjects.add(aExemplarProject);
		exemplarProject = null;
	}
	
	public ExemplarFile[] allFiles() {

		ArrayList<ExemplarFile> list = new ArrayList<ExemplarFile>();
		
		ExemplarProject[] ep = getExemplarProject();
		for (int p = 0; p < ep.length; p++) {
			
			ExemplarFile[] ef = ep[p].getExemplarFile();
			for (int f = 0; f < ef.length; f++) {
			
				list.add(ef[f]);
			}			
		}
		ExemplarFile ef[] = new ExemplarFile[list.size()];
		list.toArray(ef);
		return ef;
		
	}

	public void writeTo(StringBuffer sb) {
		sb.append("<referenceImplementation");
		writeAttr(sb,"exemplarID",exemplarID);
		writeAttr(sb,"nextID",String.valueOf(nextID));
		writeAttr(sb,"pathSeparator",pathSeparator);
		writeAttr(sb,"refImplProjectName",refImplProjectName);
		writeAttr(sb,"refImplFolder",refImplFolder);
		sb.append(" >\r\n");
		for (int p = 0; p < getExemplarProject().length; p++) {
			getExemplarProject()[p].writeTo(sb);
		}
		sb.append("</referenceImplementation>\r\n");
	}

	public int getNextID() {
		return nextID;
	}

	public void setNextID(int nextID) {
		this.nextID = nextID;
	}
	
	public String nextAvailEntryName() {
		nextID++;
		String result = "art" + String.valueOf(nextID+1000000).substring(1);
		return result;
	}

	public String getExemplarID() {
		return exemplarID;
	}

	public void setExemplarID(String exemplarID) {
		this.exemplarID = exemplarID;
	}

	public String getRefImplProjectName() {
		return refImplProjectName;
	}

	public void setRefImplProjectName(String refImplProjectName) {
		this.refImplProjectName = refImplProjectName;
	}

	public void setPathSeparator(String pathSeparator) {
		this.pathSeparator = pathSeparator;
	}

	public String getPathSeparator() {
		return pathSeparator;
	}

	public String getRefImplFolder() {
		return refImplFolder;
	}

	public void setRefImplFolder(String refImplFolder) {
		this.refImplFolder = refImplFolder;
	}

	public MultiStatus reportDelta(ReferenceImplementation refimpl2) {
		
		
		// TODO Auto-generated method stub
		return null;
	}

}
