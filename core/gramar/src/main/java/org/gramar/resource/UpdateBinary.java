package org.gramar.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.util.GramarHelper;

/**
 * Represents a request, targeting the FileStore, to create a file with 
 * a given path and binary source.  If the file exists, it should be replaced
 * only if the replaced flag is true.  Otherwise the flag is ignored. 
 * 
 * @author chrisgerken
 *
 */
public class UpdateBinary extends UpdateResource {
	
	private boolean 	replace;
	private byte[]		content;

	public UpdateBinary(String path, byte[] content, boolean replace) {
		super(path);
		this.replace = replace;
		this.content = content;
	}

	@Override
	public void execute(IFileStore store) throws NoSuchResourceException, IOException {
		
		if (store.resourceExists(path) && !replace) {
			return;
		}
		
		store.setFileContent(path, new ByteArrayInputStream(content));

	}
	
	public String toString() {
		return "UpdateBinary:    " + path;
	}

	public String report() {
		return "created file "+path;
	}
	
	public boolean isFile() {
		return true;
	}

}
