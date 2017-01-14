package com.gerken.xaa.sme;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.gerken.xaa.model.xform.CreateFile;
import com.gerken.xaa.model.xform.CreateFolder;
import com.gerken.xaa.model.xform.CreateProject;
import com.gerken.xaa.model.xform.Xform;

public class IntelliJIdeaMentor extends AbstractMentor implements IXaaMentor {

	private Set<String> projectIds = null;

	public IntelliJIdeaMentor() {
	}

	private String[] filters = new String[] { ".idea/workspace.xml",
			".idea/tasks.xml", ".idea/vcs.xml",
			".idea/jsLibraryMappings.xml",
			".idea/dictionaries",
			// files that may contain passwords
			".idea/dataSources.ids", ".idea/dataSources.xml",
			".idea/dataSources.local.xml", ".idea/sqlDataSources.xml",
			".idea/dynamic.xml", ".idea/uiDesigner.xml",

			// Gradle:
			".idea/gradle.xml", ".idea/libraries",

			// Mongo Explorer plugin:
			".idea/mongoSettings.xml"

	};

	public boolean applicable(Xform xform) {
		projectIds = new HashSet<String>();
		for (CreateProject cp : xform.projects()) {
			for (String fileName : filters) {
				if (xform.getFile(cp.getId(), fileName) != null) {
					projectIds.add(cp.getId());
					return true;
				}

				if (xform.getFile(cp.getId(), fileName) != null) {
					projectIds.add(cp.getId());
				}
			}
		}
		return projectIds.size() > 0;
	}

	public String getName() {
		return "IntelliJ Idea Artifact";
	}

	public IXaaMentorWizardPage getWizardPage() {
		return null;
	}

	public boolean hasWizardPage() {
		return false;
	}

	public void tweak(Xform xform) {

		Iterator<String> ids = projectIds.iterator();
		while (ids.hasNext()) {

			String prjId = ids.next();

			for (String fileName : filters) {
				for (CreateFile cf : xform
						.getFilesStartingWith(prjId, fileName)) {
					cf.remove();
				}
				String dirName = "/" + fileName;
				for (CreateFolder cf : xform.getFoldersStartingWith(prjId,
						dirName)) {
					cf.remove();
				}
			}
		}
	}
}
