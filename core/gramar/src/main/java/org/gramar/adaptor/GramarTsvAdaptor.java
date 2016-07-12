package org.gramar.adaptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.gramar.IModelAdaptor;
import org.gramar.exception.GramarException;
import org.gramar.model.DocumentHelper;
import org.gramar.model.XmlModel;
import org.w3c.dom.Document;

public class GramarTsvAdaptor extends GramarModelAdaptor implements
		IModelAdaptor {

	public GramarTsvAdaptor() {

	}

	@Override
	public String getId() {
		return "org.gramar.adaptor.GramarTsvAdaptor";
	}

	@Override
	public String getType() {
		return "tsv";
	}

	@Override
	public Document asDocument(String source) throws GramarException {

		String line[] = source.split("\n");
		String xml = null;
		
		int linenum = 0;
		while ((linenum<line.length)&&((line[linenum]==null)||(line[linenum].trim().length()==0))) {
			linenum++;
		}
		
		if (line.length<=linenum) {
			return XmlModel.emptyModel().asDOM();
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<root>");
		String hdr[] = line[linenum].split("\t");
		linenum++;
		
		while (linenum<line.length) {
			String col[] = line[linenum].split("\t");
			sb.append("<row");
			for (int i = 0; i < Math.min(col.length,hdr.length); i++) {
				String h = hdr[i];
				String c = escape(col[i]);
				sb.append("  "+h+"=\""+c+"\"");  
			}
			sb.append("/>");
			linenum++;
		}
		
		sb.append("</root>");
		xml = sb.toString();
		
		return DocumentHelper.buildModel(xml);
	}

	private String escape(String buf) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length(); i++) {
			char c = buf.charAt(i);
			if (c == '<') {
				sb.append("&lt;");
			} else if (c == '>') {
				sb.append("&gt;");
			} else if (c == '&') {
				sb.append("&amp;");
			} else if (c == '\'') {
				sb.append("&apos;");
			} else if (c == '\"') {
				sb.append("&quot;");
			} else if (Character.isWhitespace(c)) {
				sb.append(c);
			} else if (c < 20) {
				sb.append("");
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
