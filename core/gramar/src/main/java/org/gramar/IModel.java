package org.gramar;

import java.util.HashMap;

import org.w3c.dom.Document;

/*
 * Represents a lightweight wrapper around an object than can answer 
 * a DOM representation of that object 
 */
public interface IModel {
	
	public Document asDOM();

	/**
	 * 
	 * @return Hashmap keyed by unique xpaths found in the model (elements only)
	 * with values being the integer occurrences of those xpaths
	 * @throws Exception 
	 */
	public HashMap<String, Integer> getXpaths() throws Exception;

}
