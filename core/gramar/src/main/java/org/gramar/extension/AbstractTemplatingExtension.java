package org.gramar.extension;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.xpath.XPathFunction;

import org.gramar.ICustomTagHandler;
import org.gramar.ITemplatingExtension;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NoSuchCustomTagException;
import org.gramar.gramar.TagLibSpec;
import org.gramar.model.DocumentHelper;
import org.gramar.model.ModelAccess;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public abstract class AbstractTemplatingExtension implements ITemplatingExtension {

	/*
	 * The extension ID in the form of a java package name.
	 */
	protected String extensionId;

	/*
	 * The default abbreviation (namespace) to use when referencing specific
	 * custom tags and xpath functions in this platform extension
	 */
	protected String abbreviation;
	
	protected String label;
	
	protected String provider;
	
	/*
	 * A cache of XPath functions defined in this extension.  The functions are 
	 * wrapped as a DefinedFunction which has a reference to the implementation
	 * of the function as well as metadata about the function, including arity,
	 * whether the function has a variable number of arguments and the name of
	 * the defined XPath function.
	 */
	protected ArrayList<DefinedFunction> functions = new ArrayList<DefinedFunction>();
	
	/*
	 * A cache of custom tag handlers defined in this extension, keyed by the name
	 * of the tag each enables
	 */
	private HashMap<String,DefinedTag> tags = new HashMap<String,DefinedTag>();
	 
	public static final String META_FILE_NAME = "extension.config";
	
	public AbstractTemplatingExtension(String id, ArrayList<DefinedFunction> functions) {
		this.extensionId = id;
		this.functions = functions;
	}
	
	public AbstractTemplatingExtension(String extensionID) throws InvalidTemplateExtensionException {
		this.extensionId = extensionID;
		loadMeta();
	}

	public void loadMeta() throws InvalidTemplateExtensionException {
		try {
			
			String source = getConfig();
			Document doc = DocumentHelper.buildModel(source);
			
			extensionId = ModelAccess.getDefault().getText(doc, "/extension/id");
			label = ModelAccess.getDefault().getText(doc, "/extension/label");
			provider = ModelAccess.getDefault().getText(doc, "/extension/provider");
			
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			
			Node[] node = ModelAccess.getDefault().getNodes(doc, "/extension/tags/tag", true, null);
			for (Node n : node) {
				String name = ModelAccess.getDefault().getAttribute(n, "@name");
				String fqn = ModelAccess.getDefault().getAttribute(n, "@handler");
				String control = ModelAccess.getDefault().getAttribute(n, "@controlTag");
				boolean controlTag = Boolean.parseBoolean(control);
				tags.put(name,new DefinedTag(name, fqn, controlTag, extensionId));
			}
			
		} catch (Exception e) {
			throw new InvalidTemplateExtensionException(e);
		}
	}

	protected abstract String getConfig() throws IOException;

	public void addFunction(DefinedFunction definedFunction) {
		functions.add(definedFunction);
	}

	public XPathFunction getFunction(String name, int arity) {
		for (DefinedFunction df : functions) {
			if (df.matches(name, arity)) {
				return df.getFunction();
			}
		}
		return null;
	}

	public void setDefaultAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}
	
	@Override
	public String getDefaultAbbreviation() {
		return abbreviation;
	}

	public void setExtensionId(String id) {
		this.extensionId = id;
	}
	
	@Override
	public String getExtensionId() {
		return extensionId;
	}

	@Override
	public ICustomTagHandler getCustomTagHandler(String tagName) throws NoSuchCustomTagException {
		DefinedTag dt = tags.get(tagName);
		if (dt == null) {
			throw new NoSuchCustomTagException();
		}
		ClassLoader loader = ClassLoader.getSystemClassLoader();
		ICustomTagHandler handler;
		try {
			handler = (ICustomTagHandler) loader.loadClass(dt.getFQClassName()).newInstance();
		} catch (Exception e) {
			throw new NoSuchCustomTagException(e);
		}
		return handler;
	}

	@Override
	public boolean hasCustomTagHandler(String tagName) {
		return tags.containsKey(tagName);
	}
	
}
