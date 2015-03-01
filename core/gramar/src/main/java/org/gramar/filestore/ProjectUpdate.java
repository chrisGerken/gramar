package org.gramar.filestore;

import org.gramar.IFileStore;

public class ProjectUpdate extends ResourceUpdate {

	private String altPath;
	
	public ProjectUpdate(String path, String altPath) {
		super(path);
		this.altPath = altPath;
	}

	@Override
	public void execute(IFileStore store) {

		store.createProject(path, altPath);
		
	}

}
