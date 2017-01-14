package com.gerken.xaa.sme;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.w3c.dom.Node;

import com.gerken.xaa.model.xform.CreateFile;
import com.gerken.xaa.model.xform.CreateFolder;
import com.gerken.xaa.model.xform.CreateProject;
import com.gerken.xaa.model.xform.Group;
import com.gerken.xaa.model.xform.JavaPkg;
import com.gerken.xaa.model.xform.Replacement;
import com.gerken.xaa.model.xform.Token;
import com.gerken.xaa.model.xform.Xform;

public class MavenMentor extends AbstractMentor implements IXaaMentor {

	private ArrayList<String> mavenProjectIds = null;

	public MavenMentor() {
	}

	public boolean applicable(Xform xform) {
		mavenProjectIds = new ArrayList<String>();
		for (CreateProject cp: xform.projects()) {
			if (xform.getFile(cp.getId(), "pom.xml") != null) {
				mavenProjectIds.add(cp.getId());
			}
		}
		return !mavenProjectIds.isEmpty();
	}

	public String getName() {
		return "Maven Artifact";
	}

	public IXaaMentorWizardPage getWizardPage() {
		return null;
	}

	public boolean hasWizardPage() {
		return false;
	}

	public void tweak(Xform xform) {
		
		Iterator<String> ids = mavenProjectIds.iterator();
		while (ids.hasNext()) {
			
			String prjId = ids.next();
			
			for (CreateFile cf: xform.getFilesStartingWith(prjId,"target")) {
				cf.remove();
			}
			
			for (CreateFolder cf: xform.getFoldersStartingWith(prjId,"/target")) {
				cf.remove();
			}
			
		}
		
	}

}
