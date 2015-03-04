package org.gramar.resource;

import java.io.IOException;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;

public abstract class UpdateResource implements Comparable<UpdateResource> {

	protected String path;
	
	public UpdateResource(String path) {
		this.path = path;
	}
	
	public abstract void execute(IFileStore store) throws NoSuchResourceException, IOException;

	@Override
	public int compareTo(UpdateResource o) {
		return path.compareTo(o.path);
	}

}
