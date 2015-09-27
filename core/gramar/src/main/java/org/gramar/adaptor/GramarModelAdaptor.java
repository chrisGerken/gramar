package org.gramar.adaptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.gramar.IModelAdaptor;
import org.gramar.exception.GramarException;
import org.gramar.util.GramarHelper;
import org.w3c.dom.Document;

public abstract class GramarModelAdaptor implements IModelAdaptor {

	public GramarModelAdaptor() {

	}

	@Override
	public Document asDocument(Properties properties) throws GramarException {
		throw new GramarException("This adaptor does not support the properties-defined conversion ");
	}

	@Override
	public Document asDocument(String source) throws GramarException {
		throw new GramarException("This adaptor does not support the conversion of text");
	}

	@Override
	public Document asDocument(InputStream stream) throws GramarException {
		try {
			return asDocument(GramarHelper.asString(stream));
		} catch (IOException e) {
			throw new GramarException(e);
		}
	}

	public boolean adaptsType(String type) {
		return getType().equalsIgnoreCase(type);
	}

	
}
