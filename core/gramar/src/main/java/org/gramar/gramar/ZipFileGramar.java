package org.gramar.gramar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.zip.ZipInputStream;

import org.gramar.exception.InvalidGramarException;


public class ZipFileGramar extends ZipGrammar {

	String absFileName;

	public ZipFileGramar(File file) throws InvalidGramarException {
		absFileName = file.getAbsolutePath();
		loadMeta();
	}

	@Override
	protected ZipInputStream getZipInputStream() throws FileNotFoundException {
		FileInputStream fis = new FileInputStream(absFileName);
		return new ZipInputStream(fis);
	}

}
