package org.gramar.base.tag;

import java.io.IOException;

import javax.xml.xpath.XPathExpressionException;

import org.gramar.ICustomTagHandler;
import org.gramar.IGramarContext;
import org.gramar.filestore.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Node;

public class IterateTag extends TagHandler implements ICustomTagHandler {

	public IterateTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		String select = getAttributes().get("select");
		String var = getAttributes().get("var");
		String delim = getAttributes().get("delim");
		if (delim == null) {
			delim = "";
		}
		
		try {
			boolean first = true;
			Node[] node = context.resolveToNodes(select);

			for (Node n: node) {
				if (!first) {
					stream.append(delim);
				}
				first = false;
				
				context.setVariable(var, n);
				
				processChildren(stream, context);
				
				context.unsetVariable(var);
			}
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "iterate";
	}

}
