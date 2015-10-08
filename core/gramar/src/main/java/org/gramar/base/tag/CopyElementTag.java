package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Node;

public class CopyElementTag extends TagHandler implements ITagHandler {

	public CopyElementTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		
		try {

			Node node = getNodeAttribute("select", context);
			Node parent = getNodeAttribute("toSelect", context);
			String name = getStringAttribute("name", context);
			String var = getStringAttribute("var", context, null);
			Boolean deep = getBooleanAttribute("recursive", context, true);

			Node copy = node.cloneNode(deep);
			parent.getOwnerDocument().adoptNode(copy);
			parent.appendChild(copy);

			if (var != null) {
				context.setVariable(var, copy);
			}

		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "copyElement";
	}

}
