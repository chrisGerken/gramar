package org.gramar;

import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoSuchFileStoreException;
import org.gramar.exception.NoSuchGramarException;
import org.gramar.exception.NoSuchTemplatingExtensionException;

/**
 * Represents a means by which gramars and extensions can be located and retrieved
 * from the execution environment
 * 
 * @author chrisgerken
 *
 */
public interface IPluginSource {

	/**
	 * Return an array of String id's for the available IGramars.  Some implementations
	 * may not be able to answer this request.  In these case, callers must know the 
	 * desired patternId.
	 */
	public String[] list();

	/**
	 * Return an array of available IPatterns.
	 */
	public IGramar[] gramars();
	
	/**
	 * Return an IGramar with the given id
	 * 
	 * A gramar is a collection of text templates and a property file.  It can be persisted 
	 * as a zip file named <gramerid>.zip or as a set of individual files in and under a
	 * given subdirectory
	 */
	public IGramar getGramar(String gramarId) throws NoSuchGramarException, InvalidGramarException;
	
	/**
	 * Return the templating extension with the given id
	 */
	public ITemplatingExtension getTemplatingExtension(String extensionId) throws NoSuchTemplatingExtensionException, InvalidTemplateExtensionException;

	/**
	 * Return the file store with the given id
	 */
	public IFileStore getFileStore(String fileStoreId) throws NoSuchFileStoreException;
	
	/**
	 * Tells the receiver that a pattern with the specified ID exists and is retrievable.  This is used
	 * for gramars deployed with the GramarPlatform and which are to be retrieved via a PluginSource that
	 * can not query for available gramars
	 * 
	 * @param id
	 * @throws InvalidGramarException 
	 * @throws NoSuchGramarException 
	 */
	public void addKnownGramar(String id) throws NoSuchGramarException, InvalidGramarException;
		
}
