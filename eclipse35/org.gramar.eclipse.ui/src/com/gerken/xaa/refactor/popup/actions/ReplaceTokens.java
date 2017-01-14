package com.gerken.xaa.refactor.popup.actions;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.gramar.eclipse.ui.Activator;

import com.gerken.xaa.model.xform.Replacement;
import com.gerken.xaa.model.xform.Xform;

public class ReplaceTokens implements IObjectActionDelegate {

	private IFile file[];

	private Shell shell;
	
	/**
	 * Constructor for Action1.
	 */
	public ReplaceTokens() {
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
//		MessageDialog.openInformation(shell,"Refactor Plug-in","Replace Known Tokens was executed.");
		
		Hashtable<String,String> ht = buildReplacements();
		
		XaaTemplateRefactoring refactoring = new XaaTemplateRefactoring(file,ht);
		TokenRefactoringWizard wizard = new TokenRefactoringWizard(refactoring,RefactoringWizard.DIALOG_BASED_USER_INTERFACE);
		RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
		try {
			op.run(shell,"Replace Tokens");
		} catch (InterruptedException e) {}
	}

	private Hashtable<String, String> buildReplacements() {
		
		Hashtable<String,String> result = new Hashtable<String, String>();
		
		if (file.length == 0) { return result; }
		
		IProject project = file[0].getProject();
		Xform xform = Activator.getXformAccess().xformFor(project.getName()); 
//		Xform xform = Xform.loadFrom(project);
		Replacement[] rep = xform.getReplacementArray();
		for (int i = 0; i < rep.length; i++) {
			String oldString = rep[i].getOldString();
			String newString = rep[i].getNewString();
			newString = xform.toJetTags(newString);
			result.put(oldString, newString);
		}
		
		return result;
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof StructuredSelection) {
			StructuredSelection sel = (StructuredSelection) selection;
			List<Object> sels = sel.toList();
			ArrayList<IFile> files = new ArrayList<IFile>();
			for (int s = 0; s < sels.size(); s++) {
				Object rsc = sels.get(s);
				if (rsc instanceof IFile) {
					files.add((IFile)rsc);
				}
			}
			file = new IFile[files.size()];
			files.toArray(file);
		}
	}

}
	
