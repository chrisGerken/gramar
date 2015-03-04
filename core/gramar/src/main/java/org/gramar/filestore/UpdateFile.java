package org.gramar.filestore;

import java.io.IOException;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;

public class UpdateFile extends UpdateResource {
	
	private MergeStream content;

	public UpdateFile(String path, MergeStream content) {
		super(path);
		this.content = content;
	}

	@Override
	public void execute(IFileStore store) throws NoSuchResourceException, IOException {

		store.setFileContent(path, content.asReader());
	}
	
	public String toString() {
		return "UpdateFile:    " + path;
	}

}
