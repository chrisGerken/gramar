package org.gramar.filestore;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;

public class FileUpdate extends ResourceUpdate {
	
	private MergeStream content;

	public FileUpdate(String path, MergeStream content) {
		super(path);
		this.content = content;
	}

	@Override
	public void execute(IFileStore store) throws NoSuchResourceException {

		store.setFileContent(path, content.asInputStream());
	}

}
