package org.gramar;

import javax.xml.xpath.XPathFunction;

import org.gramar.exception.NoSuchCustomTagException;
import org.gramar.extension.DefinedTag;


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
	
}
