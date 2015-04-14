package org.gramar.eclipse.platform;

import java.io.InputStream;
import java.util.HashMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.gramar.IFileStore;
import org.gramar.IGramar;
import org.gramar.IPluginSource;
import org.gramar.ITemplatingExtension;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoSuchFileStoreException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.extension.TemplatingExtension;
import org.gramar.plugin.ClasspathPluginSource;
import org.gramar.plugin.PluginSource;

public class EclipseWorkspacePluginSource extends PluginSource implements
		IPluginSource {

	public EclipseWorkspacePluginSource() {

	}

	@Override
	public ITemplatingExtension getTemplatingExtension(String extensionId) throws NoSuchTemplatingExtensionException, InvalidTemplateExtensionException {
		
		// First, look for a config file
		String pkg = extensionId.replace('.', '/');
		String resource = "/" + pkg + "/" + TemplatingExtension.META_FILE_NAME;
		InputStream is = ClasspathPluginSource.class.getResourceAsStream(resource);
		if (is != null) {
			try { is.close(); } catch (Throwable t) {  }
			return new EclipseTemplatingExtension(extensionId);
		}
		
		throw new NoSuchTemplatingExtensionException();
	}

	@Override
	public void gather(HashMap<String, IGramar> map) {

		IProject project[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (int p = 0; p < project.length; p++) {
			if (project[p].exists() && project[p].isOpen() && project[p].getFile(".gramar").exists()) {
				try {
					EclipseWorkspaceGramar gramar = new EclipseWorkspaceGramar(project[p]);
					String id = gramar.getId();
					map.put(id, gramar);
				} catch (InvalidGramarException e) {

				}
			}
		}
		
	}

}
