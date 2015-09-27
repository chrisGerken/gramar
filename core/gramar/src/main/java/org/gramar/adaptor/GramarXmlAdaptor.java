package org.gramar.adaptor;

import org.gramar.IModelAdaptor;
import org.gramar.exception.GramarException;
import org.gramar.model.DocumentHelper;
import org.w3c.dom.Document;

/**
 * A class that converts XML documents in string form/representation into XML DOM's
 * 
 * @author chrisgerken
 *
 */
public class GramarXmlAdaptor extends GramarModelAdaptor implements IModelAdaptor {

	public GramarXmlAdaptor() {
	}

	@Override
	public String getId() {
		return "org.gramar.adaptor.GramarXmlAdaptor";
	}

	@Override
	public String getType() {
		return "xml";
	}

	@Override
	public Document asDocument(String source) throws GramarException {
		return DocumentHelper.buildModel(source);
	}

}
