package org.gramar.filestore;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;

public abstract class ResourceUpdate implements Comparable<ResourceUpdate> {

	protected String path;
	
	public ResourceUpdate(String path) {
		this.path = path;
	}
	
	public abstract void execute(IFileStore store) throws NoSuchResourceException;

	@Override
	public int compareTo(ResourceUpdate o) {
		return path.compareTo(o.path);
	}

}
