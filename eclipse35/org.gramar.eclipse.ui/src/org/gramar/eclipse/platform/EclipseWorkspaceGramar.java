package org.gramar.eclipse.platform;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.gramar.IGramar;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.gramar.Gramar;
import org.gramar.util.GramarHelper;

/**
 * Represents a Gramar whose templates and other resources exist as independent files
 * in an Eclipse project.  A project is marked to be a Gramar by the presence of
 * a file (contents ignored) named '.gramar' in the project's root directory.  
 * 
 * This class will recursively search the project until the file 'gramar.config' is found
 * and the directory in which that file is found is assumed to be the gramar's 
 * root directory.
 */
public class EclipseWorkspaceGramar extends Gramar implements IGramar {

	private IProject 	project;
	private IPath 		gramarOffset;
	
	public EclipseWorkspaceGramar(IProject project) throws InvalidGramarException {
		super();
		this.project = project;
		findConfig();
		loadMeta();
	}

	@Override
	public String readTemplateSource(String path) throws NoSuchResourceException {
		try {
			return GramarHelper.asString(readTemplateBinary(path));
		} catch (IOException e) {
			throw new NoSuchResourceException(path,e);
		}
	}

	@Override
	public InputStream readTemplateBinary(String path) throws NoSuchResourceException {
		try {
			IPath projectRelativePath = gramarOffset.append(new Path(path));
			IFile file = project.getFile(projectRelativePath);
			if (!file.exists()) {
				throw new NoSuchResourceException(path);
			}
			return file.getContents(true);
		} catch (Exception e) {
			throw new NoSuchResourceException(path,e);
		}
	}

	private void findConfig() throws InvalidGramarException {
		IContainer dir = project;
		try {
			boolean found = findConfig(dir);
			if (!found) {
				throw new InvalidGramarException();
			}
		} catch (CoreException e) {
			throw new InvalidGramarException(e);
		}
	}

	private boolean findConfig(IContainer dir) throws CoreException {
		IResource[] member = dir.members();
		for (IResource rsrc: member) {
			if (rsrc.getType() == IResource.FOLDER) {
				if (findConfig((IContainer)rsrc)) {
					return true;
				}
			}
			if (rsrc.getType() == IResource.FILE) {
				IFile file = (IFile) rsrc;
				if (file.getName().equalsIgnoreCase("gramar.config")) {
					gramarOffset = file.getParent().getProjectRelativePath();
					return true;
				}
			}
		}
		return false;
	}

}
