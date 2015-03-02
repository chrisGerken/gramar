package org.gramar.filestore;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;

public class UpdateFolder extends UpdateResource {

	public UpdateFolder(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(IFileStore store) throws NoSuchResourceException {
		
		store.createFolder(path);
		
	}
	
	public String toString() {
		return "UpdateFolder:  " + path;
	}

}
