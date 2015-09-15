package org.gramar.model;

import java.io.InputStream;
import java.util.HashMap;

import org.gramar.IModel;
import org.gramar.exception.GramarException;
import org.w3c.dom.Document; 
import org.w3c.dom.Node;


public class XmlModel implements IModel {

	private Document document;
	
	private HashMap<String, Integer> xpaths = null;
	
	/**
	 * Construct an IModel from the XML content in the source string.
	 * 
	 * @param source
	 * @throws Exception
	 */
	public XmlModel(String source) throws GramarException {
		document = DocumentHelper.buildModel(source);
	}
	
	/**
	 * Construct an IModel from the XML content in the input stream.
	 * 
	 * @param is
	 * @throws Exception
	 */
	public XmlModel(InputStream is) throws GramarException {
		document = DocumentHelper.buildModel(is);
	}

	@Override
	public Document asDOM() {
		return document;
	}
	
	/**
	 * Constructs and returns an empty XML model
	 * 
	 * @return a model with a single root element with no attributes or content.
	 */
	public static XmlModel emptyModel() {
		try {
			return new XmlModel("<root/>");
		} catch (Exception e) {

		}
		return null;
	}

	@Override
	public HashMap<String, Integer> getXpaths() throws Exception {
		if (xpaths != null) {
			return xpaths;
		}
		Document doc = asDOM();
		HashMap<String, Integer> map = new HashMap<String,Integer>();
		addPaths("",doc,map);
		xpaths = map;
		return map;
	}

	private void addPaths(String relpath, Node current, HashMap<String, Integer> map) throws Exception {
		// Get all child nodes of the current node
		Node[] child = ModelAccess.getDefault().getNodes(current, "*", true, null);
		for (Node n: child) {
			String name = n.getNodeName();
			String path = relpath + "/" + name;
			if (!map.containsKey(path)) {
				map.put(path, new Integer(0));
			}
			int count = map.get(path);
			map.put(path, (count+1));
			addPaths(path, n, map);
		}
	}

}
