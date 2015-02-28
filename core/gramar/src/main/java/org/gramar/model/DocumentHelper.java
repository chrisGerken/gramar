package org.gramar.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.gramar.IModel;
import org.w3c.dom.Document;


public class DocumentHelper {

	public DocumentHelper() {

	}

	public static InputStream load(String resource) {
		return DocumentHelper.class.getResourceAsStream(resource);
	}
	
	public static Document loadDocument(String resource) throws Exception {
		return buildModel(load(resource));
	}
	
	public static String loadString(String resource) throws Exception {
		InputStream is = load(resource);
		byte b[] = new byte[2000];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len = is.read(b);
		while (len > -1) {
			baos.write(b, 0, len);
			len = is.read(b);
		}
		is.close();
		return baos.toString();
	}
	

	public static Document buildModel(String content) throws Exception {
		return buildModel(new ByteArrayInputStream(content.getBytes()));
	}

	public static Document buildModel(InputStream is) throws Exception {
		DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = fact.newDocumentBuilder();
		Document newDoc = parser.parse(is);
		newDoc.normalize();
		is.close();
		return newDoc;
	}
	
	public static IModel modelFromResource(String resource) throws Exception {
		InputStream is = load(resource);
		XmlModel model = new XmlModel(is);
		return model;
	}

}
