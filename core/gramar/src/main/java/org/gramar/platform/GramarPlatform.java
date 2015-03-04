package org.gramar.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.gramar.GramarApplicationResult;
import org.gramar.IFileStore;
import org.gramar.IGramar;
import org.gramar.IGramarApplicationStatus;
import org.gramar.IGramarContext;
import org.gramar.IGramarPlatform;
import org.gramar.IModel;
import org.gramar.IPluginSource;
import org.gramar.ITemplate;
import org.gramar.ITemplatingExtension;
import org.gramar.context.GramarContext;
import org.gramar.exception.GramarException;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoFileStoreSpecifiedException;
import org.gramar.exception.NoSuchGramarException;
import org.gramar.exception.NoSuchTemplateException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.resource.MergeStream;


public abstract class GramarPlatform implements IGramarPlatform {

	protected IFileStore 							fileStore;
	protected ArrayList<IPluginSource> 				pluginSources = new ArrayList<IPluginSource>();
	protected HashMap<String,ITemplatingExtension> 	extensions = new HashMap<String,ITemplatingExtension>();

	public GramarPlatform() { 
	}

	@Override
	public IGramar getPattern(String patternId) throws NoSuchGramarException, InvalidGramarException {
		for (IPluginSource source : pluginSources) {
			try {
				IGramar pattern = source.getPattern(patternId);
				return pattern;
			} catch (NoSuchGramarException e) {
				// Ignore and continue with the next source
			}
		}
		throw new NoSuchGramarException();
	}

	@Override
	public IFileStore getDefaultFileStore() {
		return fileStore;
	}

	@Override
	public void setDefaultFileStore(IFileStore fileStore) {
		this.fileStore = fileStore;  
	}

	@Override
	public void addPluginSource(IPluginSource source) {
		pluginSources.add(source);
	}

	@Override
	public IGramarApplicationStatus apply(IModel model, IGramar pattern) throws GramarException {
		if (fileStore == null) {
			throw new NoFileStoreSpecifiedException();
		}
		return apply(model, pattern, fileStore);
	}

	@Override
	public IGramarApplicationStatus apply(IModel model, String patternId) throws GramarException {
		if (fileStore == null) {
			throw new NoFileStoreSpecifiedException();
		}
		return apply(model, getPattern(patternId), fileStore);
	}

	@Override
	public IGramarApplicationStatus apply(IModel model, String patternId, IFileStore fileStore) throws GramarException {
		return apply(model,getPattern(patternId),fileStore);
	}

	@Override
	public IGramarApplicationStatus apply(IModel model, IGramar gramar, IFileStore fileStore) throws GramarException {
		String mainId = gramar.getMainTemplateId();
		IGramarContext context = new GramarContext(this, model.asDOM());
		context.setPattern(gramar);
		context.setFileStore(fileStore);
		ITemplate mainTemplate = gramar.getTemplate(mainId, context);
		return apply(mainTemplate,gramar, context);
	}

	public IGramarApplicationStatus apply(ITemplate mainTemplate, IGramar gramar, IGramarContext context) {
		try {
			context.getFileStore().reset();
			MergeStream stream = new MergeStream();
			mainTemplate.mergeTo(stream,context);
			context.getFileStore().commit("Applied gramar "+gramar.getId(), context);
		} catch (IOException e) {
			context.error(e);
		}
		return new GramarApplicationResult(context);
	}

	@Override
	public void addTemplatingExtension(ITemplatingExtension extension) {
		String abbreviation = extension.getExtensionId();
		extensions.put(abbreviation, extension);		
	}

	@Override
	public ITemplatingExtension getTemplatingExtension(String extensionId) throws NoSuchTemplatingExtensionException, InvalidTemplateExtensionException {
		ITemplatingExtension extension = extensions.get(extensionId);
		if (extension != null) {
			return extension;
		}
		
		for (IPluginSource source: pluginSources) {
			try {
				extension = source.getTemplatingExtension(extensionId);
				addTemplatingExtension(extension);
				return extension;
			} catch (NoSuchTemplatingExtensionException e) {
				// Do nothing.  Keep looking.
			}
		}
		throw new NoSuchTemplatingExtensionException();
	}
	
	protected abstract void loadExtensions();

}
