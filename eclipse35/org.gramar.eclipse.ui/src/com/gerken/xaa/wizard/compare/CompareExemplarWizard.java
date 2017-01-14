package com.gerken.xaa.wizard.compare;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.gramar.eclipse.ui.Activator;

import com.gerken.xaa.model.refimpl.ReferenceImplementation;

public class CompareExemplarWizard extends Wizard implements INewWizard {

	private CompareExemplarlWizardPage choicePage;

	private ArrayList<IWizardPage>	allPages;

	/**
	 * Constructor for EclipseExemplarWizard.
	 */
	public CompareExemplarWizard() {
		super();
		setNeedsProgressMonitor(true);
		allPages = new ArrayList<IWizardPage>();
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		choicePage = new CompareExemplarlWizardPage(this);
		addPage(choicePage);
		allPages.add(choicePage);
	}

	public boolean performFinish() {

		final IProject project1  = choicePage.getSelectedExemplar1();
		final IProject project2  = choicePage.getSelectedExemplar2();

		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(project1, project2, monitor);
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
	
	private void doFinish(IProject project1, IProject project2, IProgressMonitor monitor) throws CoreException {

		monitor.beginTask("Comparing exemplars", 1);
		
		ReferenceImplementation refimpl1 = ReferenceImplementation.loadFrom(project1);
		ReferenceImplementation refimpl2 = ReferenceImplementation.loadFrom(project2);
		
		IStatus report = refimpl1.reportDelta(refimpl2);
		Activator.getDefault().getLog().log(report);
		
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "com.gerken.xaa.wizard", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}
	
		// Wizard class overrides
	
    public void createPageControlsX(Composite pageContainer) {
        // just do the first two pages for now
    	choicePage.createControl(pageContainer);
    }

}