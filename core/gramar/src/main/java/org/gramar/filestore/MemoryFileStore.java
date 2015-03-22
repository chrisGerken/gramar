package org.gramar.filestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.util.GramarHelper;

/**
 * An implementation of IFileStore that holds file contents in memory.  Created folder and
 * project names are also kept.  This FileStore is useful for testing of user regions.
 * 
 * @author chrisgerken
 *
 */
public class MemoryFileStore extends FileStore implements IFileStore {

	private HashMap<String, String> files = new HashMap<String, String>();
	private HashMap<String, byte[]> binaries = new HashMap<String, byte[]>();
	private HashSet<String> folders = new HashSet<String>();
	private HashSet<String> projects = new HashSet<String>();
	private ArrayList<String> logs = new ArrayList<String>();
	
	public MemoryFileStore() {

	}

	@Override
	public Reader getFileContent(String path) throws NoSuchResourceException {
		String content = files.get(path);
		if (content == null) { 
			return null;
		}
		return new StringReader(content);
	}

	@Override
	public void setFileContent(String path, Reader reader) throws NoSuchResourceException, IOException {
		files.put(path, GramarHelper.asString(reader));
	}

	@Override
	public boolean resourceExists(String path) {
		return files.containsKey(path);
	}

	@Override
	public void createProject(String projectName, String altPath) throws IOException {
		projects.add(projectName);
	}

	@Override
	public void createFolder(String pathName) throws NoSuchResourceException, IOException {
		folders.add(pathName);
	}

	@Override
	public void log(String message) {
		logs.add(message);
	}
	
	public ArrayList<String> getLogs() {
		return logs;
	}

	@Override
	public void free() {
		files = new HashMap<String, String>();
		folders = new HashSet<String>();
		projects = new HashSet<String>();
		logs = new ArrayList<String>();
	}

	@Override
	public InputStream getFileByteContent(String path) throws NoSuchResourceException {
		if (files.containsKey(path)) {
			return new ByteArrayInputStream(files.get(path).getBytes());
		}
		if (binaries.containsKey(path)) {
			return new ByteArrayInputStream(binaries.get(path));
		}
		return null;
	}

	@Override
	public void setFileContent(String path, InputStream stream) throws NoSuchResourceException, IOException {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		GramarHelper.copy(stream, boas);
		binaries.put(path, boas.toByteArray());
	}

}
