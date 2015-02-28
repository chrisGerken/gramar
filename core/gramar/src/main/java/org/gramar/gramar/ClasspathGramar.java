package org.gramar.gramar;

import java.io.IOException;
import java.io.InputStream;

import org.gramar.IGramar;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.plugin.ClasspathPluginSource;
import org.gramar.util.GramarHelper;


/*
 * A Pattern object representing a pattern whose resources are stored as individual 
 * files in the classpath.  All files are found in or under the package with the
 * same name as the pattern ID. 
 */
public class ClasspathGramar extends Gramar implements IGramar {

	/*
	 * Construct a Pattern object representing the pattern with ID patternID 
	 */
	public ClasspathGramar(String patternId) throws InvalidGramarException {
		this.patternId = patternId;
		loadMeta();
	}

	/*
	 * Look for a file in the classpath with the package with the same name as the pattern
	 * For example, the file "d/xyz.txt" for pattern ID a.b.c would be found at resource /a/b/c/d/xyz.txt
	 * 
	 * If such a resource is found, return its source contents.  Otherwise throw an exception
	 */
	@Override
	public String readTemplateSource(String id) throws NoSuchResourceException {

		try {
			String pkg = patternId.replace('.', '/');
			String resource = "/" + pkg + "/" + id;
			InputStream is = ClasspathPluginSource.class.getResourceAsStream(resource);
			return GramarHelper.asString(is);
		} catch (IOException e) {
			throw new NoSuchResourceException(e);
		}

	}

}
