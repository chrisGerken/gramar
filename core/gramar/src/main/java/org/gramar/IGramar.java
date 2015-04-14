package org.gramar;

import java.util.List;

import org.gramar.exception.GramarException;
import org.gramar.exception.NoSuchResourceException;

public interface IGramar {

	public String getId();
	
	public String getLabel();
	
	public String getProvider();
	
	public String getMainTemplateId();
	
	public ITemplate getTemplate(String id, IGramarContext context) throws GramarException;

	public String getTemplateSource(String id) throws NoSuchResourceException;

	/**
	 * @return a list of sample models deployed with this gramar
	 */
	public List<ISampleModel> getSamples();

	/**
	 * @param name
	 * @return the sample model configured with the given name 
	 */
	public IModel getSampleModel(String name);
	
 	/**
	 * Free any cached resources
	 */
	public void free();

	/**
	 * Estimates the applicability of the receiving gramar to the specified 
	 * proposed model.  The returned score is a double value between 0 and 100 
	 * where 100 represents a high applicability.
	 * 
	 * It is expected that the receiving gramar will count how many of the elements
	 * in the proposed model have the same xpath as an element in one of the sample
	 * models configured for the receiver.  The percentage would be returned as a 
	 * double value between 0 and 100 inclusive.    
	 * 
	 * @param proposedModel
	 * @return
	 */
	public Double scoreApplicability(IModel proposedModel);

}
