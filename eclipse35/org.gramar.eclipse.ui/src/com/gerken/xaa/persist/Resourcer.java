package com.gerken.xaa.persist;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class Resourcer {

	public Resourcer() {
	}

	public static IProject createProject(String name, String natureID, IProgressMonitor monitor) throws CoreException {
		IProjectDescription desc;
		
		IProject target = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		target.create(monitor);
		target.open(monitor);
		desc = target.getDescription();
		String nature[] = new String[1];
		nature[0] = natureID;
		desc.setNatureIds(nature);
		target.setDescription(desc,monitor);
		
		return target;
	}

}
