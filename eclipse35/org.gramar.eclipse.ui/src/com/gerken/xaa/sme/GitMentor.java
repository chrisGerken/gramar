package com.gerken.xaa.sme;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.gerken.xaa.model.xform.CreateFile;
import com.gerken.xaa.model.xform.CreateFolder;
import com.gerken.xaa.model.xform.CreateProject;
import com.gerken.xaa.model.xform.Xform;

public class GitMentor extends AbstractMentor implements IXaaMentor {

	private Set<String> projectIds = null;

	public GitMentor() {
	}

	private String[] directoryFilterList = new String[] { ".git" };

	public boolean applicable(Xform xform) {
		projectIds = new HashSet<String>();
		for (CreateProject cp : xform.projects()) {
			for (String dirName : directoryFilterList) {
				if (xform.getFilesStartingWith(cp.getId(), dirName).size() > 0) {
					projectIds.add(cp.getId());
				}
			}
		}
		return projectIds.size() > 0;
	}

	public String getName() {
		return "Git Artifact";
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

			for (String dir : directoryFilterList) {
				for (CreateFile cf : xform.getFilesStartingWith(prjId, dir)) {
					cf.remove();
				}

				for (CreateFolder cf : xform.getFoldersStartingWith(prjId, "/"
						+ dir)) {
					cf.remove();
				}
			}

		}

	}
}
