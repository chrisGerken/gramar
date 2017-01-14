package com.gerken.xaa.mpe.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IFileEditorInput;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ModelLoader {

	private static ModelLoader instance = null;
	
	private ModelLoader() {
	}

	public static ModelLoader getInstance() {
		if (instance == null) {
			instance = new ModelLoader();
		}
		return instance;
	}

	public Document buildModel(String content) throws Exception {
		return buildModel(new ByteArrayInputStream(content.getBytes()));
	}

	public Document buildModel(InputStream is) throws Exception {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = fact.newDocumentBuilder();
		Document newDoc = parser.parse(is);
		newDoc.normalize();
		return newDoc;
	}

	public Document buildEmptyModel() {
		try { return buildModel("<xform></xform>"); }
		catch (Throwable t) { return null; }
	}
	
}
