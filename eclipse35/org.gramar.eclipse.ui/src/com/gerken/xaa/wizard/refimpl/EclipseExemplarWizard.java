package com.gerken.xaa.wizard.refimpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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

import com.gerken.xaa.adaptor.EclipseAdaptor;
import com.gerken.xaa.model.refimpl.ReferenceImplementation;
import com.gerken.xaa.persist.RefImplPersist;

public class EclipseExemplarWizard extends Wizard implements INewWizard {
	private EclipseExemplarWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for EclipseExemplarWizard.
	 */
	public EclipseExemplarWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new EclipseExemplarWizardPage(selection);
		addPage(page);
	}

	public boolean performFinish() {
		final String exemplarID = page.getExemplarID();
		final String project[]  = page.getSelected();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(exemplarID,project, monitor);
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
		String project[],
		IProgressMonitor monitor)
		throws CoreException {

		// create the project
		monitor.beginTask("Capturing exemplar " + exemplarID, project.length+1);
		
		ReferenceImplementation refimpl = new ReferenceImplementation();
		refimpl.setExemplarID(exemplarID);

		IProject prj[] = new IProject[project.length];
		for (int p = 0; p < project.length; p++) {
			prj[p] = ResourcesPlugin.getWorkspace().getRoot().getProject(project[p]);
		}
		
		RefImplPersist refImplPersist = new RefImplPersist(refimpl);
		try {
			refImplPersist.populate(new EclipseAdaptor(),prj, monitor);
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