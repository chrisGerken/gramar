package org.gramar.platform;

import org.gramar.IGramarPlatform;
import org.gramar.ITemplatingExtension;
import org.gramar.plugin.ClasspathPluginSource;


public class SimpleGramarPlatform extends GramarPlatform implements IGramarPlatform {
	
	public SimpleGramarPlatform() {
		addPluginSource(new ClasspathPluginSource());
	}

	@Override
	protected void loadExtensions() {
		
	}

}
