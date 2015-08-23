package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Node;

public class RemoveElementTag extends TagHandler implements ITagHandler {

	public RemoveElementTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		
		try {

			Node node = getNodeAttribute("select", context);

			node.getParentNode().removeChild(node);

		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "removeElement";
	}

}
