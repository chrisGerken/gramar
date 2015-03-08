package org.gramar.eclipse.platform;

import org.gramar.ITemplatingExtension;
import org.gramar.eclipse.ui.Activator;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.extension.ClasspathTemplatingExtension;

/**
 * An implementation of ITemplateExtension that provides extensions deployed in the classpath in 
 * an Eclipse plugin runtime environment.  
 * 
 * A ClasspathExtension is able to use the system classloader, which is not the correct class loader
 * to use in an Eclipse plugin.  Rather, the plugin-specific class loader must be used.  We access
 * it via the plugin's bundle.
 * 
 * @author chrisgerken
 *
 */
public class EclipseTemplatingExtension extends ClasspathTemplatingExtension
		implements ITemplatingExtension {

	public EclipseTemplatingExtension(String extensionID) throws InvalidTemplateExtensionException {
		super(extensionID);
	}

	@Override
	public Class loadClass(String fullyQualifiedName) throws ClassNotFoundException, Exception {
		return Activator.getDefault().getBundle().loadClass(fullyQualifiedName);
	}

	
}
