package org.gramar.gramar;

import java.io.IOException;
import java.io.InputStream;

import org.gramar.IGramar;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.plugin.ClasspathPluginSource;
import org.gramar.util.GramarHelper;


/**
 * An implementation of IGramar representing a gramar whose resources are stored as individual 
 * files in the classpath.  All files are found in or under the package with the
 * same name as the gramar ID. 
 */
public class ClasspathGramar extends Gramar implements IGramar {

	/**
	 * Construct a Gramar object representing the pattern with ID patternID 
	 */
	public ClasspathGramar(String gramarId) throws InvalidGramarException {
		this.gramarId = gramarId;
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
			throw new NoSuchResourceException(e);
		}

	}

	@Override
	public InputStream readTemplateBinary(String id) throws NoSuchResourceException {

		String pkg = gramarId.replace('.', '/');
		String resource = "/" + pkg + "/" + id;
		InputStream is = ClasspathPluginSource.class.getResourceAsStream(resource);
		if (is == null) {
			throw new NoSuchResourceException();
		}
		return is;

	}

}
