package org.gramar;

import java.io.InputStream;

import org.gramar.exception.NoSuchResourceException;
import org.gramar.filestore.MergeStream;


/**
 * Represents a method to store and retrieve files.
 * 
 * @author chrisgerken
 *
 */
public interface IFileStore {

	/*
	 * Retrieve the content of the specified file.  The path is assumed to start at
	 * a project within the file store. 
	 */
	public InputStream getFileContent(String path) throws NoSuchResourceException;
	
	/*
	 * Store the content of the specified file.  The path is assumed to start at
	 * a project within the file store. 
	 */
	public void setFileContent(String path, InputStream is) throws NoSuchResourceException;
	
	public boolean resourceExists(String path);
	
	/*
	 * Create a target project with the given name.  If non null or blank, the 
	 * path represents a store-dependent alternate or relative location to be used
	 * in the creation of the project
	 */
	public void createProject(String projectName, String path);
	
	public void createFolder(String projectName, String pathName) throws NoSuchResourceException;
	
	/*
	 * Commit to the file store all pending resource changes
	 */
	public void commit(String comment);
	
}
