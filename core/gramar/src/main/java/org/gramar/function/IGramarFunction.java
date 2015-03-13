package org.gramar.function;

import org.gramar.IGramarContext;

/**
 * Any XPath function that needs to have access to information in the Gramar context
 * 
 * @author chrisgerken
 *
 */
public interface IGramarFunction {

	public void setContext(IGramarContext context);
	
}
