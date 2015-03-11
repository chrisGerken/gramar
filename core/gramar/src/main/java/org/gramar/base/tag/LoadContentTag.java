package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.IModel;
import org.gramar.ITagHandler;
import org.gramar.model.XmlModel;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;

public class LoadContentTag extends TagHandler implements ITagHandler {

	public LoadContentTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String name = getStringAttribute("var", context);

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
