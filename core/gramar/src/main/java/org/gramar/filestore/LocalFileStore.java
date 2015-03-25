package org.gramar.filestore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.gramar.IFileStore;
import org.gramar.exception.GramarException;
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
	
	public LocalFileStore() {
		
	}
	
	/**
	 * Convenience constructor
	 * 
	 * @param rootDir
	 */
	public LocalFileStore(String rootDir) {
		this.rootDir = rootDir;
	}

	@Override
	public InputStream getFileByteContent(String path) throws NoSuchResourceException {
		try {
			return new FileInputStream(absolutePathFor(path));
		} catch (FileNotFoundException e) {
			throw new NoSuchResourceException(e);
		}
	}

	@Override
	public void setFileContent(String path, InputStream stream) throws NoSuchResourceException {
		try {
			FileOutputStream fos = new FileOutputStream(absolutePathFor(path));
			GramarHelper.copy(stream, fos);
			fos.close();
		} catch (Exception e) {
			throw new NoSuchResourceException(e);
		}
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
		
	}

	@Override
	public void log(String message) {
		System.out.println(message);
	}

	@Override
	public void configure(Properties properties) throws GramarException {
		super.configure(properties);
		rootDir = (String) properties.getProperty("filestore.local.root");
		if (rootDir == null) {
			throw new GramarException("Missing filestore.local.root property for LocalFileStore configuration");
		}
	}

}
