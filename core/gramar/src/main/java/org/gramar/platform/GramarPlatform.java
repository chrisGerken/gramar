package org.gramar.platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoFileStoreSpecifiedException;
import org.gramar.exception.NoSuchFileStoreException;
import org.gramar.exception.NoSuchGramarException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.gramar.GramarScore;
import org.gramar.resource.MergeStream;


public abstract class GramarPlatform implements IGramarPlatform {

	protected IFileStore 							fileStore;
	protected ArrayList<IPluginSource> 				pluginSources = new ArrayList<IPluginSource>();
	protected HashMap<String,ITemplatingExtension> 	extensions = new HashMap<String,ITemplatingExtension>();

	public GramarPlatform() { 
		loadExtensions();
	}

	@Override
	public IGramar getGramar(String gramarId) throws NoSuchGramarException, InvalidGramarException {
		IGramar gramar;
		for (IPluginSource source : pluginSources) {
			try {
				gramar = source.getGramar(gramarId);
				return gramar;
			} catch (NoSuchGramarException e) {
				// Ignore and continue with the next source
				gramar = null;
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
	public IGramarApplicationStatus apply(IModel model, IGramar gramar) throws GramarException {
		if (fileStore == null) {
			throw new NoFileStoreSpecifiedException();
		}
		return apply(model, gramar, fileStore);
	}

	@Override
	public IGramarApplicationStatus apply(IModel model, String gramarId) throws GramarException {
		if (fileStore == null) {
			throw new NoFileStoreSpecifiedException();
		}
		return apply(model, getGramar(gramarId), fileStore);
	}

	@Override
	public IGramarApplicationStatus apply(IModel model, String gramarId, IFileStore fileStore) throws GramarException {
		return apply(model,getGramar(gramarId),fileStore);
	}

	@Override
	public IGramarApplicationStatus apply(IModel model, IGramar gramar, IFileStore fileStore) throws GramarException {
		String mainId = gramar.getMainTemplateId();
		IGramarContext context = new GramarContext(this, model.asDOM());
		context.setGramar(gramar);
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
		} catch (Exception e) {
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
				extension = null;
			}
		}
		throw new NoSuchTemplatingExtensionException();
	}
	
	/**
	 * Called by the constructor to give the implementer the chance to load
	 * any known extensions, gramars, etc
	 * @throws GramarException 
	 */
	protected abstract void loadExtensions() ;

	@Override
	public IGramar[] getKnownGramars() {
		
		ArrayList<IGramar> list = new ArrayList<IGramar>();
		
		for (IPluginSource pluginSource: pluginSources) {
			IGramar[] gramar = pluginSource.gramars();
			for (IGramar g: gramar) {
				list.add(g);
			}
		}

		IGramar gramar[] = new IGramar[list.size()];
		list.toArray(gramar);
		return gramar;
	}

	@Override
	public IFileStore getFileStore(String fileStoreId) throws NoSuchFileStoreException {
		for (ITemplatingExtension ext: extensions.values()) {
			try {
				return ext.getFileStore(fileStoreId);
			} catch (Exception e) {

			}
		}
		throw new NoSuchFileStoreException();
	}

	@Override
	public GramarScore[] scoreKnownGramars(IModel proposedModel) {
		ArrayList<GramarScore> scores = new ArrayList<GramarScore>();
		IGramar[] gramars = getKnownGramars();
		for (IGramar gramar: gramars) {
			Double score = gramar.scoreApplicability(proposedModel);
			scores.add(new GramarScore(gramar, score));
		}
		GramarScore gramarScore[] = new GramarScore[scores.size()];
		scores.toArray(gramarScore);
		Arrays.sort(gramarScore);
		return gramarScore;
	}

}
