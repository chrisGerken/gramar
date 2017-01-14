package com.gerken.xaa.adaptor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ProjectInterchangeResource {

	private String name;
	private String relativePath = null;
	private ZipEntry zipEntry;
	private Hashtable<String, ProjectInterchangeResource> kids = null;

	public ProjectInterchangeResource(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public ZipEntry getZipEntry() {
		return zipEntry;
	}

	public void setZipEntry(ZipEntry zipEntry) {
		this.zipEntry = zipEntry;
	}

	private Hashtable<String, ProjectInterchangeResource> getKids() {
		if (kids == null) {
			kids = new Hashtable<String, ProjectInterchangeResource>();
		}
		return kids;
	}
	
	public boolean hasKid(String name) {
		return getKid(name) != null;
	}
	
	public ProjectInterchangeResource getKid(String name) {
		return getKids().get(name);
	}
	
	public void addKid(ProjectInterchangeResource kid) {
		String kidName = kid.getName();
		getKids().put(kidName,kid);
		if (relativePath == null) {
			kid.setRelativePath(kidName);
		} else {
			kid.setRelativePath(relativePath+"/"+kidName);
		}
	}

	public boolean isFile() {
		return kids == null;
	}

	public ProjectInterchangeResource[] getKidArray() {
		return getKidArray(true,true);
	}

	public ProjectInterchangeResource[] getKidArray(boolean files, boolean folders) {
		if (kids == null) { return new ProjectInterchangeResource[0]; }
		
		Collection<ProjectInterchangeResource> values = kids.values();
		
		ArrayList<ProjectInterchangeResource> list = new ArrayList<ProjectInterchangeResource>();
		Iterator<ProjectInterchangeResource> iter = values.iterator();
		while (iter.hasNext()) {
			ProjectInterchangeResource pir = iter.next();
			if ((pir.isFile() && files) || (!pir.isFile() && folders)) {
				list.add(pir);
			}
		}
		
		ProjectInterchangeResource[] result = new ProjectInterchangeResource[list.size()];
		list.toArray(result);
		
		return result;
	}

	public InputStream getInputStream(ZipFile zipFile) {
		if (zipEntry == null) { return null; }
		try {
			return zipFile.getInputStream(zipEntry);
		} catch (IOException e) {
			return new ByteArrayInputStream(new byte[0]);
		}
	}
}
