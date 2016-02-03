package org.gramar.filestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import org.gramar.IFileStore;
import org.gramar.IGramarContext;
import org.gramar.IGramarStatus;
import org.gramar.exception.GramarException;
import org.gramar.exception.GramarPlatformConfigurationException;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.resource.UpdateResource;
import org.gramar.util.GramarHelper;

public abstract class FileStore implements IFileStore {

	protected ArrayList<UpdateResource> updates = new ArrayList<UpdateResource>();
	private   int minLogLevel = IGramarStatus.SEVERITY_ERROR;
	
	public FileStore() {

	}

	@Override
	public boolean sameBytes(String relpath, byte[] after) throws IOException {
		try {
			byte before[] = GramarHelper.getBytes(getFileByteContent(relpath));
			
			if (before.length != after.length) {
				return false;
			}
			
			for (int i = 0; i < before.length; i++) {
				if (before[i] != after[i]) {
					return false;
				}
			}
		} catch (NoSuchResourceException e) {
			return false;
		}
		
		return true;
	}

	@Override
	public void commit(String comment, IGramarContext context) throws GramarException {

		executeUpdates(comment, context);
	}
	
	protected void executeUpdates(String comment, IGramarContext context) {

		UpdateResource update[] = new UpdateResource[updates.size()];
		updates.toArray(update);
		Arrays.sort(update);
		
		for (UpdateResource ru: update) {
			try {
				ru.execute(this);
				context.info(ru.report());
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

	@Override
	public void free() {
		updates = new ArrayList<UpdateResource>();
	}

	@Override
	public void configure(Properties properties) throws GramarPlatformConfigurationException {
		
	}

	@Override
	public boolean logMessage(String message, int severity) {
		if (minLogLevel <= severity) {
			log(message,severity);
			return true;
		}
		return false;
	}

	@Override
	public int getMinLogLevel() {
		return minLogLevel;
	}

	@Override
	public void setMinLogLevel(int minLogLevel) {
		this.minLogLevel = minLogLevel;
	}


}
