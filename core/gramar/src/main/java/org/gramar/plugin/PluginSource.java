package org.gramar.plugin;

import java.util.HashMap;

import org.gramar.IFileStore;
import org.gramar.IGramar;
import org.gramar.IPluginSource;
import org.gramar.ITemplatingExtension;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.NoSuchFileStoreException;
import org.gramar.exception.NoSuchGramarException;


public abstract class PluginSource implements IPluginSource {

	protected boolean autoRefresh = true;
	private HashMap<String, IGramar> cache = null;
	private HashMap<String, IGramar> knownPatterns = new HashMap<String, IGramar>();
	
	public PluginSource() {

	}
	
	public void gather() {
		if ((cache==null) || autoRefresh) {
			HashMap<String, IGramar> map = new HashMap<String, IGramar>();
			gather(map);
			cache = map;
		}
	}

	public void gather(HashMap<String, IGramar> map) {
		for (String id: knownPatterns.keySet()) {
			map.put(id, knownPatterns.get(id));
		}
	}

	@Override
	public String[] list() {
		gather();
		String result[] = new String[cache.size()];
		cache.keySet().toArray(result);
		return result;
	}

	@Override
	public IGramar[] gramars() {
		gather();
		IGramar result[] = new IGramar[cache.size()];
		cache.values().toArray(result);
		return result;
	}

	@Override
	public IGramar getGramar(String gramarId) throws NoSuchGramarException, InvalidGramarException {
		gather();
		IGramar pattern = cache.get(gramarId);
		if (pattern == null) {
			throw new NoSuchGramarException();
		}
		return pattern;
	}

	@Override
	public void addKnownGramar(String id) throws NoSuchGramarException, InvalidGramarException {
		knownPatterns.put(id, getGramar(id));
	}

}
