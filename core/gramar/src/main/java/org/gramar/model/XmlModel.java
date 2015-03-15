package org.gramar.model;

import java.io.InputStream;

import org.gramar.IModel;
import org.w3c.dom.Document;


public class XmlModel implements IModel {

	private Document document;
	
	/**
	 * Construct an IModel from the XML content in the source string.
	 * 
	 * @param source
	 * @throws Exception
	 */
	public XmlModel(String source) throws Exception {
		document = DocumentHelper.buildModel(source);
	}
	
	/**
	 * Construct an IModel from the XML content in the input stream.
	 * 
	 * @param is
	 * @throws Exception
	 */
	public XmlModel(InputStream is) throws Exception {
		document = DocumentHelper.buildModel(is);
	}

	@Override
	public Document asDOM() {
		return document;
	}
	
	public static XmlModel emptyModel() {
		try {
			return new XmlModel("<root/>");
		} catch (Exception e) {

		}
		return null;
	}

}
