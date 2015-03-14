package org.gramar.resource;

import java.io.IOException;
import java.io.Reader;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.util.GramarHelper;

/**
 * Represents a request, targeting the FileStore, to create a file with 
 * a given path and content.  If the file exists, it should be replaced
 * only if the replaced flag is true.  Otherwise the flag is ignored.
 * 
 * @author chrisgerken
 *
 */
public class UpdateFile extends UpdateResource {
	
	private MergeStream content;
	private boolean 	replace;

	public UpdateFile(String path, MergeStream content, boolean replace) {
		super(path);
		this.content = content;
		this.replace = replace;
	}

	@Override
	public void execute(IFileStore store) throws NoSuchResourceException, IOException {

		Reader reader = store.getFileContent(path);
		
		if ((reader != null)  && !replace) {
			return;
		}
		
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
	
	public boolean isFile() {
		return true;
	}

}
