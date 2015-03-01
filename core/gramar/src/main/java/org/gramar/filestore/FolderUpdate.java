package org.gramar.filestore;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;

public class FolderUpdate extends ResourceUpdate {

	public FolderUpdate(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(IFileStore store) throws NoSuchResourceException {
		
		store.createFolder(path);
		
	}

}
