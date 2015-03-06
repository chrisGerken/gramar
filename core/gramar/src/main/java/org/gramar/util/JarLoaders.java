package org.gramar.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;

/**
 * A utility class that manages class loaders for the various jar files
 * that might have been used to deploy gramars and/or extensions.
 * 
 * @author chrisgerken
 *
 */
public class JarLoaders {

//	private static JarLoaders instance = new JarLoaders();
	
	private static HashMap<String, ClassLoader> loaders = new HashMap<String, ClassLoader>();
	
	private JarLoaders() {

	}

	public static ClassLoader loaderFor(String jarPath) throws IOException {
		ClassLoader loader = loaders.get(jarPath);
		if (loader == null) {
			URL url[] = new URL[1];
			url[0] = new URL("file:"+jarPath);
			loader = new URLClassLoader(url);
			loaders.put(jarPath, loader);
		}
		return loader;
	}

	
}
