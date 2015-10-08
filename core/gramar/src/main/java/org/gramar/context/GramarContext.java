package org.gramar.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPathFunction;

import org.gramar.IFileStore;
import org.gramar.IGramar;
import org.gramar.IGramarContext;
import org.gramar.IGramarPlatform;
import org.gramar.IGramarStatus;
import org.gramar.ITagHandler;
import org.gramar.ITemplatingExtension;
import org.gramar.exception.GramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NamespaceNotDefinedException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.exception.NoSuchXPathFunctionException;
import org.gramar.extension.DefinedTag;
import org.gramar.model.ModelAccess;
import org.gramar.model.XmlModel;
import org.gramar.platform.GramarStatus;
import org.gramar.platform.SimpleGramarPlatform;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class GramarContext implements IGramarContext {

	private GramarContext	parentContext = null;
	
	private Document primaryModel;
	
	private HashMap<String, Document> secondaryModels = new HashMap<String, Document>();
	
	private HashMap<String, Object> variables = new HashMap<String, Object>();
	
	private HashMap<String, String> extensions = new HashMap<String, String>();
	
	private IFileStore fileStore;
	
	private IGramarPlatform platform;
	
	private IGramar gramar;
	
	private ArrayList<IGramarStatus> stati = new ArrayList<IGramarStatus>();
	
	private XPath xpath = null;
	
	private int modelAccess = 0;
	
	private int minLogLevel = IGramarStatus.SEVERITY_INFO;
	
	/**
	 * A list of characters whose presence in an XPath expression indicates that the expression
	 * is more than just a simple variable reference
	 */
	private String xpathChars = "./[]";
	
	public GramarContext(IGramarPlatform platform, Document model) {
		this.platform = platform;
		this.primaryModel = model;
	}
	
	public GramarContext(GramarContext parentContext) {
		this.parentContext = parentContext;
	}

	public Document getPrimaryModel() {
		return primaryModel;
	}

	public void setPrimaryModel(Document primaryModel) {
		this.primaryModel = primaryModel;
	}

	@Override
	public Object getVariable(String name) {
		modelAccess++;
		Object result = variables.get(name);
		if (result != null) {
			return result;
		}
		
		if (parentContext != null) {
			return parentContext.getVariable(name);
		}
		
		return null;
	}

	@Override
	public void setVariable(String variableName, Object value) {
		if (variableName == null) { return; }
		variables.put(variableName, value);
		debug("set variable "+variableName+" = "+String.valueOf(value));
	}

	@Override
	public void unsetVariable(String variableName) {
		if (variableName == null) { return; }
		variables.remove(variableName);
		debug("unset variable "+variableName);
	}

	@Override
	public void setAttribute(String nodeExpression, String attrName, String value) throws XPathExpressionException {
		Node node = resolveToNode(nodeExpression);
		ModelAccess.getDefault().setAttribute(node, attrName, value);
	}

	@Override
	public String resolveExpressions(String pattern) throws XPathExpressionException {

		if (pattern == null) { return null; }
		
		StringTokenizer st = new StringTokenizer(pattern, "{}", true);
		ArrayList<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}
		
		boolean reduced = true;
		while (reduced) {
			reduced = false;
			for (int t = 1; t < tokens.size()-1; t++) {
				if (tokens.get(t-1).equals("{") & tokens.get(t+1).equals("}")) {
					String result = resolveToString(tokens.get(t));
					tokens.remove(t-1);
					tokens.remove(t-1);
					tokens.remove(t-1);
					tokens.add(t-1, result);
					reduced = true;
				}
			}
		}
		
		StringBuffer sb = new StringBuffer();
		for (String token: tokens) {
			sb.append(token);
		}
		
		return sb.toString();
		
	}

	@Override
	public String resolveToString(String expression) throws XPathExpressionException {
		return resolveToString(expression, primaryModel);
	}

	@Override
	public String resolveToString(String expression, Node sourceNode) throws XPathExpressionException {
		Object obj = ModelAccess.getDefault().resolve(sourceNode, expression, this, XPathConstants.STRING);
		return (String) obj;
	}

	@Override
	public Node resolveToNode(String expression) throws XPathExpressionException {
		return resolveToNode(expression, primaryModel);
	}

	@Override
	public Node resolveToNode(String expression, Node sourceNode) throws XPathExpressionException {
		return ModelAccess.getDefault().getNode(sourceNode, expression, true, this);
	}

	@Override
	public Node[] resolveToNodes(String expression) throws XPathExpressionException {
		return resolveToNodes(expression, primaryModel);
	}

	@Override
	public Node[] resolveToNodes(String expression, Node sourceNode) throws XPathExpressionException {
		return ModelAccess.getDefault().getNodes(sourceNode, expression, true, this);
	}

	@Override
	public boolean resolveToBoolean(String expression) throws XPathExpressionException {
		return resolveToBoolean(expression, primaryModel);
	}

	@Override
	public boolean resolveToBoolean(String expression, Node sourceNode) throws XPathExpressionException {
		return (Boolean) ModelAccess.getDefault().resolve(sourceNode, expression, this, XPathConstants.BOOLEAN);
	}

	@Override
	public double resolveToNumber(String expression) throws XPathExpressionException {
		return resolveToNumber(expression, primaryModel);
	}

	@Override
	public double resolveToNumber(String expression, Node sourceNode) throws XPathExpressionException {
		return (Double) ModelAccess.getDefault().resolve(sourceNode, expression, this, XPathConstants.NUMBER);
	}

	@Override
	public Object resolveToObject(String expression) throws XPathExpressionException {

		return resolveToObject(expression, primaryModel);
	
	}

	@Override
	public Object resolveToObject(String expression, Node sourceNode) throws XPathExpressionException {

		// If the expression is a sinple variable reference ('$' followed by a single variable name)
		// then just return the value of that variable.  Some versions of XPath have issues dealing
		// with a single variable reference, so we'll just bypass for that simple case.
		if (singleVariableReference(expression)) {
			String name = expression.trim().substring(1);
			return getVariable(name);
		}
		
		try {
			Node[] node = resolveToNodes(expression, sourceNode);
			if (node.length > 0) {
				return node[0];
			} else {

			}
		} catch (Exception e) {
			// Guess it wasn't a node. Ignore exception and try another return type
		}
		
		try {
			Double result = resolveToNumber(expression, sourceNode);
			if (!Double.isNaN(result)) {
				return result;
			}
		} catch (Exception e) {
			// Guess it wasn't a number. Ignore exception and try another return type
		}

		return resolveToString(expression, sourceNode);
	
	}

	/**
	 * Answer whether the given expression is a single variable reference which is
	 * a '$' char folloed by a single variable name
	 * 
	 * @param expression
	 * @return whether the expression is a single variable reference
	 */
	protected boolean singleVariableReference(String expression) {
		String buf = expression.trim();
		if (buf.length() < 2) { return false; }
		if (buf.charAt(0) != '$') { return false; }
		String name = buf.substring(1);
		if (new StringTokenizer(name,xpathChars).countTokens() > 1) { return false; }
		return true;
	}

	@Override
	public void addModel(String name, Document model) {
		secondaryModels.put(name, model);
		setVariable(name,model);
	}

	public void setFileStore(IFileStore fileStore) {
		this.fileStore = fileStore;
	}
	
	@Override
	public IFileStore getFileStore() {
		if (fileStore == null) {
			fileStore = getPlatform().getDefaultFileStore();
		}
		return fileStore;
	}

	@Override
	public DefinedTag getTagDef(String namespace, String tagName) throws GramarException {
		
		ITemplatingExtension extension = extensionForNamespace(namespace);
		return extension.getTagDef(tagName);
	}

	@Override
	public ITagHandler getTagHandler(String namespace, String tagName) throws GramarException {
		
		ITemplatingExtension extension = extensionForNamespace(namespace);
		return extension.getCustomTagHandler(tagName);
	}

	@Override
	public XPathFunction getXPathFunction(String namespace, String name, int arity) throws GramarException {
		ITemplatingExtension extension = extensionForNamespace(namespace);
		XPathFunction function = extension.getFunction(name, arity);
		if (function != null) {
			return function;
		}
		throw new NoSuchXPathFunctionException();
	}

	private ITemplatingExtension extensionForNamespace(String namespace) throws NamespaceNotDefinedException, NoSuchTemplatingExtensionException, InvalidTemplateExtensionException {
		String extensionId = extensionIdForNamespace(namespace);
		return getPlatform().getTemplatingExtension(extensionId);
	}

	public String extensionIdForNamespace(String namespace) throws NamespaceNotDefinedException {
		if (extensions.containsKey(namespace)) {
			return extensions.get(namespace);
		}
		if (parentContext != null) {
			return parentContext.extensionIdForNamespace(namespace);
		}
		throw new NamespaceNotDefinedException();
	}

	@Override
	public IGramarPlatform getPlatform() {
		if (platform == null) {
			return parentContext.getPlatform();
		}
		return platform;
	}

	@Override
	public void declareTemplatingExtension(String namespace, String extensionId) {
		extensions.put(namespace, extensionId);
		
	}

	@Override
	public boolean isExtensionDefined(String extensionId) {
		return extensions.containsValue(extensionId);
	}

	@Override
	public void setGramar(IGramar pattern) {
		this.gramar = pattern;
	}
	
	@Override
	public IGramar getPattern() {
		return gramar;
	}

	@Override
	public void warning(Exception e) {
		stati.add(GramarStatus.warning(e));
		log(e.getMessage(), IGramarStatus.SEVERITY_WARN);
	}

	@Override
	public void error(Exception e) {
		stati.add(GramarStatus.error(e));
		log(e.getMessage(), IGramarStatus.SEVERITY_ERROR);
	}

	@Override
	public void error(String message) {
		stati.add(GramarStatus.error(message));
		log(message, IGramarStatus.SEVERITY_ERROR);
	}

	@Override
	public void info(String message) {
		stati.add(GramarStatus.info(message));
		log(message, IGramarStatus.SEVERITY_INFO);
	}

	@Override
	public void debug(String message) {
		stati.add(GramarStatus.debug(message));
		log(message, IGramarStatus.SEVERITY_DEBUG);
	}

	@Override
	public void warning(String message) {
		stati.add(GramarStatus.warning(message));
		log(message, IGramarStatus.SEVERITY_WARN);
	}

	public void log(String message, int severity) {
		if ((minLogLevel <= severity) && (getFileStore() != null)) {
			getFileStore().log(message);
		}
	}

	@Override
	public XPath getXPath() {
		modelAccess++;
		if (xpath == null) {
			XPathFactory factory = XPathFactory.newInstance();
			xpath = factory.newXPath();
			xpath.setXPathFunctionResolver(new XPathResolver(this));
			xpath.setXPathVariableResolver(new XPathResolver(this));
		}
		return xpath;
	}

	@Override
	public int getModelAccessCount() {
		return modelAccess;
	}

	@Override
	public void free() {
		if (parentContext != null) { parentContext.free(); }
		parentContext = null;
		
		primaryModel = null;
		secondaryModels = new HashMap<String, Document>();
		variables = new HashMap<String, Object>();
		extensions = new HashMap<String, String>();
		
		if (fileStore != null) {
			fileStore.free();
		}
		fileStore = null;
		platform = null;
		
		if (gramar != null) {
			gramar.free();
		}
		gramar = null;
		
		stati = new ArrayList<IGramarStatus>();
		xpath = null;
		modelAccess = 0;
		
	}

	@Override
	public int getMaxStatus() {
		int status = 0;
		for (IGramarStatus stat: stati) {
			if (stat.getSeverity() > status) {
				status = stat.getSeverity();
			}
		}
		return status;
	}

	@Override
	public IGramar getGramar() {
		return gramar;
	}

	/**
	 * Answer a bare-bones context useful for testing
	 * 
	 * @return a minimally functional gramar context
	 */
	public static IGramarContext dummy() {
		return new GramarContext(new SimpleGramarPlatform(), XmlModel.emptyModel().asDOM());
	}
	
	
}
