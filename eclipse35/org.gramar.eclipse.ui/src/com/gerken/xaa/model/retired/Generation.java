package com.gerken.xaa.model.retired;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Generation extends ModelElement {
 
	private String 					xformID;
	
	private ArrayList<Artifact> 	artifacts = new ArrayList<Artifact>();
	private Artifact[]				artifact;
	
	private ArrayList<Replacement> 	replacements = new ArrayList<Replacement>();
	private Replacement[]			replacement;
	
	private ArrayList<Scope> scopes = new ArrayList<Scope>();
	private Scope[]			 scope;
	                
	public Generation() {
	}

	public static Generation loadFrom(IProject project) {
		IFile file = (IFile) project.findMember(".xform");
		InputStream is = null;
		try {
			is = file.getContents();
			Document doc = readData(is);
			Node root = doc.getFirstChild();
			String name = root.getNodeName();
			if (name.equals("generation")) { return new Generation(root); }
		} catch (CoreException e) {
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			try { is.close(); } catch (IOException e) {}
		}
		return null;
	}

	public static Document readData(InputStream is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document newDocument = parser.parse(is);
		newDocument.normalize();
		return newDocument;
	}
	
	public Generation(Node node) {
		this.xformID = getAttr(node,"xformID");
		Node kid[] = getChildren(node);
		for (int k = 0; k < kid.length; k++) {
			String name = kid[k].getNodeName();
			if (name.equals("scope")) { addScope(new Scope(kid[k])); }
			if (name.equals("file")) { addArtifact(new File(kid[k])); }
			if (name.equals("project")) { addArtifact(new Project(kid[k])); }
			if (name.equals("replacement")) { addReplacement(new Replacement(kid[k])); }
		}
	}

	public Artifact[] getArtifacts() {
		if (artifact == null) {
			artifact = new Artifact[artifacts.size()];
			artifacts.toArray(artifact);
		}
		return artifact;
	}

	public void addArtifact(Artifact anArtifact) {
		artifacts.add(anArtifact);
		artifact = null;
	}

	public Replacement[] getReplacements() {
		if (replacement == null) {
			replacement = new Replacement[replacements.size()];
			replacements.toArray(replacement);
		}
		return replacement;
	}

	public void addReplacement(Replacement aReplacement) {
		replacements.add(aReplacement);
		replacement = null;
	}

	public Scope[] getScopes() {
		if (scope == null) {
			scope = new Scope[scopes.size()];
			scopes.toArray(scope);
		}
		return scope;
	}

	public void addScope(Scope aScope) {
		scopes.add(aScope);
		scope = null;
	}

	public void writeTo(StringBuffer sb) {
		sb.append("<generation");
		writeAttr(sb,"xformID",xformID);
		sb.append(" >\r\n");
		for (int i = 0; i < getScopes().length; i++) {
			getScopes()[i].writeTo(sb);
		}
		for (int i = 0; i < getArtifacts().length; i++) {
			getArtifacts()[i].writeTo(sb);
		}
		for (int i = 0; i < getReplacements().length; i++) {
			getReplacements()[i].writeTo(sb);
		}
		sb.append("</generation>\r\n");
	}

	public String getXformID() {
		return xformID;
	}

	public void setXformID(String xformID) {
		this.xformID = xformID;
	}

	public Project getProject(String projectID) {
		getArtifacts();
		for (int i = 0; i < artifact.length; i++) {
			if (artifact[i] instanceof Project) {
				Project project = (Project) artifact[i];
				if (project.getProjectID().equals(projectID)) {
					return project;
				}
			}
		}
		return null;
	}

	
}
