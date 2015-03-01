package org.gramar.base.tag;

import org.gramar.ICustomTagHandler;
import org.gramar.IGramarContext;
import org.gramar.filestore.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class AddElementTag extends TagHandler implements ICustomTagHandler {

	public AddElementTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		String select = getAttributes().get("select");
		String name = getAttributes().get("name");
		String var = getAttributes().get("var");
		
		try {
			Node node = context.resolveToNode(select);
			if (node != null) {
				Element element = node.getOwnerDocument().createElement(name);
				node.appendChild(element);
				if (var != null) {
					context.setVariable(var, element);
				}
			}
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "addElement";
	}

}
