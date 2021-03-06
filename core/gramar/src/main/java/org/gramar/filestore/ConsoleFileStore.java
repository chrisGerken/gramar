package org.gramar.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

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
public class ConsoleFileStore extends FileStore implements IFileStore {

	public ConsoleFileStore() {

	}

	@Override
	public Reader getFileContent(String path) throws NoSuchResourceException {
		return null;
	}

	@Override
	public void setFileContent(String path, Reader reader) throws NoSuchResourceException {
		try {
			String content = GramarHelper.asString(reader);
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
	public void createFolder(String pathName) throws NoSuchResourceException {
		System.out.println("\n\nCreated folder: "+pathName);
	}

	@Override
	public void reset() {
		
	}

	@Override
	public void log(String message, int severity) {
		System.out.println(message);
	}

	@Override
	public InputStream getFileByteContent(String path) throws NoSuchResourceException {
		return null;
	}

	@Override
	public void setFileContent(String path, InputStream stream)
			throws NoSuchResourceException, IOException {
		// TODO Auto-generated method stub
		
	}

}
