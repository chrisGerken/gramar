package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.filestore.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class AddTextElementTag extends TagHandler implements ITagHandler {

	public AddTextElementTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		String select = getAttributes().get("select");
		String name = getAttributes().get("name");
		
		try {

			MergeStream newStream = processChildren(context);
			String content = newStream.toString();
			
			Node node = context.resolveToNode(select);
			if (node != null) {
				Element element = node.getOwnerDocument().createElement(name);
				node.appendChild(element);
				Text text = node.getOwnerDocument().createTextNode(content);
				element.appendChild(text);
			}
			
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "addTextElement";
	}

}
