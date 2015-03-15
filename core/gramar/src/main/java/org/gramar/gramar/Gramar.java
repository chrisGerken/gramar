package org.gramar.gramar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.gramar.IGramar;
import org.gramar.IGramarContext;
import org.gramar.ITemplate;
import org.gramar.ast.Template;
import org.gramar.exception.GramarException;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.exception.NoSuchTemplateException;
import org.gramar.model.DocumentHelper;
import org.gramar.model.ModelAccess;
import org.gramar.tag.TagDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public abstract class Gramar implements IGramar {

	@Override
	public void free() {
		cache = new HashMap<String, ITemplate>();
		sources = new HashMap<String, String>();
	}

	private Properties props;
	private HashMap<String, ITemplate> cache = new HashMap<String, ITemplate>();
	private HashMap<String, String> sources = new HashMap<String, String>();

	protected String gramarId;
	protected String label;
	protected String provider;
	protected String mainTemplate;
	protected ArrayList<TagLibSpec> taglibs = new ArrayList<TagLibSpec>();

	public static final String META_FILE_NAME = "gramar.config";
	
	public Gramar() throws InvalidGramarException {

	}

	@Override
	public String getId() {		
		return gramarId;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getProvider() {
		return provider;
	}

	@Override
	public String getMainTemplateId() {
		return mainTemplate;
	}
	
	public Properties getProperties() {
		return props;
	}

	@Override
	public ITemplate getTemplate(String id, IGramarContext context) throws GramarException {
		for (TagLibSpec spec: taglibs) {
			if (!context.isExtensionDefined(spec.getExtensionId())) {
				context.declareTemplatingExtension(spec.getNamespace(), spec.getExtensionId());
			}
		}
		if (!cache.containsKey(id)) {
			cache.put(id, extractTemplate(id, context));
		}
		return cache.get(id);
	}
	
	public void storeSource(String name, String content) {
		sources.put(name, content);
	}
	
	/*
	 * Get the source for a selected template and create the template from that source
	 */
	private ITemplate extractTemplate(String id, IGramarContext context) throws GramarException {
		try {
			String source = getTemplateSource(id); 
			TagDocument document = TagDocument.build(source,context);
			return new Template(document);
		} catch (NoSuchResourceException e) {
			throw new NoSuchTemplateException(e);
		} 
	}

	public String getTemplateSource(String id) throws NoSuchResourceException {
		if (sources.containsKey(id)) {
			return sources.get(id);
		}
		String source = readTemplateSource(id);
		sources.put(id, source);
		return source;
	}

	public abstract String readTemplateSource(String id) throws NoSuchResourceException;
	
	public void loadMeta() throws InvalidGramarException {
		try {
			
			String source = getTemplateSource(META_FILE_NAME);
			Document doc = DocumentHelper.buildModel(source);
			
			gramarId = ModelAccess.getDefault().getText(doc, "/gramar/id");
			label = ModelAccess.getDefault().getText(doc, "/gramar/label");
			provider = ModelAccess.getDefault().getText(doc, "/gramar/provider");
			mainTemplate = ModelAccess.getDefault().getText(doc, "/gramar/main");
			
			Node[] node = ModelAccess.getDefault().getNodes(doc, "/gramar/taglibs/taglib", true, null);
			for (Node n : node) {
				String prefix = ModelAccess.getDefault().getAttribute(n, "@prefix");
				String extension = ModelAccess.getDefault().getAttribute(n, "@extension");
				taglibs.add(new TagLibSpec(extension, prefix));
			}
			
		} catch (Exception e) {
			throw new InvalidGramarException(e);
		}
	}
	 
		public ArrayList<TagLibSpec> getTaglibs() {
			return taglibs;
		}

}
