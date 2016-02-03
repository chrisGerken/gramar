package org.gramar.eclipse.ui.popup.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
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

public class MigrateJetTransform implements IObjectActionDelegate {

	private Shell shell;
	private IProject target;
	
	/**
	 * Constructor for Action1.
	 */
	public MigrateJetTransform() {
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
			msg = "No project selected";
		} else {
			try {
				
				msg = "JET Transform migrated";  
			} catch (Exception e) {
				msg = e.getMessage();
			}
		}
		

		MessageDialog.openInformation(shell, "UI", msg);
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		StructuredSelection sel = (StructuredSelection) selection;
		target = (IProject) sel.getFirstElement();
		
	}

}
