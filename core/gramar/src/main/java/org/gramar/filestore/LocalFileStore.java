package org.gramar.filestore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.util.GramarHelper;


/**
 * An implementation of IFileStore that stores files on the local file system.
 * 
 * An absolute directory path is specified on the constructor and all projects are assumed
 * to be subdirectories of that directory
 * 
 * @author chrisgerken
 *
 */
public class LocalFileStore extends FileStore implements IFileStore {

	private String rootDir;
	
	public LocalFileStore(String rootDir) {
		this.rootDir = rootDir;
	}

	@Override
	public Reader getFileContent(String path) throws NoSuchResourceException {
		try {
			return new FileReader(absolutePathFor(path));
		} catch (FileNotFoundException e) {
			throw new NoSuchResourceException(e);
		}
	}

	@Override
	public void setFileContent(String path, Reader reader) throws NoSuchResourceException {
		try {
			FileWriter writer = new FileWriter(absolutePathFor(path));
			GramarHelper.copy(reader, writer);
			writer.close();
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
	public void createFolder(String pathName) throws NoSuchResourceException {
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
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log(String message) {
		System.out.println(message);
	}

}
