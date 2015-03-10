package org.gramar.plugin;

import java.io.InputStream;
import java.util.HashMap;

import org.gramar.IFileStore;
import org.gramar.IGramar;
import org.gramar.IPluginSource;
import org.gramar.ITemplatingExtension;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoSuchFileStoreException;
import org.gramar.exception.NoSuchGramarException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.extension.ClasspathTemplatingExtension;
import org.gramar.extension.TemplatingExtension;
import org.gramar.gramar.ClasspathGramar;
import org.gramar.gramar.ClasspathZipGramar;
import org.gramar.gramar.Gramar;

/*
 * An implementation of IPluginSource that searches the classpath for gramars and extensions
 */
public class ClasspathPluginSource extends PluginSource implements IPluginSource {

	public ClasspathPluginSource() {
		super();
		try {
			addKnownGramar("org.gramar.basic.gramar");
//			addKnownGramar("org.gramar.basic.extension");
		} catch (NoSuchGramarException e) {

		} catch (InvalidGramarException e) {

		}
	}

	@Override
	public void gather(HashMap<String, IGramar> map) {
		super.gather(map);
	}

	@Override
	public IGramar getGramar(String patternId) throws NoSuchGramarException, InvalidGramarException {

		// First, look for a zip file in the classpath with the same name as the pattern ID in 
		// the package associated with that id.  For example, the zip file for pattern ID
		// a.b.c would be found at resource /a/b/c/a.b.c.zip
		String pkg = patternId.replace('.', '/');
		String resource = "/" + pkg + "/" + patternId + ".zip";
		InputStream is = ClasspathPluginSource.class.getResourceAsStream(resource);
		if (is != null) {
			try { is.close(); } catch (Throwable t) {  }
			return new ClasspathZipGramar(resource); 
		} 
		
		// Next, look for a file in the classpath with the name "pattern.config" in 
		// the package associated with that id.  For example, the meta file for pattern ID
		// a.b.c would be found at resource /a/b/c/pattern.config
		pkg = patternId.replace('.', '/');
		resource = "/" + pkg + "/" + Gramar.META_FILE_NAME;
		is = ClasspathPluginSource.class.getResourceAsStream(resource);
		if (is != null) {
			try { is.close(); } catch (Throwable t) {  }
			return new ClasspathGramar(patternId);
		} 
		
		throw new NoSuchGramarException();
	}

	@Override
	public ITemplatingExtension getTemplatingExtension(String extensionId)
			throws NoSuchTemplatingExtensionException, InvalidTemplateExtensionException {
		
		// First, look for a config file
		String pkg = extensionId.replace('.', '/');
		String resource = "/" + pkg + "/" + TemplatingExtension.META_FILE_NAME;
		InputStream is = ClasspathPluginSource.class.getResourceAsStream(resource);
		if (is != null) {
			try { is.close(); } catch (Throwable t) {  }
			return new ClasspathTemplatingExtension(extensionId);
		}
		
		throw new NoSuchTemplatingExtensionException();
	}

	@Override
	public IFileStore getFileStore(String fileStoreId)
			throws NoSuchFileStoreException {
		// TODO Auto-generated method stub
		return null;
	}

}
