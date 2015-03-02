package org.gramar.filestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.gramar.IFileStore;
import org.gramar.IGramarContext;
import org.gramar.exception.NoSuchResourceException;

public abstract class FileStore implements IFileStore {

	protected ArrayList<UpdateResource> updates = new ArrayList<UpdateResource>();
	
	public FileStore() {

	}

	@Override
	public void commit(String comment, IGramarContext context) {

		UpdateResource update[] = new UpdateResource[updates.size()];
		updates.toArray(update);
		Arrays.sort(update);
		
		for (UpdateResource ru: update) {
			try {
				ru.execute(this);
			} catch (Exception e) {
				context.error(e);
			}
		}
		
	}

	@Override
	public void addUpdate(UpdateResource update) {
		updates.add(update);
	}

	@Override
	public void reset() {
		
	}

}
