package org.gramar.resource;

import java.io.IOException;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;

public class UpdateFolder extends UpdateResource {

	public UpdateFolder(String path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(IFileStore store) throws NoSuchResourceException, IOException {
		
		store.createFolder(path);
		
	}
	
	public String toString() {
		return "UpdateFolder:  " + path;
	}

	public String report() {
		return "created folder "+path;
	}

}
