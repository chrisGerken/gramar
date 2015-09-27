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
		String hdr[] = line[linenum].split(",");
		linenum++;
		
		while (linenum<line.length) {
			String col[] = line[linenum].split("\t");
			sb.append("<row");
			for (int i = 0; i < col.length; i++) {
				sb.append("  "+hdr[i]+"=\""+col[i]+"\"");
			}
			sb.append("/>");
			linenum++;
		}
		
		sb.append("</root>");
		xml = sb.toString();
		
		return DocumentHelper.buildModel(xml);
	}

}
