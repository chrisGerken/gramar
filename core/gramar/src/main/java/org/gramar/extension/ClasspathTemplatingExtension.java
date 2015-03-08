package org.gramar.extension;

import java.io.IOException;
import java.io.InputStream;

import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.plugin.ClasspathPluginSource;
import org.gramar.util.GramarHelper;


public class ClasspathTemplatingExtension extends TemplatingExtension {

	public ClasspathTemplatingExtension(String extensionID) throws InvalidTemplateExtensionException {
		super(extensionID);
	}

	@Override
	protected String getConfig() throws IOException {
		String pkg = extensionId.replace('.', '/');
		String resource = "/" + pkg + "/" + TemplatingExtension.META_FILE_NAME;
		InputStream is = ClasspathPluginSource.class.getResourceAsStream(resource);
		return GramarHelper.asString(is);
	}

	@Override
	protected ClassLoader getExtensionClassloader() {
		return ClasspathTemplatingExtension.class.getClassLoader();
	}

}
