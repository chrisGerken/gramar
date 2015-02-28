package org.gramar.model;

import java.io.InputStream;

import org.gramar.IModel;
import org.w3c.dom.Document;


public class XmlModel implements IModel {

	private Document document;
	
	public XmlModel(String source) throws Exception {
		document = DocumentHelper.buildModel(source);
	}
	
	public XmlModel(InputStream is) throws Exception {
		document = DocumentHelper.buildModel(is);
	}

	@Override
	public Document asDOM() {
		return document;
	}

}
