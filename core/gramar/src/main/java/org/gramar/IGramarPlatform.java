package org.gramar;

import java.util.List;

import org.gramar.exception.GramarException;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoSuchFileStoreException;
import org.gramar.exception.NoSuchGramarException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.gramar.GramarScore;

/*
 * An IPatternPlatform manages an ordered set of IPatternSources and has helper methods
 * to apply IPatterns to models in various forms. 
 * 
 * An IPatternPlatform will have a default IFileStore, but that can be overridden on a
 * case by case basis when an IPattern is applied to an IModel.
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

	/*
	 * Caches the given extension for later use
	 */
	public void addTemplatingExtension(ITemplatingExtension extension);

	/*
	 * If not already cached, searches the known plugin sources for the extension, caches it, then returns it.
	 */
	public ITemplatingExtension getTemplatingExtension(String extensionId) throws NoSuchTemplatingExtensionException, InvalidTemplateExtensionException;
	
	public IGramarApplicationStatus apply(IModel model, IGramar pattern) throws GramarException;
	
	public IGramarApplicationStatus apply(IModel model, String patternId) throws GramarException;
	
	public IGramarApplicationStatus apply(IModel model, IGramar pattern, IFileStore fileStore) throws GramarException;
	
	public IGramarApplicationStatus apply(IModel model, String patternId, IFileStore fileStore) throws GramarException;
	
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

}
