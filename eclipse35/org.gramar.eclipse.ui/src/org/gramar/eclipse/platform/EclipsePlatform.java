package org.gramar.eclipse.platform;

import org.gramar.IGramarPlatform;
import org.gramar.platform.GramarPlatform;
import org.gramar.plugin.ClasspathPluginSource;

public class EclipsePlatform extends GramarPlatform implements IGramarPlatform {

	public EclipsePlatform() {
		super();
		addPluginSource(new EclipseWorkspacePluginSource());
		addPluginSource(new ClasspathPluginSource());
	}

	@Override
	protected void loadExtensions() {
		
	}

}
