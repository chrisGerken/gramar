package org.gramar.extension;

import org.gramar.ITemplatingExtension;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.util.GramarHelper;
import org.gramar.util.JarLoaders;

public class JarFileTemplatingExtension extends TemplatingExtension implements ITemplatingExtension {

	private String 			jarFilePath;
	
	public JarFileTemplatingExtension(String jarFilePath, String extensionID) throws InvalidTemplateExtensionException {
		super(extensionID);
		this.jarFilePath = jarFilePath;
	}

	@Override
	protected ClassLoader getExtensionClassloader() throws Exception {

		return JarLoaders.loaderFor(jarFilePath);

	}

	@Override
	protected String getConfig() throws Exception {
		return GramarHelper.asString(getExtensionClassloader().getResourceAsStream(META_FILE_NAME));
	}

}
