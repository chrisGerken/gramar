package org.gramar;

import java.io.InputStream;
import java.util.Properties;

import org.gramar.exception.GramarException;
import org.w3c.dom.Document;

/**
 * A class that is able to convert an input model into an XML DOM
 * 
 * @author chrisgerken
 *
 */
public interface IModelAdaptor {

	/**
	 * Answers the model adaptor ID for this adaptor
	 * 
	 * @return model adaptor ID
	 */
	public String getId();
	
	/**
	 * Answers the type of source consumed by this adaptor.  Usually is the same as the expected file type
	 * of the data (e.g. "xml" or "json".  The value should be in lower case.
	 * @return
	 */
	public String getType();
	
	/**
	 * Converts the source into an XML DOM and answers the DOM's document node.  This methods is used when additional
	 * information must be provided in order to convert the source. 
	 * 
	 * @param properties 
	 * @return an XML Document
	 * @throws GramarException
	 */
	public Document asDocument(Properties properties) throws GramarException;
	
	/**
	 * Converts the given source content into an XML DOM and answers the DOM's document node.
	 * 
	 * @param source 
	 * @return an XML Document
	 * @throws GramarException
	 */
	public Document asDocument(String source) throws GramarException;
	
	/**
	 * Reads and converts the content InputStream and then answers the rsulting DOM's document node.
	 * 
	 * @param stream 
	 * @return an XML Document
	 * @throws GramarException
	 */
	public Document asDocument(InputStream stream) throws GramarException;

	/**
	 * Answers whether the receiver is able to convert the given model source type to an XML DOM
	 * 
	 * @param type
	 * @return whether this adaptor is an appropriate adaptor for the given source type
	 */
	public boolean adaptsType(String type);
	
}
