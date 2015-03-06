package org.gramar.plugin;

import java.util.HashMap;

import org.gramar.IGramar;
import org.gramar.IPluginSource;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.NoSuchGramarException;


public abstract class PluginSource implements IPluginSource {

	protected boolean autoRefresh = true;
	private HashMap<String, IGramar> cache = null;
	
	public PluginSource() {

	}
	
	public void gather() {
		if ((cache==null) || autoRefresh) {
			HashMap<String, IGramar> map = new HashMap<String, IGramar>();
			gather(map);
			cache = map;
		}
	}

	public abstract void gather(HashMap<String, IGramar> map);

	@Override
	public String[] list() {
		gather();
		String result[] = new String[cache.size()];
		cache.keySet().toArray(result);
		return result;
	}

	@Override
	public IGramar[] patterns() {
		gather();
		IGramar result[] = new IGramar[cache.size()];
		cache.values().toArray(result);
		return result;
	}

	@Override
	public IGramar getPattern(String patternId) throws NoSuchGramarException, InvalidGramarException {
		gather();
		IGramar pattern = cache.get(patternId);
		if (pattern == null) {
			throw new NoSuchGramarException();
		}
		return pattern;
	}

}
