package com.gerken.xaa.wizard.refimpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.gerken.xaa.adaptor.ProjectInterchangeAdaptor;
import com.gerken.xaa.adaptor.ProjectInterchangeResource;
import com.gerken.xaa.model.refimpl.ReferenceImplementation;
import com.gerken.xaa.persist.RefImplPersist;

public class ProjectInterchangeExemplarWizard extends Wizard implements INewWizard {
	private ProjectInterchangeExemplarWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for EclipseExemplarWizard.
	 */
	public ProjectInterchangeExemplarWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new ProjectInterchangeExemplarWizardPage(selection);
		addPage(page);
	}

	public boolean performFinish() {
		final String exemplarID = page.getExemplarID();
		final String path[]  = page.getSelected();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(exemplarID,path, monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}
	
	private void doFinish(
		String exemplarID,
		String path[],
		IProgressMonitor monitor)
		throws CoreException {

		// create the project
		monitor.beginTask("Capturing exemplar " + exemplarID, path.length+1);
		
		ReferenceImplementation refimpl = new ReferenceImplementation();
		refimpl.setExemplarID(exemplarID);

		ProjectInterchangeAdaptor adaptor = new ProjectInterchangeAdaptor(path[0]);
		ProjectInterchangeResource[] project = adaptor.getProjects();
		
		RefImplPersist refImplPersist = new RefImplPersist(refimpl);
		try {
			refImplPersist.populate(adaptor,project, monitor);
		} catch (IOException e) {
			throwCoreException(e.getMessage());
		}
		refImplPersist.writeRefimpl(monitor);
			
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "com.gerken.xaa.wizard", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}