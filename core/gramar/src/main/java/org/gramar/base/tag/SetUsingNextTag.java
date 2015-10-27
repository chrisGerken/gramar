package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SetUsingNextTag extends TagHandler implements ITagHandler {

	public SetUsingNextTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		
		try {

			String select = getRawAttribute("select");
			String name = getStringAttribute("name", context);
			String var = getStringAttribute("var", context);
			String last = getStringAttribute("last", context);

			Node[] node = context.resolveToNodes(select);

			for (int i = 0; i < (node.length - 1); i++) {
				
				Node n = node[i+1];
				
				context.setVariable(var, n);
				String value = getStringAttribute("value", context);
				
				Element element = (Element) node[i];
				element.setAttribute(name, value);

			}

			context.unsetVariable(var);
				
			if (node.length > 0) {
				Element element = (Element) node[node.length-1];
				element.setAttribute(name, last);
			}

		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}

	}

	@Override
	public String getTagName() {
		return "setUsingNext";
	}

}
