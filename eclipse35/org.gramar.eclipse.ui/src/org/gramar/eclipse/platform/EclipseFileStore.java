package org.gramar.eclipse.platform;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.gramar.IFileStore;
import org.gramar.IGramarContext;
import org.gramar.IModel;
import org.gramar.exception.GramarException;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.filestore.FileStore;
import org.gramar.model.XmlModel;
import org.gramar.resource.UpdateResource;
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
				try {
					((IFolder)current).create(true, true, (IProgressMonitor)null);
				} catch (CoreException e) {
					throw new NoSuchResourceException(e);
				}
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
		InputStream stream = getFileByteContent(path);
		if (stream == null) {
			return null;
		}
		return new InputStreamReader(stream);
	}

	@Override
	public InputStream getFileByteContent(String path) throws NoSuchResourceException {
		try {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
			if (!file.exists()) {
				return null;
			}
			return file.getContents(true);
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
		InputStream stream = GramarHelper.asStream(reader);
		setFileContent(path, stream);
	}

	@Override
	public void setFileContent(String path, InputStream stream) throws NoSuchResourceException, IOException {
		try {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));
			if (file.exists()) {
				file.setContents(stream, true, true, (IProgressMonitor)null);
			} else {
				String segment[] = GramarHelper.pathSegments(path);
				if (segment.length > 1) {
					int index = path.lastIndexOf('/');
					String prp = path.substring(0,index);
					createFolder(prp);
				}
				file.create(stream, true, (IProgressMonitor)null);
			}
		} catch (CoreException e) {
			throw new NoSuchResourceException(e);
		}
	}

	public IModel modelFrom(String modelPath) throws CoreException, Exception {
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(modelPath));
		return new XmlModel(file.getContents(true));
	}

	@Override
	public void commit(String comment, IGramarContext context) throws GramarException { 

		try {
			final EclipseFileStore fileStore 	= this;
			final String commitComment 			= comment;
			final IGramarContext commitContext 	= context;
			
			IWorkspaceRunnable batchCommit = new IWorkspaceRunnable() {
				
				@Override
				public void run(IProgressMonitor monitor) throws CoreException {

					fileStore.executeUpdates(commitComment, commitContext);

					UpdateResource update[] = new UpdateResource[updates.size()];
					updates.toArray(update);
					Arrays.sort(update);
					
					for (UpdateResource ru: update) {
						try {
							if (ru.isProject()) {
								ru.execute(fileStore);
								System.out.println(ru.report());
							}
						} catch (Exception e) {
							commitContext.error(e);
						}
					}
					
					ResourcesPlugin.getWorkspace().checkpoint(true);
					
					for (UpdateResource ru: update) {
						try {
							if (!ru.isProject()) {
								ru.execute(fileStore);
								System.out.println(ru.report());
							}
						} catch (Exception e) {
							commitContext.error(e);
						}
					}
					
				}
			};

			ResourcesPlugin.getWorkspace().run(batchCommit, null);

		} catch (CoreException e) {
			throw new GramarException(e);
		}
	}

	@Override
	public void log(String message) {

//		MessageConsole myConsole = new MessageConsole(name, null);		

	}

}
