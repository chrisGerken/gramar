package org.gramar;

import java.io.InputStream;
import java.util.List;

import org.gramar.exception.NoSuchResourceException;
import org.gramar.filestore.UpdateResource;


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
	
	/*
	 * Answers a boolean indicating that the resource at the given path exists (true) or
	 * that it does not (false)
	 */
	public boolean resourceExists(String path);
	
	/*
	 * Create a target project with the given name.  If non null or blank, the 
	 * altPath represents a store-dependent alternate or relative location to be used
	 * in the creation of the project
	 */
	public void createProject(String projectName, String altPath);
	
	public void createFolder(String pathName) throws NoSuchResourceException;
	
	/*
	 * Initialize to prepare for gramar application
	 */
	public void reset();
	
	/*
	 * Commit to the file store all pending resource changes
	 */
	public void commit(String comment, IGramarContext context);
	
	/*
	 * Add an update request to the set of changes that need to be made as a result of
	 * applying this gramar
	 */
	public void addUpdate(UpdateResource update);

}
