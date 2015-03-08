package org.gramar.eclipse.platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.gramar.IFileStore;
import org.gramar.IModel;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.filestore.FileStore;
import org.gramar.model.XmlModel;
import org.gramar.util.GramarHelper;


public class EclipseFileStore extends FileStore implements IFileStore {

	public EclipseFileStore() {

	}

	@Override
	public void createFolder(String path) throws NoSuchResourceException,
			IOException {

		String segment[] = GramarHelper.pathSegments(path);
		if (segment.length == 0) {
			return;
		}
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(segment[0]);
		if (!project.exists()) {
			throw new NoSuchResourceException("Project "+segment[0]);
		}
		if (segment.length == 1) {
			return;
		}
		IContainer current = project;
		for (int s = 1; s < segment.length; s++) {
			IResource resource = current.findMember(segment[s]);
			if (resource != null) {
				if (resource.getType() == IResource.FOLDER) {
					current = (IContainer) resource;
				} else {
					throw new NoSuchResourceException("Not a folder: "+path);
				}
			} else {
				current = current.getFolder(new Path(segment[s]));
			}
		}
	}

	@Override
	public void createProject(String name, String altPath) throws Exception {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		if (!project.exists()) {
			project.create((IProgressMonitor)null);
		}
		if (!project.isOpen()) {
			project.open((IProgressMonitor)null);
		}
	}

	@Override
	public Reader getFileContent(String path) throws NoSuchResourceException {
		try {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
			if (!file.exists()) {
				return null;
			}
			return new InputStreamReader(file.getContents(true));
		} catch (CoreException e) {
			throw new NoSuchResourceException(e);
		}
	}

	@Override
	public boolean resourceExists(String path) {
		return ResourcesPlugin.getWorkspace().getRoot().exists(new Path(path));
	}

	@Override
	public void setFileContent(String path, Reader reader) throws NoSuchResourceException, IOException {
		try {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
			InputStream is = GramarHelper.asStream(reader);
			if (file.exists()) {
				file.setContents(is, true, true, (IProgressMonitor)null);
			} else {
				file.create(is, true, (IProgressMonitor)null);
			}
		} catch (CoreException e) {
			throw new NoSuchResourceException(e);
		}
	}

	public IModel modelFrom(String modelPath) throws CoreException, Exception {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(modelPath));
		return new XmlModel(file.getContents(true));
	}

}
