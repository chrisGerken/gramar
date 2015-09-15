package org.gramar;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunction;

import org.gramar.exception.GramarException;
import org.gramar.exception.NamespaceNotDefinedException;
import org.gramar.extension.DefinedTag;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public interface IGramarContext {

	/**
	 * Sets associates the value with the variable name.  
	 * Ignored if the variabe name is null 
	 */
	public void setVariable(String variableName, Object value);

	/**
	 * Removes the association between the variable name and its value.  
	 * Ignored if the variabe name is null
	 */
	public void unsetVariable(String variableName);
	
	/**
	 * Answers the value associated with the variable name
	 */
	public Object getVariable(String variableName);

	/**
	 * Sets the attribute on the node (obtained by resolving the nodeExpression) to
	 * the specified value
	 */
	public void setAttribute(String nodeExpression, String attrName, String value) throws XPathExpressionException;
	
	/**
	 * Replace substrings delimited by curly brackets with the results of evaluating those
	 * substrings as xpath expressions.  Return the resulting string.
	 */
	public String resolveExpressions(String pattern) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression to a single string
	 */
	public String resolveToString(String expression) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression from the sourceNode to a single string
	 */
	public String resolveToString(String expression, Node sourceNode) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression to a boolean value
	 */
	public boolean resolveToBoolean(String expression) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression from the sourceNode to a boolean value
	 */
	public boolean resolveToBoolean(String expression, Node sourceNode) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression to a numeric (double) value
	 */
	public double resolveToNumber(String expression) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression from the sourceNode to a numeric (double) value
	 */
	public double resolveToNumber(String expression, Node sourceNode) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression to a value and return the correct type of result.
	 * If the expression results in a NodeSet of length greater than one then return the 
	 * first node in the NodeSet.  Otherwise if the expression returns a numeric value that is
	 * not NaN then return the result as a Double.  Otherwise, return the result as a String.
	 * Booleans won't be returned because of the difficulty in differentiating between 
	 * 'false' and true.
	 */
	public Object resolveToObject(String expression) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression from the source node to a value and return the correct type of result.
	 * If the expression results in a NodeSet of length greater than one then return the 
	 * first node in the NodeSet.  Otherwise if the expression returns a numeric value that is
	 * not NaN then return the result as a Double.  Otherwise, return the result as a String.
	 * Booleans won't be returned because of the difficulty in differentiating between 
	 * 'false' and true.
	 */
	public Object resolveToObject(String expression, Node sourceNode) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression to a single node
	 */
	public Node resolveToNode(String expression) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression from the sourceNode to a single node
	 */
	public Node resolveToNode(String expression, Node sourceNode) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression to an array of nodes
	 */
	public Node[] resolveToNodes(String expression) throws XPathExpressionException;
	
	/**
	 * Resolve the given XPath expression from the sourceNode to an array of nodes
	 */
	public Node[] resolveToNodes(String expression, Node sourceNode) throws XPathExpressionException;

	/**
	 * Adds a secondary DOM to the context and associates the Document object at the root of that model 
	 * with the given variable name.
	 */
	public void addModel(String name, Document model);
	
	/**
	 * Answer the model specified when the context was originally constructed
	 */
	public Document getPrimaryModel();
	
	/**
	 * Answer the gramar being applied
	 */
	public IGramar getGramar();
	
	/**
	 * Associates a platform extension (ID) with a namespace.  References to custom templating tags
	 * by namespace (prefix) will resolve to tag handlers in the extension with this ID.
	 */
	public void declareTemplatingExtension(String namespace, String extensionId);
	
	/**
	 * Return the extension ID that has been associated with this namespace.
	 */
	public String extensionIdForNamespace(String namespace) throws NamespaceNotDefinedException;

	/**
	 * Answers the current file store
	 */
	public IFileStore getFileStore();

	/**
	 * Sets the current file store
	 */
	public void setFileStore(IFileStore fileStore);
	
	/**
	 * Answers the PatternPlatform from which all extensions can be eventually located
	 */
	public IGramarPlatform getPlatform();

	/**
	 * Answers the tag definition with the given name from the extension associated with the given namespace
	 */
	public DefinedTag getTagDef(String namespace, String tagName) throws GramarException;

	/**
	 * Answers the tag handler with the given name from the extension associated with the given namespace.
	 * Each call to this method returns a new instance of the tag handler class.
	 */
	public ITagHandler getTagHandler(String namespace, String tagName) throws GramarException;

	/**
	 * Answers the xpath function that best matches the given name and arity from the extension associated
	 * with the given namespace
	 */
	public XPathFunction getXPathFunction(String namespace, String name, int arity) throws GramarException;
	
	public boolean isExtensionDefined(String extensionId);

	public void setGramar(IGramar pattern);
	
	public IGramar getPattern();

	/**
	 * Log a warning
	 */
	public void warning(Exception e);
	
	/**
	 * Log an error
	 */
	public void error(Exception e);
	
	public XPath getXPath();
	
	public int getModelAccessCount();

	/**
	 * Log a debug-level message
	 * @param message - the text to be written
	 */
	public void debug(String message);

	/**
	 * Log an informational message
	 * @param message - the text to be written
	 */
	public void info(String message);

	/**
	 * Log a warning message
	 * @param message - the text to be written
	 */
	public void warning(String message);

	/**
	 * Log an error message
	 * @param message - the text to be written
	 */
	public void error(String message);
	
	/**
	 * Frees up any cached resources
	 */
	public void free();

	public int getMaxStatus();

}
