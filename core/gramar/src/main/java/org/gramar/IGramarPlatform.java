package org.gramar;

import org.gramar.exception.GramarException;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoFileStoreSpecifiedException;
import org.gramar.exception.NoSuchGramarException;
import org.gramar.exception.NoSuchTemplatingExtensionException;

/*
 * An IPatternPlatform manages an ordered set of IPatternSources and has helper methods
 * to apply IPatterns to models in various forms. 
 * 
 * An IPatternPlatform will have a default IFileStore, but that can be overridden on a
 * case by case basis when an IPattern is applied to an IModel.
 */
public interface IGramarPlatform {
	
	public void addPluginSource(IPluginSource source);
	
	public IGramar getPattern(String patternId) throws NoSuchGramarException, InvalidGramarException;
	
	public IFileStore getDefaultFileStore();
	
	public void setDefaultFileStore(IFileStore fileStore);

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

}
