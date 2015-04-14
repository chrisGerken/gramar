package org.gramar.eclipse.ui.popup.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.gramar.IGramar;
import org.gramar.IGramarApplicationStatus;
import org.gramar.IModel;
import org.gramar.eclipse.platform.EclipseFileStore;
import org.gramar.eclipse.platform.EclipsePlatform;
import org.gramar.eclipse.ui.dialog.ChooseGramarDialog;
import org.gramar.exception.GramarException;
import org.gramar.gramar.GramarScore;
import org.gramar.model.XmlModel;

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

		String msg = null;
		if (target == null) {
			msg = "No file selected";
		} else {
			try {

				InputStream is = target.getContents();
				IModel proposedModel = new XmlModel(is);
				try { is.close(); } catch (Throwable t) {  }

				EclipsePlatform platform = new EclipsePlatform();
				GramarScore[] scored = platform.scoreKnownGramars(proposedModel);
				if (scored.length == 0) {
					msg = "No gramars were found.";  
				} else {
					ChooseGramarDialog cgd = new ChooseGramarDialog(shell, scored);
					cgd.setBlockOnOpen(true);
					int returnCode = cgd.open();
					if (returnCode == Window.OK) {

						IGramar gramar = cgd.getSelected();
						
						EclipseFileStore fileStore = (EclipseFileStore) platform.getDefaultFileStore();
						String path = target.getFullPath().toString();
						IGramarApplicationStatus result = platform.apply(proposedModel, gramar, fileStore);
						
						msg = "Apply Gramar was executed.  Model accessed "+result.getModelAccesses()+" times";  

					}

				}
				
			} catch (Exception e) {
				msg = e.getMessage();
			}
		}
		
		if (msg != null) {
			MessageDialog.openInformation(shell, "Ui", msg);
		}

	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		StructuredSelection sel = (StructuredSelection) selection;
		target = (IFile) sel.getFirstElement();
		
	}

}
