package org.gramar.base.tag;

import javax.xml.xpath.XPathExpressionException;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Node;

public class IfTag extends TagHandler implements ITagHandler {

	public IfTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		
		try {
			String test = getAttributes().get("test");
			String var = getAttributes().get("var");

			boolean result = context.resolveToBoolean(test);
			
			if (result) {
				
				Node node = null;
				try { node = context.resolveToNode(test); } catch (Throwable t) { }
				if (node != null) {
					context.setVariable(var, node);
				}
				
				processChildren(stream, context);
				
				if (node != null) {
					context.unsetVariable(var);
				}
			}
		} catch (XPathExpressionException e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "if";
	}

}
