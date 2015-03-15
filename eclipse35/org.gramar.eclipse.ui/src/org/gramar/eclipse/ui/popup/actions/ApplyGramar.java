package org.gramar.eclipse.ui.popup.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.gramar.IGramarApplicationStatus;
import org.gramar.IModel;
import org.gramar.eclipse.platform.EclipseFileStore;
import org.gramar.eclipse.platform.EclipsePlatform;
import org.gramar.exception.GramarException;

public class ApplyGramar implements IObjectActionDelegate {

	private Shell shell;
	private IFile target;
	
	/**
	 * Constructor for Action1.
	 */
	public ApplyGramar() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {

		String msg = "";
		if (target == null) {
			msg = "No file selected";
		} else {
			try {
				InputStream is = target.getContents();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String gramarId = br.readLine();
				String modelPath = br.readLine();
				br.close();
				
				EclipsePlatform platform = new EclipsePlatform();
				EclipseFileStore fileStore = (EclipseFileStore) platform.getDefaultFileStore();
				IModel model = fileStore.modelFrom(modelPath);
				IGramarApplicationStatus result = platform.apply(model, gramarId, fileStore);
				
				msg = "Apply Gramar was executed.  Model accessed "+result.getModelAccesses()+" times";  
			} catch (Exception e) {
				msg = e.getMessage();
			}
		}
		

		MessageDialog.openInformation(shell, "Ui", msg);
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		StructuredSelection sel = (StructuredSelection) selection;
		target = (IFile) sel.getFirstElement();
		
	}

}
