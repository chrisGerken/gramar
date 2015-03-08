package org.gramar;

import javax.xml.xpath.XPathFunction;

import org.gramar.exception.NoSuchCustomTagException;
import org.gramar.extension.DefinedTag;

/**
 * Represents an object that extends the templating platform with tag handlers and/or
 * XPath functions.  Platform extensions can be deployed in many ways and each deployment
 * method requires an implementation of ITemplatingExtension to answer the extending tag
 * handlers and XPath functions.
 * 
 * @author chrisgerken
 *
 */
public interface ITemplatingExtension {

	/*
	 * Return the default prefix to use if one is not specified
	 */
	public String getDefaultAbbreviation();
	
	/*
	 * Return the unique extension ID, a single string in the 
	 * form of a valid java package name
	 */
	public String getExtensionId();
	
	public XPathFunction getFunction(String name, int arity);

	public DefinedTag getTagDef(String tagName) throws NoSuchCustomTagException;
	
	public ITagHandler getCustomTagHandler(String tagName) throws NoSuchCustomTagException;

	public boolean hasCustomTagHandler(String tagName);
	
	public Class loadClass(String fullyQualifiedName) throws ClassNotFoundException, Exception;
	
}
