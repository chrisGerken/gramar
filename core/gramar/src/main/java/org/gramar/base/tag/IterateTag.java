package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Node;

public class IterateTag extends TagHandler implements ITagHandler {

	public IterateTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		
		try {

			String select = getRawAttribute("select");
			String var = getStringAttribute("var", context);
			String delim = getStringAttribute("delimiter", context, "");

			boolean first = true;
			Node[] node = context.resolveToNodes(select);

			for (Node n: node) {
				if (!first) {
					stream.append(delim);
				}
				first = false;
				
				context.setVariable(var, n);
				
				processChildren(stream, context);
				
				context.unsetVariable(var);
			}
		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}

	}

	@Override
	public String getTagName() {
		return "iterate";
	}

}
