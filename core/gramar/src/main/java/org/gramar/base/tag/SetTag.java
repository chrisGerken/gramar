package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Element;

public class SetTag extends TagHandler implements ITagHandler {

	public SetTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		String select = getAttributes().get("select");
		String name = getAttributes().get("name");
		
		try {

			MergeStream newStream = processChildren(context);
			String content = newStream.toString();
			
			Element element = (Element) context.resolveToNode(select);
			if (element != null) {
				element.setAttribute(name, content);
			}
			
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "set";
	}

}
