package org.gramar.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.ast.SourceRegion;
import org.gramar.ast.TagInfo;
import org.gramar.exception.GramarException;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.exception.MissingRequiredAttributeException;
import org.gramar.resource.MergeStream;
import org.w3c.dom.Node;

/**
 * A class that handles the interpretation and subsequent templating behavior
 * of a templating tag in a Gramar template.
 * 
 * @author chrisgerken
 *
 */
public abstract class TagHandler implements ITagHandler {

	protected ITagHandler parent;
	protected ArrayList<ITagHandler> children = new ArrayList<ITagHandler>();
	protected HashMap<String, String> attributes;
	
	public TagHandler() {
		// Needs to be no-argument
	}

	@Override
	public ITagHandler glomUsing(SourceRegion region, IGramarContext context) throws GramarException {

		if (region.isTag()) {
			TagInfo info = region.getTagInfo();
			String namespace = info.getNamespace();
			String tagName = info.getTagName();
			ITagHandler handler = context.getTagHandler(namespace, tagName);
			handler.setAttributes(region.getAttributes());
			handler.setParent(this);
			addChild(handler);
			return handler;
		} else if (region.isEndTag()) {
			if (region.getTagInfo().getTagName().equalsIgnoreCase(getTagName())) {
				return parent;
			}
			throw new IllFormedTemplateException(region,"Missing end tag: "+getTagName());
		} else if (region.isEmptyTag()) {
			TagInfo info = region.getTagInfo();
			String namespace = info.getNamespace();
			String tagName = info.getTagName();
			ITagHandler handler = context.getTagHandler(namespace, tagName);
			handler.setAttributes(region.getAttributes());
			handler.setParent(this);
			addChild(handler);
			return this;
		} else if (region.isText()) {
			StaticTextTag handler = new StaticTextTag(this, region.getContent());
			addChild(handler);
			return this;
		} else if (region.isDirective()) {
			return this;
		} 
		
		throw new IllFormedTemplateException(region, "Unknown source region type");
		
	}

	public abstract String getTagName();

	private void addChild(ITagHandler handler) {
		children.add(handler);
	}

	@Override
	public List<ITagHandler> getChildren() {
		return children;
	}

	public ITagHandler getParent() {
		return parent;
	}

	public void setParent(ITagHandler parent) {
		this.parent = parent;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}
	
	/**
	 * Merge all of the receiver's child handlers with the given stream
	 */
	public void processChildren(MergeStream stream, IGramarContext context) {
		for (ITagHandler child: children) {
			child.mergeTo(stream, context);
		}
	}
	
	/**
	 * Create a new stream and merge all of the receiver's child handlers
	 * with that stream.  Return the stream.
	 */
	public MergeStream processChildren(IGramarContext context) {
		MergeStream stream = new MergeStream();
		processChildren(stream, context);
		return stream;
	}
	
	/**
	 * Answers the first parent (direct or indirect) with the given tag name
	 */
	public ITagHandler parentNamed(String name) {
		if (getTagName().equalsIgnoreCase(name)) {
			return this;
		} else if (getParent() == null) {
			return null;
		} else {
			return getParent().parentNamed(name);
		}
	}
	
	/**
	 * Get the boolean value of the given property name.  If the property isn't
	 * found, default to the defaultValue.  Any XPath expressions nested in
	 * curly brackets are resolved.
	 * 
	 * @param attributeName
	 * @param context
	 * @param defaultValue
	 * @return
	 * @throws XPathExpressionException
	 */
	protected Boolean getBooleanAttribute(String attributeName, IGramarContext context, boolean defaultValue) throws XPathExpressionException {
		try {
			Boolean value = getBooleanAttribute(attributeName, context);
			return value;
		} catch (MissingRequiredAttributeException e) {
		}
		return defaultValue;
	}

	
	/**
	 * Get the boolean value of the given property name.  If the property isn't
	 * found then throw a MissingRequiredAttributeException.  Any XPath expressions nested in
	 * curly brackets are resolved.
	 * 
	 * @param attributeName
	 * @param context
	 * @param defaultValue
	 * @return
	 * @throws XPathExpressionException
	 * @throws MissingRequiredAttributeException 
	 */
	protected Boolean getBooleanAttribute(String attributeName, IGramarContext context) throws XPathExpressionException, MissingRequiredAttributeException {
		String value = getAttributes().get(attributeName);
		if (value == null) {
			throw new MissingRequiredAttributeException();
		}
		value = context.resolveExpressions(value);
		try { return Boolean.parseBoolean(value); } catch (Throwable t) { }
		return null;
	}
	
	/**
	 * Get the String value of the given property name.  If the property isn't
	 * found, default to the defaultValue.  Any XPath expressions nested in
	 * curly brackets are resolved.
	 * 
	 * @param attributeName
	 * @param context
	 * @param defaultValue
	 * @return
	 * @throws XPathExpressionException
	 */
	protected String getStringAttribute(String attributeName, IGramarContext context, String defaultValue) throws XPathExpressionException {
		try {
			String value = getStringAttribute(attributeName, context);
			return value;
		} catch (MissingRequiredAttributeException e) {
		}
		return defaultValue;
	}

	
	/**
	 * Get the String value of the given property name.  If the property isn't
	 * found then throw a MissingRequiredAttributeException.  Any XPath expressions nested in
	 * curly brackets are resolved.
	 * 
	 * @param attributeName
	 * @param context
	 * @param defaultValue
	 * @return
	 * @throws XPathExpressionException
	 * @throws MissingRequiredAttributeException 
	 */
	protected String getStringAttribute(String attributeName, IGramarContext context) throws XPathExpressionException, MissingRequiredAttributeException {
		String value = getAttributes().get(attributeName);
		if (value == null) {
			throw new MissingRequiredAttributeException();
		}
		return context.resolveExpressions(value);
	}
	
	/**
	 * Get the raw String value of the given property name.  If the property isn't
	 * found, default to the defaultValue.  No XPath expression resolution is performed
	 * 
	 * @param attributeName
	 * @param context
	 * @param defaultValue
	 * @return
	 * @throws XPathExpressionException
	 */
	protected String getRawAttribute(String attributeName, String defaultValue) {
		try {
			String value = getRawAttribute(attributeName);
			return value;
		} catch (MissingRequiredAttributeException e) {
		}
		return defaultValue;
	}

	
	/**
	 * Get the raw String value of the given property name.  If the property isn't
	 * found then throw a MissingRequiredAttributeException.  No XPath expression resolution
	 * is performed.
	 * 
	 * @param attributeName
	 * @param context
	 * @param defaultValue
	 * @return
	 * @throws XPathExpressionException
	 * @throws MissingRequiredAttributeException 
	 */
	protected String getRawAttribute(String attributeName) throws MissingRequiredAttributeException {
		String value = getAttributes().get(attributeName);
		if (value == null) {
			throw new MissingRequiredAttributeException();
		}
		return value;
	}
	
	/**
	 * Get the Node that is the result of evaluating the given property name as an XPath expression.
	 * If the property isn't found then throw an exception.  No XPath expression resolution is performed
	 * on expressions nested in curly brackets
	 * 
	 * @param attributeName
	 * @param context
	 * @return
	 * @throws MissingRequiredAttributeException 
	 * @throws XPathExpressionException
	 */
	protected Node getNodeAttribute(String attributeName, IGramarContext context) throws MissingRequiredAttributeException, XPathExpressionException {
		String expression = getAttributes().get(attributeName);
		if (expression == null) {
			throw new MissingRequiredAttributeException();
		}
		return context.resolveToNode(expression);
	}


}
