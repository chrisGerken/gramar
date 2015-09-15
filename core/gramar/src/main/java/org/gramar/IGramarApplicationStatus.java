package org.gramar;

import org.gramar.exception.GramarException;

public interface IGramarApplicationStatus {

	public int getStatus();
	
	public int getModelAccesses();
	
	public boolean hadErrors();
	
	/**
	 * Returns the text resulting from the application of the model to the main production
	 * 
	 * @return Text produced by the main production
	 */
	public String mainProductionResult();
	
	/**
	 * Applies the new model using the gramar and platform from the previous gramar application
	 * 
	 * @param model
	 * @return the resulting application status object
	 * @throws GramarException 
	 */
	public IGramarApplicationStatus reApply(IModel model) throws GramarException;
	
}
