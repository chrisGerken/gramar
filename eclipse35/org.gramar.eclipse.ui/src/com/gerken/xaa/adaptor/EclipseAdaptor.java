package com.gerken.xaa.adaptor;

import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

import com.gerken.xaa.model.refimpl.ExemplarProject;

public class EclipseAdaptor implements IExemplarAdaptor {

	public EclipseAdaptor() {
	}

	public String name(Object object) {
		if (object instanceof IProject) {
			return ((IProject)object).getName();
		} else if (object instanceof IFolder) {
			return ((IFolder)object).getName();
		} else if (object instanceof IFile) {
			return ((IFile)object).getName();
		}
		return null;
	}

	public Object[] filesIn(Object object) throws CoreException {
		return membersIn(object,IResource.FILE);
	}

	public Object[] foldersIn(Object object) throws CoreException {
		return membersIn(object,IResource.FOLDER);
	}

	private Object[] membersIn(Object object, int type) throws CoreException {
		ArrayList<IResource> members = new ArrayList<IResource>();
		if (object instanceof IContainer) {
			IContainer cont = (IContainer) object;
			IResource member[] = cont.members();
			for (int m = 0; m < member.length; m++) {
				if (member[m].getType() == type) {
					members.add(member[m]);
				}
			}
		}
		Object[] result = new Object[members.size()];
		members.toArray(result);
		return result;
	}

	public InputStream inputStream(Object object) throws CoreException {
		if (object instanceof IFile) {
			return ((IFile)object).getContents();
		}
		return null;
	}

	public String relativePath(ExemplarProject exemplarProject, Object source) {
		return ((IResource)source).getProjectRelativePath().toString();
	}

	public String location(Object object) {
		return "";
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
