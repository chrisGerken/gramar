package com.gerken.xaa.persist;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.gramar.eclipse.ui.Activator;

import com.gerken.xaa.adaptor.IExemplarAdaptor;
import com.gerken.xaa.constants.Constants;
import com.gerken.xaa.model.refimpl.ExemplarFile;
import com.gerken.xaa.model.refimpl.ExemplarFolder;
import com.gerken.xaa.model.refimpl.ExemplarProject;
import com.gerken.xaa.model.refimpl.ReferenceImplementation;

public class RefImplPersist {

	private		ReferenceImplementation refimpl;
	private		ByteArrayOutputStream bzos;
	private		ZipOutputStream zos;
	
	public RefImplPersist(ReferenceImplementation refimpl) {
		this.refimpl = refimpl;
	}

	public void populate(IExemplarAdaptor adaptor, Object[] obj, IProgressMonitor monitor) throws IOException, CoreException {
		
		for (int i = 0; i < obj.length; i++) {
			String projectName = adaptor.name(obj[i]);	
			String location = adaptor.location(obj[i]);
			ExemplarProject prj = new ExemplarProject(refimpl,projectName,location);
			prj.setId(refimpl.nextAvailEntryName());
			prj.setSource(obj[i]);
			refimpl.addExemplarProject(prj);
			gatherMembers(obj[i],adaptor,prj); 
			monitor.worked(1);
		}
		
		adaptor.close();
	}
	
	private void gatherMembers(Object source, IExemplarAdaptor adaptor, ExemplarProject exemplarProject) throws IOException, CoreException {

		Object member[] = adaptor.filesIn(source);
		for (int i = 0; i < member.length; i++) {
			
			String entryName = refimpl.nextAvailEntryName();
			String memberName = adaptor.name(member[i]);
			String relativePath = adaptor.relativePath(exemplarProject,member[i]);

			InputStream is = adaptor.inputStream(member[i]);
			byte b[] = new byte[8000];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int read = is.read(b);
			while (read > -1) {
				baos.write(b,0,read);
				read = is.read(b);
			}
			try { is.close(); } catch (Throwable t) {}

			byte content[] = baos.toByteArray();
			ExemplarFile art = new ExemplarFile(exemplarProject,relativePath,entryName,content);
			exemplarProject.addExemplarFile(art);
		
		}

		member = adaptor.foldersIn(source);
		for (int i = 0; i < member.length; i++) {
			String relativePath = adaptor.relativePath(exemplarProject,member[i]);
			ExemplarFolder folder = new ExemplarFolder(exemplarProject, relativePath);
			exemplarProject.addExemplarFolder(folder);
			gatherMembers(member[i],adaptor,exemplarProject);
		}
		
	}

	public void writeRefimpl(IProgressMonitor monitor) throws CoreException {

			// use plugin.getStateLocation
		
		String loc = Activator.getDefault().getStateLocation().toString() + "/temp.zip";
		FileOutputStream bzos = null;
		try { bzos = new FileOutputStream(loc); } catch (FileNotFoundException e1) {}
		
//		bzos = new ByteArrayOutputStream();
		zos = new ZipOutputStream(bzos);
		zos.setMethod(ZipOutputStream.DEFLATED);
		zos.setLevel(9);

		IProject target = Resourcer.createProject(refimpl.getExemplarID(),Constants.RefImplNatureID,monitor);
		refimpl.setRefImplProjectName(target.getName());
		refimpl.setRefImplFolder("refimpl");
		refimpl.setPathSeparator(String.valueOf(IPath.SEPARATOR));
		
		IFolder refimplDir = target.getFolder(refimpl.getRefImplFolder());
		refimplDir.create(true,true,monitor);
		
		monitor.worked(1);

		ExemplarProject project[] = refimpl.getExemplarProject();
		for (int p = 0; p < project.length; p++) {
			ExemplarFile file[] = project[p].getExemplarFile();
			for (int f = 0; f < file.length; f++) {
				ExemplarFile ef = file[f];
				IFile ifile = refimplDir.getFile(ef.getEntry()); 
				ifile.create(new ByteArrayInputStream(ef.getContent()), true, monitor);

				ZipEntry ze = new ZipEntry(ef.getEntry());
				try {
					zos.putNextEntry(ze);
					zos.write(ef.getContent());
					zos.closeEntry();
				} catch (IOException e) {}
			}
		}
		
		byte content[] = refimpl.xmlRepresentation().getBytes();
		IFile manifest = target.getFile("manifest.xml"); 
		manifest.create(new ByteArrayInputStream(content), true, monitor);

		ZipEntry ze = new ZipEntry("manifest.xml");
		try {
			zos.putNextEntry(ze);
			zos.write(content);
			zos.closeEntry();
		} catch (IOException e) {}

		ze = new ZipEntry("empty");
		try {
			zos.putNextEntry(ze);
			zos.write(" ".getBytes());
			zos.closeEntry();
		} catch (IOException e) {}
		IFile ifile = refimplDir.getFile("empty"); 
		ifile.create(new ByteArrayInputStream(" ".getBytes()), true, monitor);
				
		
		try { zos.close(); } catch (Throwable t) {}
		try { bzos.close(); } catch (Throwable t) {}
		
		IFile monolith = target.getFile(refimpl.getExemplarID()+".monolith");
		
		FileInputStream fis = null;;
		
		try { fis = new FileInputStream(loc); } catch (FileNotFoundException e) {}
		monolith.create(fis, true, monitor);
		try { fis.close(); } catch (Throwable t) {}
		
		monitor.worked(1);
	
	}
}
