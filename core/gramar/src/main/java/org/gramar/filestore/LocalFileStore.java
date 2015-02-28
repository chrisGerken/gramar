package org.gramar.filestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.platform.Util;


/**
 * An implementation of IFileStore that stores files on the local file system.
 * 
 * An absolute directory path is specified on the constructor and all projects are assumed
 * to be subdirectories of that directory
 * 
 * @author chrisgerken
 *
 */
public class LocalFileStore implements IFileStore {

	private String rootDir;
	
	public LocalFileStore(String rootDir) {
		this.rootDir = rootDir;
	}

	@Override
	public InputStream getFileContent(String path) throws NoSuchResourceException {
		try {
			return new FileInputStream(absolutePathFor(path));
		} catch (FileNotFoundException e) {
			throw new NoSuchResourceException(e);
		}
	}

	@Override
	public void setFileContent(String path, InputStream is) throws NoSuchResourceException {
		try {
			FileOutputStream fos = new FileOutputStream(absolutePathFor(path));
			Util.copy(is, fos);
			is.close();
			fos.close();
		} catch (Exception e) {
			throw new NoSuchResourceException(e);
		}
	}

	@Override
	public boolean resourceExists(String path) {
		return new File(absolutePathFor(path)).exists();
	}

	@Override
	public void createProject(String projectName, String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createFolder(String projectName, String pathName)
			throws NoSuchResourceException {
		// TODO Auto-generated method stub

	}

	private String absolutePathFor(String resourcePath) {
		if (rootDir.endsWith("/") && resourcePath.startsWith("/")) {
			return rootDir + resourcePath.substring(1);
		} else if (!rootDir.endsWith("/") && !resourcePath.startsWith("/")) {
			return rootDir + "/" + resourcePath.substring(1);
		}  
		return rootDir + resourcePath.substring(1);
	}

	@Override
	public void commit(String comment) {
		// Does nothing as there is no penalty for writing files 
		// one at a time to the local file system
	}
}
