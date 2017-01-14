package com.gerken.xaa.adaptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.core.runtime.CoreException;

import com.gerken.xaa.model.refimpl.ExemplarProject;

public class ProjectInterchangeAdaptor implements IExemplarAdaptor {

	private String zipFilePath;
	private ZipFile zipFile;
	
	public ProjectInterchangeAdaptor(String zipFilePath) {
		this.zipFilePath = zipFilePath;
	}

	public Object[] filesIn(Object object) throws CoreException {
		ProjectInterchangeResource pir = (ProjectInterchangeResource) object;
		return pir.getKidArray(true, false);
	}

	public Object[] foldersIn(Object object) throws CoreException {
		ProjectInterchangeResource pir = (ProjectInterchangeResource) object;
		return pir.getKidArray(false, true);
	}

	public InputStream inputStream(Object object) throws CoreException {
		ProjectInterchangeResource pir = (ProjectInterchangeResource) object;
		return pir.getInputStream(zipFile);
	}

	public String name(Object object) {
		ProjectInterchangeResource pir = (ProjectInterchangeResource) object;
		return pir.getName();
	}

	public String relativePath(ExemplarProject exemplarProject, Object source) {
		ProjectInterchangeResource pir = (ProjectInterchangeResource) source;
		return pir.getRelativePath();
	}

	public String location(Object object) {
		return "";
	}
	
	@SuppressWarnings("unchecked")
	public ProjectInterchangeResource[] getProjects() {
		
		ProjectInterchangeResource root = new ProjectInterchangeResource("");
		
		try {
			
			zipFile = new ZipFile(zipFilePath);

			Enumeration mune;
			for (mune = zipFile.entries(); mune.hasMoreElements(); ) {
				
				ZipEntry zipEntry = (ZipEntry) mune.nextElement();
				String replativePath = zipEntry.getName();
				String seg[] = replativePath.split("/");
				ProjectInterchangeResource current = root;
				for (int i = 0; i < seg.length; i++) {
					ProjectInterchangeResource next;
					if (!current.hasKid(seg[i])) { 
						next = new ProjectInterchangeResource(seg[i]);
						current.addKid(next);
					}
					current = current.getKid(seg[i]);
				}
				current.setZipEntry(zipEntry);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		return root.getKidArray();
	}

	@Override
	public void close() {
		if (zipFile != null) {
			try {
				zipFile.close();
			} catch (IOException e) {

			}
		}
	}

}
