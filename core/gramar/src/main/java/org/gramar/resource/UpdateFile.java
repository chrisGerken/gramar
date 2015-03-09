package org.gramar.resource;

import java.io.IOException;
import java.io.Reader;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.util.GramarHelper;

public class UpdateFile extends UpdateResource {
	
	private MergeStream content;

	public UpdateFile(String path, MergeStream content) {
		super(path);
		this.content = content;
	}

	@Override
	public void execute(IFileStore store) throws NoSuchResourceException, IOException {

		Reader reader = store.getFileContent(path);
		if ((content.hasUserRegions()) && (reader != null)) {
			
			String prev = GramarHelper.asString(reader);
			
			content = content.saveRegionChanges(prev);
			
		}
		
		store.setFileContent(path, content.asReader());
	}
	
	public String toString() {
		return "UpdateFile:    " + path;
	}

	public String report() {
		return "created file "+path;
	}
}
