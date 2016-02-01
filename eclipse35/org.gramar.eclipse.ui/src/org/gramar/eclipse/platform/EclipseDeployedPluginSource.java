package org.gramar.eclipse.platform;

import java.util.HashMap;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.gramar.IGramar;
import org.gramar.IPluginSource;
import org.gramar.ITemplatingExtension;
import org.gramar.eclipse.ui.Activator;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.gramar.ClasspathGramar;
import org.gramar.gramar.Gramar;
import org.gramar.plugin.PluginSource;
import org.osgi.framework.Bundle;
import org.osgi.framework.wiring.BundleWiring;

public class EclipseDeployedPluginSource extends PluginSource implements
		IPluginSource {

	public EclipseDeployedPluginSource() {

	}

	@Override
	public ITemplatingExtension getTemplatingExtension(String extensionId)
			throws NoSuchTemplatingExtensionException,
			InvalidTemplateExtensionException {

		throw new NoSuchTemplatingExtensionException();
		
	}

	@Override
	public void gather(HashMap<String, IGramar> map) {
		
		// For each gramar ID called out in a deploy plugin extension, construct
		// a ClasspathGramar using the owning plugin's bundle class loader and add that
		// gramar to the passed map.

		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint("org.gramar.eclipse.ui.deploy");
		if (point == null) return;
		IExtension[] extensions = point.getExtensions();
		for (IExtension extension : extensions) {
			ClassLoader classLoader = null;
			IConfigurationElement[] configurationElements = extension.getConfigurationElements();
			for (IConfigurationElement element: configurationElements) {
				if (element.getName().equalsIgnoreCase("deploy")) {
					String gramarId = element.getAttribute("id");
					if (classLoader==null) {
						String contributor = element.getDeclaringExtension().getContributor().getName();
						Bundle bundle = Platform.getBundle(contributor);
						classLoader = bundle.adapt(BundleWiring.class).getClassLoader();
					}
					try {
						Gramar deployedGramar = new ClasspathGramar(gramarId,classLoader);
						map.put(gramarId, deployedGramar);
					} catch (InvalidGramarException e) {
						Activator.logError(e);
					}
				}
			}
		}
		
	}

}
