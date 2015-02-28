package org.gramar;

import org.w3c.dom.Document;

/*
 * Represents a lightweight wrapper around an object than can answer 
 * a DOM representation of that object 
 */
public interface IModel {
	
	public Document asDOM();

}
