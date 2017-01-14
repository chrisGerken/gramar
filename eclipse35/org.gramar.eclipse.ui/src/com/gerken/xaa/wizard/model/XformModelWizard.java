package com.gerken.xaa.wizard.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.gerken.xaa.adaptor.EclipseAdaptor;
import com.gerken.xaa.constants.Constants;
import com.gerken.xaa.model.prepare.Converter;
import com.gerken.xaa.model.refimpl.ReferenceImplementation;
import com.gerken.xaa.model.xform.Xform;
import com.gerken.xaa.nature.XformNature;
import com.gerken.xaa.persist.RefImplPersist;
import com.gerken.xaa.persist.Resourcer;
import com.gerken.xaa.sme.IXaaMentor;
import com.gerken.xaa.sme.IXaaMentorWizardPage;
import com.gerken.xaa.sme.MentorHerder;
import com.gerken.xaa.wizard.refimpl.EclipseExemplarWizardPage;

public class XformModelWizard extends Wizard implements INewWizard {

	private RefImplChoicelWizardPage refImplPage;
	private MentorChoicesWizardPage  mentorsPage;
	private ISelection 				selection;

	private IXaaMentor[] 			mentor;
	private IXaaMentorWizardPage[] 	mentorPage;
	private ArrayList<IWizardPage>	allPages;
	
	private Xform					xform;

	/**
	 * Constructor for EclipseExemplarWizard.
	 */
	public XformModelWizard() {
		super();
		setNeedsProgressMonitor(true);
		mentor = MentorHerder.getInstance().getMentors();
		
		ArrayList<IXaaMentorWizardPage> mp = new ArrayList<IXaaMentorWizardPage>();
		for (int i = 0; i < mentor.length; i++) {
			if (mentor[i].hasWizardPage()) {
				mp.add(mentor[i].getWizardPage());
			}
		}
		mentorPage = new IXaaMentorWizardPage[mp.size()];
		mp.toArray(mentorPage);
		allPages = new ArrayList<IWizardPage>();
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		refImplPage = new RefImplChoicelWizardPage(this);
		addPage(refImplPage);
		allPages.add(refImplPage);
		
		mentorsPage = new MentorChoicesWizardPage(this);
		addPage(mentorsPage);
		allPages.add(mentorsPage);
	}

	public boolean performFinish() {
		final IXaaMentor[] xm = mentor;
		final String projectName  = refImplPage.getProjectName();
		final Xform model = xform;
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(mentor,model,projectName, monitor);
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
		IXaaMentor[] xm,
		Xform model,
		String projectName,
		IProgressMonitor monitor)
		throws CoreException {

		// create the project
		monitor.beginTask("Building Xform Model ", xm.length+1);
		
		model.setXformId(projectName);
		
		for (int m = 0; m < xm.length; m++) {
			xm[m].tweak(model);
		}

		IProject target = Resourcer.createProject(projectName,Constants.XformNatureID, monitor);

		IFile xaa = target.getFile("xform.xform"); 
		xaa.create(new ByteArrayInputStream(model.xmlRepresentation().getBytes()), true, monitor);
		
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "com.gerken.xaa.wizard", IStatus.OK, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

	public void useExemplar(IProject selectedExemplar) {
		xform = Converter.getInstance().transform(selectedExemplar);
		for (int i = 0; i < mentor.length; i++) {
			mentor[i].setEnabled(mentor[i].applicable(xform));
		}
		mentorsPage.updateSelections();
		for (int i = 0; i < mentorPage.length; i++) {
			mentorPage[i].prepareUsing(xform);
			addPage(mentorPage[i]);
			allPages.add(mentorPage[i]);
		}
	}

	public IXaaMentor[] mentors() {
		return mentor;
	}
	
		// Wizard class overrides
	
    public void createPageControlsX(Composite pageContainer) {
        // just do the first two pages for now
    	refImplPage.createControl(pageContainer);
    	mentorsPage.createControl(pageContainer);
    }

    public IWizardPage getNextPage(IWizardPage page) {
        int index = allPages.indexOf(page);
        if (index ==0 ) {
        	return allPages.get(1);
        }
        if (index == -1) {
            return null;
		}
        return enabledPageAfter(index);
    }

    private IWizardPage enabledPageAfter(int index) {
    	for (int i = index+1; i < allPages.size(); i++) {
    		IXaaMentorWizardPage page = (IXaaMentorWizardPage) allPages.get(i);
    		if (page.getMentor().isEnabled()) {
    			return page;
    		}
    	}
		return null;
	}

	public IWizardPage getPreviousPage(IWizardPage page) {
        int index = allPages.indexOf(page);
        if (index < 2 || index == -1) {
            return null;
		} 
		return enabledPageBefore(index);
    }

	private IWizardPage enabledPageBefore(int index) {
    	for (int i = index-1; i > 1; i--) {
    		IXaaMentorWizardPage page = (IXaaMentorWizardPage) allPages.get(i);
    		if (page.getMentor().isEnabled()) {
    			return page;
    		}
    	}
		return mentorsPage;
	}
   
}