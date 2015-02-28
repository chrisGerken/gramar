package org.gramar;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunction;

import org.gramar.exception.GramarException;
import org.gramar.exception.NamespaceNotDefinedException;
import org.gramar.exception.NoSuchXPathFunctionException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public interface IGramarContext {

	public void setVariable(String name, Object value);
	
	public Object getVariable(String name);

	/*
	 * Sets the attribute on the node (obtained by resolving the nodeExpression) to
	 * the specified value
	 */
	public void setAttribute(String nodeExpression, String attrName, String value) throws XPathExpressionException;
	
	/*
	 * Replace substrings delimited by curly brackets with the results of evaluating those
	 * substrings as xpath expressions.  Return the resulting string.
	 */
	public String resolveExpressions(String pattern) throws XPathExpressionException;
	
	/*
	 * Resolve the given XPath expression to a single string
	 */
	public String resolveToString(String expression) throws XPathExpressionException;
	
	/*
	 * Resolve the given XPath expression to a boolean value
	 */
	public boolean resolveToBoolean(String expression) throws XPathExpressionException;
	
	/*
	 * Resolve the given XPath expression to a single node
	 */
	public Node resolveToNode(String expression) throws XPathExpressionException;
	
	/*
	 * Resolve the given XPath expression to an array of nodes
	 */
	public Node[] resolveToNodes(String expression) throws XPathExpressionException;

	/*
	 * Adds a secondary DOM to the context and associates the Document object at the root of that model 
	 * with the given variable name.
	 */
	public void addModel(String name, Document model);
	
	/*
	 * Associates a platform extension (ID) with a namespace.  References to custom templating tags
	 * by namespace (prefix) will resolve to tag handlers in the extension with this ID.
	 */
	public void declareTemplatingExtension(String namespace, String extensionId);
	
	/*
	 * Return the extension ID that has been associated with this namespace.
	 */
	public String extensionIdForNamespace(String namespace) throws NamespaceNotDefinedException;

	/*
	 * Answers the current file store
	 */
	public IFileStore getFileStore();

	/*
	 * Sets the current file store
	 */
	public void setFileStore(IFileStore fileStore);
	
	/*
	 * Answers the PatternPlatform from which all extensions can be eventually located
	 */
	public IGramarPlatform getPlatform();

	/*
	 * Answers the tag handler with the given name from the extension associated with the given namespace
	 */
	public ICustomTagHandler getCustomTagHandler(String namespace, String tagName) throws GramarException;

	/*
	 * Answers the xpath function that best matches the given name and arity
	 */
	public XPathFunction getXPathFunction(String name, int arity) throws GramarException;
	
	public boolean isExtensionDefined(String extensionId);

	public void setPattern(IGramar pattern);
	
	public IGramar getPattern();

	/*
	 * Log a warning
	 */
	public void warning(Exception e);
	
	/*
	 * Log an error
	 */
	public void error(Exception e);
	
}
