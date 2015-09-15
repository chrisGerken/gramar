package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.model.DocumentHelper;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Node;

public class DumpTag extends TagHandler implements ITagHandler {

	public DumpTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {

			Node node = getNodeAttribute("select", context);
			
			stream.append(DocumentHelper.asString(node));
		
		} catch (Exception e) {
			context.error(e);
		}
		
	}

	@Override
	public String getTagName() {
		return "dump";
	}

}
