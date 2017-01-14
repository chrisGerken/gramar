package com.gerken.xaa.model.prepare;

import org.eclipse.core.resources.IProject;

import com.gerken.xaa.model.refimpl.ExemplarFile;
import com.gerken.xaa.model.refimpl.ExemplarFolder;
import com.gerken.xaa.model.refimpl.ExemplarProject;
import com.gerken.xaa.model.refimpl.ReferenceImplementation;
import com.gerken.xaa.model.xform.CreateFile;
import com.gerken.xaa.model.xform.CreateFolder;
import com.gerken.xaa.model.xform.CreateProject;
import com.gerken.xaa.model.xform.Group;
import com.gerken.xaa.model.xform.Xform;

public class Converter {

	private static Converter instance = null;
	
	private Converter() {
	}

	public static Converter getInstance() {
		if (instance == null) {
			instance = new Converter();
		}
		return instance;
	}
	
	public Xform transform(IProject project) {
		ReferenceImplementation refimpl = ReferenceImplementation.loadFrom(project);
		return transform(refimpl);
	}
	
	public Xform transform(ReferenceImplementation refimpl) {
		Xform xform = new Xform();
		xform.setNextID("1");
		xform.setId(xform.nextId());
		ExemplarProject exemplarProject[] = refimpl.getExemplarProject();

		Group root = new Group();
		root.setId(xform.nextId());
		root.setParentId("");
		root.setName("root");
		root.setRoot(true);

		Group group = new Group();
		group.setId(xform.nextId());
		group.setParentId(root.getId());
		group.setName("thing");
		group.setRoot(false);
		xform.addGroup(group);
		xform.addGroup(root);
		
		for (int p = 0; p < exemplarProject.length; p++) {
			
			CreateProject cp = new CreateProject();
			cp.setId(xform.nextId());
			String projectId = cp.getId();
			cp.setOLocation(exemplarProject[p].getLocation());
			cp.setLocationExpr(exemplarProject[p].getLocation());
			cp.setOPath(exemplarProject[p].getName());
			cp.setProjectExpr(exemplarProject[p].getName());
			group.addCreateProject(cp);
			
			ExemplarFile exemplarFile[] = exemplarProject[p].getExemplarFile();
			for (int f = 0; f < exemplarFile.length; f++) {
				CreateFile cf = new CreateFile();
				cf.setId(xform.nextId());
				cf.setProjectId(projectId);
			
				String path = exemplarFile[f].getPath();
				path = path.replace('\\','/');
				int last = path.lastIndexOf('/');
				path = exemplarFile[f].getPath();
				String pn = exemplarProject[p].getName();
				String fn = "";
				String mn = "";
				if (last == -1) {
					mn = "";
					fn = path;
				} else {
					mn = path.substring(0,last);
					fn = path.substring(last+1);
					if (!mn.startsWith("/")) {
						mn = "/" + mn;
					}
				}
				cf.setOPath(path);
				cf.setNameExpr(fn);
				cf.setFolderExpr(mn);
				cf.setProjectExpr(pn);
				cf.setSrc(refimpl.getRefImplProjectName()+refimpl.getPathSeparator()+refimpl.getRefImplFolder()+refimpl.getPathSeparator()+exemplarFile[f].getEntry());
				cf.setBinary(false);
				cf.setReplace(true);
				cf.setChangeableName(true);
				cf.setPurposeAsTokenName(false);
				group.addCreateFile(cf);
			}
			
			ExemplarFolder exemplarFolder[] = exemplarProject[p].getExemplarFolder();
			for (int f = 0; f < exemplarFolder.length; f++) {
				CreateFolder cf = new CreateFolder();
				cf.setId(xform.nextId());
				cf.setProjectId(projectId);
			
				String path = exemplarFolder[f].getPath();
				if ((path.length() > 0) && (!path.startsWith("/"))) {
					path = "/" + path;
				}
				cf.setOPath(path);
				cf.setFolderExpr(path);
				cf.setProjectExpr(exemplarProject[p].getName());
				group.addCreateFolder(cf);
			}

		}
		return xform;
	}
}
