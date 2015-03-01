package org.gramar.filestore;

import java.util.ArrayList;
import java.util.Arrays;

import org.gramar.IFileStore;
import org.gramar.IGramarContext;
import org.gramar.exception.NoSuchResourceException;

public abstract class FileStore implements IFileStore {

	protected ArrayList<ResourceUpdate> updates = new ArrayList<ResourceUpdate>();
	
	public FileStore() {

	}

	@Override
	public void commit(String comment, IGramarContext context) {

		ResourceUpdate update[] = new ResourceUpdate[updates.size()];
		updates.toArray(update);
		Arrays.sort(update);
		
		for (ResourceUpdate ru: update) {
			try {
				ru.execute(this);
			} catch (NoSuchResourceException e) {
				context.error(e);
			}
		}
		
	}

	@Override
	public void addUpdate(ResourceUpdate update) {
		updates.add(update);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
