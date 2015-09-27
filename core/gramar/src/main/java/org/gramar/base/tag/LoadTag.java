package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.IModel;
import org.gramar.ITagHandler;
import org.gramar.model.XmlModel;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;

public class LoadTag extends TagHandler implements ITagHandler {

	public LoadTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String name = getStringAttribute("var", context);
			String loader = getStringAttribute("loader", context, null);
			String type = getStringAttribute("type", context, null);

			MergeStream newStream = processChildren(context);
			String content = newStream.toString();
			
			IModel inline = new XmlModel(content);
			context.setVariable(name, inline.asDOM());
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "loadContent";
	}

}
