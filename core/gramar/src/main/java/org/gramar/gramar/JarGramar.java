package org.gramar.gramar;

import java.io.IOException;
import java.io.InputStream;

import org.gramar.IGramar;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.util.GramarHelper;
import org.gramar.util.JarLoaders;

/**
 * An implementation of IGramar representing a gramar whose resources are stored within a jar 
 * file.  All files are found in or under the package with the same name as the gramar ID. 
 */
public class JarGramar extends Gramar implements IGramar {

	private String jarPath;
	
	/**
	 * Construct a Gramar object representing the pattern with ID patternID 
	 */
	public JarGramar(String gramarId, String jarPath) throws InvalidGramarException {
		this.gramarId = gramarId;
		this.jarPath = jarPath;
		loadMeta();
	}

	/**
	 * Look for a file in the classpath with the package with the same name as the pattern
	 * For example, the file "d/xyz.txt" for pattern ID a.b.c would be found at resource /a/b/c/d/xyz.txt
	 * 
	 * If such a resource is found, return its source contents.  Otherwise throw an exception
	 */
	@Override
	public String readTemplateSource(String id) throws NoSuchResourceException {

		try {
			return GramarHelper.asString(readTemplateBinary(id));
		} catch (IOException e) {
			throw new NoSuchResourceException(id,e);
		}

	}

	@Override
	public InputStream readTemplateBinary(String id) throws NoSuchResourceException {

		try {
			String pkg = gramarId.replace('.', '/');
			String resource =  pkg + "/" + id;
			InputStream is = JarLoaders.loaderFor(jarPath).getResourceAsStream(resource);
			return is;
		} catch (IOException e) {
			throw new NoSuchResourceException(id,e);
		}

	}

}
