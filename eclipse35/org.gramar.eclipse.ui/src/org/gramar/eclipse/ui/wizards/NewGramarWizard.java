package org.gramar.eclipse.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.gramar.eclipse.platform.EclipsePlatform;

/**
 * Wizard to create a new gramar
 */

public class NewGramarWizard extends Wizard implements INewWizard {
	private NewGramarWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for NewGramarWizard.
	 */
	public NewGramarWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new NewGramarWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	public boolean performFinish() {
		final String gramarId 		= page.getGramarId();
		final String gramarLabel 	= page.getGramarLabel();
		final String gramarProvider = page.getGramarProvider();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(gramarId, gramarLabel, gramarProvider, monitor);
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
	
	/**
	 * The worker method. It will build the model and apply the Create Gramar gramar.
	 * @param gramarProvider 
	 */

	private void doFinish(
		String gramarId,
		String gramarLabel,
		String gramarProvider, 
		IProgressMonitor monitor)
		throws CoreException {
		
		// create a sample file
		monitor.beginTask("Creating " + gramarId, 1);

		String modelString = "<gramar  id=\""+gramarId+"\"  label=\""+gramarLabel+"\"  provider=\""+gramarProvider+"\"   />";
		try {
			EclipsePlatform.apply(modelString, "org.gramar.basic.gramar");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		monitor.worked(1);
	}

	/**
	 * We will accept the selection in the workbench to see if
	 * we can initialize from it.
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}