package org.gramar.filestore;

import java.io.IOException;
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

}
