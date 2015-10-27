package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AddElementTag extends TagHandler implements ITagHandler {

	public AddElementTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		
		try {

			Node node = getNodeAttribute("select", context);
			String name = getStringAttribute("name", context);
			String var = getStringAttribute("var", context, null);

			Element element = node.getOwnerDocument().createElement(name);
			node.appendChild(element);
			if (var != null) {
				context.setVariable(var, element);
			}

		} catch (Exception e) {
			context.error(e.getMessage());
			logStackTrace(context);
		}

	}

	@Override
	public String getTagName() {
		return "addElement";
	}

}
