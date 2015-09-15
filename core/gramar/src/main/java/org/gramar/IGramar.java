package org.gramar;

import java.io.InputStream;
import java.util.List;

import org.gramar.exception.GramarException;
import org.gramar.exception.NoSuchResourceException;

public interface IGramar {

	public String getId();
	
	public String getLabel();
	
	public String getProvider();

	/**
	 * Answers the current relative path into the gramar of the gramar's primary production
	 * 
	 * @return the primary production id
	 */
	public String getMainProductionId();
	
	/**
	 * Sets the relative path of the gramar production that will be processed when the gramar
	 * is applied to the model.
	 * 
	 * @param newId
	 */
	public void setPrimaryProductionId(String newId);
	
	public ITemplate getTemplate(String id, IGramarContext context) throws GramarException;

	/**
	 * @param id
	 * @return a String containing the content of the given template
	 * @throws NoSuchResourceException
	 */
	public String getTemplateSource(String id) throws NoSuchResourceException;

	/**
	 * @param src
	 * @return an InputStream from which can be read the binary content of the given template
	 * @throws NoSuchResourceException 
	 */
	public InputStream getTemplateBinary(String src) throws NoSuchResourceException;

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
