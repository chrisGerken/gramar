package org.gramar.filestore;

import java.io.IOException;
import java.io.InputStream;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.util.GramarHelper;


/**
 * A very simple IFileStore implementation that "stores" file contents out to the console and never reads them.
 * 
 * For debugging, primarily.
 * 
 * @author chrisgerken
 *
 */
public class ConsoleFileStore implements IFileStore {

	public ConsoleFileStore() {

	}

	@Override
	public InputStream getFileContent(String path) throws NoSuchResourceException {
		return null;
	}

	@Override
	public void setFileContent(String path, InputStream is) throws NoSuchResourceException {
		try {
			String content = GramarHelper.asString(is);
			System.out.println("\n\nPath: "+path+"\n\n"+content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean resourceExists(String path) {
		return false;
	}

	@Override
	public void createProject(String projectName, String path) {
		System.out.println("\n\nCreated project: "+projectName);
	}

	@Override
	public void createFolder(String projectName, String pathName) throws NoSuchResourceException {
		System.out.println("\n\nCreated folder: "+projectName+"/"+pathName);
	}

	@Override
	public void commit(String comment) {

	}

}
