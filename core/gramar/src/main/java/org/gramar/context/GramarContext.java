package org.gramar.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.gramar.ICustomTagHandler;
import org.gramar.IFileStore;
import org.gramar.IGramar;
import org.gramar.IGramarContext;
import org.gramar.IGramarPlatform;
import org.gramar.ITemplatingExtension;
import org.gramar.exception.GramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NamespaceNotDefinedException;
import org.gramar.exception.NoSuchCustomTagException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.exception.TemplatingExtensionNotDefinedException;
import org.gramar.model.ModelAccess;
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
	
	private IGramar pattern;
	
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
	public void setVariable(String name, Object value) {
		variables.put(name,value);
	}

	@Override
	public void setAttribute(String nodeExpression, String attrName, String value) throws XPathExpressionException {
		Node node = resolveToNode(nodeExpression);
		ModelAccess.getDefault().setAttribute(node, attrName, value);
	}

	@Override
	public String resolveExpressions(String pattern) throws XPathExpressionException {

		StringTokenizer st = new StringTokenizer(pattern, "{}", true);
		ArrayList<String> tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			tokens.add(st.nextToken());
		}
		
		boolean reduced = true;
		while (reduced) {
			reduced = false;
			for (int t = 1; t < tokens.size()-2; t++) {
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
		return (String) ModelAccess.getDefault().resolve(primaryModel, expression, this, XPathConstants.STRING);
	}

	@Override
	public Node resolveToNode(String expression) throws XPathExpressionException {
		Node node[] = ModelAccess.getDefault().getNodes(primaryModel, expression, true, this);
		if (node.length == 0) {
			return null;
		}
		return node[0];
	}

	@Override
	public Node[] resolveToNodes(String expression) throws XPathExpressionException {
		return ModelAccess.getDefault().getNodes(primaryModel, expression, true, this);
	}

	@Override
	public boolean resolveToBoolean(String expression) throws XPathExpressionException {
		return (Boolean) ModelAccess.getDefault().resolve(primaryModel, expression, this, XPathConstants.BOOLEAN);
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
	public ICustomTagHandler getCustomTagHandler(String namespace, String tagName) throws GramarException {
		
		ITemplatingExtension extension = extensionForNamespace(namespace);
		return extension.getCustomTagHandler(tagName);
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
	public void setPattern(IGramar pattern) {
		this.pattern = pattern;
	}
	
	@Override
	public IGramar getPattern() {
		return pattern;
	}

	@Override
	public void warning(Exception e) {
		
	}

	@Override
	public void error(Exception e) {
		
	}
	

}
