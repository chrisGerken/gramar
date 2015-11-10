package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.model.XmlModel;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class NewModelTag extends TagHandler implements ITagHandler {

	public NewModelTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String var = getStringAttribute("var", context);
			String name = getStringAttribute("name", context);

			String content = "<"+name+"/>";
			Document doc = new XmlModel(content).asDOM();

			Node root = doc.getFirstChild();
			
			context.setVariable(var, root);
			
		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}

	}

	@Override
	public String getTagName() {
		return "loadContent";
	}

}
