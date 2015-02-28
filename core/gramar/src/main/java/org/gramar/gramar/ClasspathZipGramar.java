package org.gramar.gramar;

import java.io.InputStream;
import java.util.zip.ZipInputStream;

import org.gramar.exception.InvalidGramarException;


public class ClasspathZipGramar extends ZipGrammar {

	private String resource;
	
	public ClasspathZipGramar(String resource) throws InvalidGramarException {
		this.resource = resource;
		loadMeta();
	}

	@Override
	protected ZipInputStream getZipInputStream() {
		InputStream is = ClasspathZipGramar.class.getResourceAsStream(resource);
		return new ZipInputStream(is);
	}

}
