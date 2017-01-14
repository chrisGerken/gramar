package com.gerken.xaa.wizard.refimpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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

import com.gerken.xaa.adaptor.FileSystemAdaptor;
import com.gerken.xaa.constants.Constants;
import com.gerken.xaa.model.refimpl.ExemplarFile;
import com.gerken.xaa.model.refimpl.ExemplarProject;
import com.gerken.xaa.model.refimpl.ReferenceImplementation;
import com.gerken.xaa.persist.RefImplPersist;
import com.gerken.xaa.persist.Resourcer;

public class MonolithExemplarWizard extends Wizard implements INewWizard {
	private MonolithExemplarWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for EclipseExemplarWizard.
	 */
	public MonolithExemplarWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */

	public void addPages() {
		page = new MonolithExemplarWizardPage(selection);
		addPage(page);
	}

	public boolean performFinish() {
		final String monolithFileName = page.getMonolithFileName();
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monolithFileName, monitor);
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
	
	private void doFinish(String monolithFileName,
			IProgressMonitor monitor)
		throws CoreException {

		try {
			ZipFile zip = new ZipFile(monolithFileName);
			ZipEntry entry = zip.getEntry("manifest.xml");
			InputStream is = zip.getInputStream(entry);
			ReferenceImplementation refImpl = ReferenceImplementation.loadFrom(is);
			ExemplarFile[] ef = refImpl.allFiles();
			String projectName = refImpl.getExemplarID();
			String refImplFolder = refImpl.getRefImplFolder();
			
			monitor.beginTask("Rebuilding reference exemplar project " + projectName, ef.length+4);
			
			monitor.subTask("Create project "+projectName);
			IProject target = Resourcer.createProject(projectName,Constants.RefImplNatureID,monitor);		
			monitor.worked(1);

			
			monitor.subTask("Create refimpl folder");
			IFolder refimplDir = target.getFolder(refImplFolder);
			refimplDir.create(true,true,monitor);
			monitor.worked(1);
			
			for (int f = 0; f < ef.length; f++) {
				monitor.subTask("Writing artifact");
				String id = ef[f].getEntry();
				entry = zip.getEntry(id);
				byte[] content = read(zip,entry);
				IFile ifile = refimplDir.getFile(id); 
				ifile.create(new ByteArrayInputStream(content), true, monitor);
				monitor.worked(1);
			}

			monitor.subTask("Writing manifest");
			entry = zip.getEntry("manifest.xml");
			byte[] content = read(zip,entry);
			IFile ifile = target.getFile("manifest.xml"); 
			ifile.create(new ByteArrayInputStream(content), true, monitor);
			monitor.worked(1);
			
			zip.close();

			monitor.subTask("Writing monolth");
			FileInputStream fis = new FileInputStream(monolithFileName);
			content = from(fis);
			is.close();
			ifile = target.getFile(refImpl.getExemplarID()+".monolith"); 
			ifile.create(new ByteArrayInputStream(content), true, monitor);
			monitor.worked(1);
		} catch (Throwable t) {
			throwCoreException(t.getLocalizedMessage());
		}
	}

	private byte[] from(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[8000];
		int bytesRead = is.read(b);
		while (bytesRead > 0) {
			baos.write(b,0,bytesRead);
			bytesRead = is.read(b);
		}
		return baos.toByteArray();
	}

	private byte[] read(ZipFile zip, ZipEntry entry) throws IOException {
		return from(zip.getInputStream(entry));
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status =
			new Status(IStatus.ERROR, "com.gerken.xaa.wizard", IStatus.ERROR, message, null);
		throw new CoreException(status);
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}