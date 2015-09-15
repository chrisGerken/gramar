package org.gramar.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.util.StreamReaderDelegate;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.gramar.IModel;
import org.gramar.exception.GramarException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


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
	

	public static Document buildModel(String content) throws GramarException {
		return buildModel(new ByteArrayInputStream(content.getBytes()));
	}

	public static Document buildModel(InputStream is) throws GramarException {
		try {
			DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
			DocumentBuilder parser = fact.newDocumentBuilder();
			Document newDoc = parser.parse(is);
			newDoc.normalize();
			is.close();
			return newDoc;
		} catch (Exception e) {
			throw new GramarException(e);
		}
	}
	
	/**
	 * Converts a TSV file into the XML equivalent of that data.  
	 * 
	 * For example, the TSV:
	 * 
	 *    First<tab>Last<tab>Age
	 *    Fred<tab>Fredrickson<tab>93
	 *    Biff<tab>Bifford<tab>23
	 *    
	 * becomes the Document for:
	 * 
	 *    <root>
	 *    <row First="Fred" Last="Fredrison" Age="93" />
	 *    <row First="Biff" Last="Bifford" Age="23" />
	 *    </root>
	 *    
	 * @param is the text content of a tab-separated file
	 * @return a Document built from the string representation of the TSV data
	 * @throws GramarException 
	 */
	public static Document fromTsv(InputStream is) throws GramarException {

		String xml = null;
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String row = br.readLine();
			String hdr[] = row.split("\t");
			
			row = br.readLine();
			while (row!=null) {
				String col[] = row.split("\t");
				sb.append("<row");
				for (int i = 0; i < col.length; i++) {
					sb.append("  "+hdr[i]+"=\""+col[i]+"\"");
				}
				sb.append("/>");
				row = br.readLine();
			}
			is.close();
			br.close();
			
			sb.append("</root>");
			xml = sb.toString();
		} catch (IOException e) {
			throw new GramarException("Error during TSV-XML conversion", e);
		}
		
		return buildModel(xml);
	}
	
	/**
	 * Converts a CSV file into the XML equivalent of that data.  
	 * 
	 * For example, the CSV:
	 * 
	 *    First,Last,Age
	 *    Fred,Fredrickson,93
	 *    Biff,Bifford,23
	 *    
	 * becomes the Document for:
	 * 
	 *    <root>
	 *    <row First="Fred" Last="Fredrison" Age="93" />
	 *    <row First="Biff" Last="Bifford" Age="23" />
	 *    </root>
	 *    
	 * @param is the text content of a comma-separated file
	 * @return a Document built from the string representation of the CSV data
	 * @throws GramarException 
	 */
	public static Document fromCsv(InputStream is) throws GramarException {

		String xml = null;
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String row = br.readLine();
			String hdr[] = row.split(",");
			
			row = br.readLine();
			while (row!=null) {
				String col[] = row.split(",");
				sb.append("<row");
				for (int i = 0; i < col.length; i++) {
					sb.append("  "+hdr[i]+"=\""+col[i]+"\"");
				}
				sb.append("/>");
				row = br.readLine();
			}
			is.close();
			br.close();
			
			sb.append("</root>");
			xml = sb.toString();
		} catch (IOException e) {
			throw new GramarException("Error during CSV-XML conversion", e);
		}
		
		return buildModel(xml);
	}
	
	/**
	 * Format and return the string representation of the specified node and it's children
	 * 
	 * @param node the node to represent
	 * @return a string representation of the specified node
	 * @throws TransformerException 
	 */
	public static String asString(Node node) throws TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		DOMSource source = new DOMSource(node);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		transformer.transform(source, result);
		return writer.toString();
	}
	
	public static IModel modelFromResource(String resource) throws GramarException {
		try {
			InputStream is = load(resource);
			XmlModel model = new XmlModel(is);
			return model;
		} catch (Exception e) {
			throw new GramarException(e);
		}
	}

}
