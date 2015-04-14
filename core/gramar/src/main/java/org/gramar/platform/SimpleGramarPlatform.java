package org.gramar.platform;

import java.util.Properties;

import org.gramar.IFileStore;
import org.gramar.IGramarPlatform;
import org.gramar.exception.GramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoSuchFileStoreException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.plugin.ClasspathPluginSource;
import org.gramar.plugin.FileSystemPluginSource;
import org.gramar.util.PropertiesHelper;


public class SimpleGramarPlatform extends GramarPlatform implements IGramarPlatform {
	
	/**
	 * Simple constructor.  
	 */
	public SimpleGramarPlatform() {
		super();
	}

	/**
	 * Constructor that loads additional extensions as specified in a property file
	 * 
	 * For example, adding a property "plugin.directory.1=common/gramars" in the property file
	 * would result in a FileSystemPluginSource being added for directory common/gramars.
	 * 
	 * @param pm
	 * @throws GramarException 
	 */
	public SimpleGramarPlatform(Properties props) throws GramarException {
		super();
		loadExtensions(props);
	}
	
	@Override
	protected void loadExtensions() {
		try {
			addPluginSource(new ClasspathPluginSource());
			getTemplatingExtension("org.gramar.base");
		} catch (GramarException e) {

		}
		
	}
	
	private void loadExtensions(Properties props) throws GramarException {
		
		for (String directory: PropertiesHelper.getIndexedValues(props, "plugin.directory.")) {
			addPluginSource(new FileSystemPluginSource(directory));
		}
		
		try {
			IFileStore store = getFileStore(props.getProperty("filestore.id", "org.gramar.filestore.LocalFileStore"));
			store.configure(props);
			setDefaultFileStore(store);
		} catch (NoSuchFileStoreException e) {
			throw new GramarException("FileStore not correctly configured", e);
		} 
		
	}

}
