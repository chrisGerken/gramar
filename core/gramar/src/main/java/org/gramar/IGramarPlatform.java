package org.gramar;

import java.util.List;

import org.gramar.exception.GramarException;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoSuchFileStoreException;
import org.gramar.exception.NoSuchGramarException;
import org.gramar.exception.NoSuchModelAdaptorException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.gramar.GramarScore;

/*
 * An IGramarPlatform manages an ordered set of IGramarSources and has helper methods
 * to apply IGramars to models in various forms. 
 * 
 * An IGramarPlatform will have a default IFileStore, but that can be overridden on a
 * case by case basis when an IGramar is applied to an IModel.
 */
public interface IGramarPlatform {
	
	public void addPluginSource(IPluginSource source);
	
	public IGramar getGramar(String gramarId) throws NoSuchGramarException, InvalidGramarException;
	
	public IFileStore getDefaultFileStore();
	
	public void setDefaultFileStore(IFileStore fileStore);

	/**
	 * Searches all known plugin sources for an extension that provides a filestore with the given
	 * filestore ID.  Ask that extension for an instance of that filestare and return the instance.
	 * 
	 * @param fileStoreId
	 * @return
	 * @throws NoSuchFileStoreException 
	 */
	public IFileStore getFileStore(String fileStoreId) throws NoSuchFileStoreException;

	/**
	 * Caches the given extension for later use
	 */
	public void addTemplatingExtension(ITemplatingExtension extension);

	/**
	 * If not already cached, searches the known plugin sources for the extension, caches it, then returns it.
	 */
	public ITemplatingExtension getTemplatingExtension(String extensionId) throws NoSuchTemplatingExtensionException, InvalidTemplateExtensionException;
	
	public IGramarApplicationStatus apply(IModel model, IGramar gramar) throws GramarException;
	
	public IGramarApplicationStatus apply(IModel model, String gramarId) throws GramarException;
	
	public IGramarApplicationStatus apply(IModel model, IGramar gramar, IFileStore fileStore) throws GramarException;
	
	public IGramarApplicationStatus apply(IModel model, String gramarId, IFileStore fileStore) throws GramarException;
	
	/**
	 * Returns aa array of all Gramars known to any PluginSource
	 * 
	 * @return
	 */
	public IGramar[] getKnownGramars();
	
	/**
	 * @return a sorted array of gramars with associated scores.  GramarScores are returned with the best match
	 * first and the worst match last.
	 */
	public GramarScore[] scoreKnownGramars(IModel proposedModel);
	
	/**
	 * Returns the IModelAdaptor implementation for the given adaptor ID.  If no such model adaptor
	 * is supported by this extension then throw an exception
	 * 
	 * @param adaptorID
	 * @return an IModelAdaptor implementation
	 * @throws NoSuchModelAdaptorException
	 */
	public IModelAdaptor getModelAdaptor(String adaptorID) throws NoSuchModelAdaptorException;
	
	/**
	 * Returns the IModelAdaptor implementation appropriate for convering the specified source type.  
	 * If no such model adaptor is supported by this extension then throw an exception
	 * 
	 * @param model source type (e.g. "xml" or "csv")
	 * @return an IModelAdaptor implementation
	 * @throws NoSuchModelAdaptorException
	 */
	public IModelAdaptor getModelAdaptorFor(String type) throws NoSuchModelAdaptorException;

}
