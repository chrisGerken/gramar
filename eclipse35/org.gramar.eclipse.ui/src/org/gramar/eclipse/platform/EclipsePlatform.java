package org.gramar.eclipse.platform;

import org.gramar.IGramarApplicationStatus;
import org.gramar.IGramarPlatform;
import org.gramar.IModel;
import org.gramar.model.XmlModel;
import org.gramar.platform.GramarPlatform;
import org.gramar.plugin.ClasspathPluginSource;

public class EclipsePlatform extends GramarPlatform implements IGramarPlatform {

	public EclipsePlatform() {
		super();
	}

	@Override
	protected void loadExtensions() {
		addPluginSource(new EclipseWorkspacePluginSource());
		addPluginSource(new ClasspathPluginSource());
		setDefaultFileStore(new EclipseFileStore());
	}

	public static IGramarApplicationStatus apply(String modelContent, String gramarId) throws Exception {

		EclipsePlatform platform = new EclipsePlatform();
		EclipseFileStore fileStore = (EclipseFileStore) platform.getDefaultFileStore();
		IModel model = new XmlModel(modelContent);
		return platform.apply(model, gramarId, fileStore);

	}
	
}
