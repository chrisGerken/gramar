package org.gramar.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;

public class UnFileStore extends FileStore implements IFileStore {

	public UnFileStore() {
		super();
	}

	@Override
	public Reader getFileContent(String path) throws NoSuchResourceException {
		return null;
	}

	@Override
	public void setFileContent(String path, Reader reader)
			throws NoSuchResourceException, IOException {
	}

	@Override
	public InputStream getFileByteContent(String path)
			throws NoSuchResourceException {
		return null;
	}

	@Override
	public void setFileContent(String path, InputStream stream)
			throws NoSuchResourceException, IOException {
	}

	@Override
	public boolean resourceExists(String path) {
		return false;
	}

	@Override
	public void createProject(String projectName, String altPath)
			throws Exception {
	}

	@Override
	public void createFolder(String pathName) throws NoSuchResourceException,
			IOException {
	}

	@Override
	public void log(String message, int severity) {
	}

}
