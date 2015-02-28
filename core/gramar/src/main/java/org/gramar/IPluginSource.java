package org.gramar;

import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoSuchFileStoreException;
import org.gramar.exception.NoSuchGramarException;
import org.gramar.exception.NoSuchTemplatingExtensionException;

/*
 * An object that can list its available IPatterns and return an IPattern by name
 */
public interface IPluginSource {

	/*
	 * Return an array of String id's for the available IPatterns.  Some implementations
	 * may not be able to answer this request.  In these case, callers must know the 
	 * desired patternId.
	 */
	public String[] list();

	/*
	 * Return an array of available IPatterns.
	 */
	public IGramar[] patterns();
	
	/*
	 * Return an IPattern with the given id
	 * 
	 * A pattern is a collection of text templates and a property file.  It can be persisted 
	 * as a zip file named <patternid>.zip or as a set of individual files in and under a
	 * given subdirectory
	 */
	public IGramar getPattern(String patternId) throws NoSuchGramarException, InvalidGramarException;
	
	/*
	 * Return the templating extension with the given id
	 */
	public ITemplatingExtension getTemplatingExtension(String extensionId) throws NoSuchTemplatingExtensionException, InvalidTemplateExtensionException;

	/*
	 * Return the file store with the given id
	 */
	public IFileStore getFileStore(String fileStoreId) throws NoSuchFileStoreException;
	
}
