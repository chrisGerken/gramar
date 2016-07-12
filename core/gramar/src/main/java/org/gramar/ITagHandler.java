package org.gramar;

import java.util.HashMap;
import java.util.List;

import org.gramar.ast.SourceRegion;
import org.gramar.exception.GramarException;
import org.gramar.exception.MissingRequiredAttributeException;
import org.gramar.resource.MergeStream;


public interface ITagHandler {

	public void mergeTo(MergeStream stream, IGramarContext context);
	
	public ITagHandler getParent();
	
	public List<ITagHandler> getChildren();

	public ITagHandler glomUsing(SourceRegion region, IGramarContext context) throws GramarException;

	public void setParent(ITagHandler parent);

	public void setAttributes(HashMap<String, String> attributes);

	public ITagHandler parentNamed(String name);

	public String getTagName();
	
	public String getRawAttribute(String attributeName, String defaultValue);


	/**
	 * Sets the original SourceRegion as parsed from the AST
	 * 
	 * @param region
	 */
	public void setSourceRegion(SourceRegion region);

	/**
	 * Answer a List of Strings, one for each level of tag nesting.  The first string represents the tag
	 * document itself and the last string represents the receiver.  Each level, except the first,
	 * indicates the line number and starting column of the tag as well as the body of the tag flattened 
	 * to a single line
	 * 
	 * @return List<String> describing the nested tags from the document tag to the receiver.
	 */
	public List<String> stackTrace();

}
