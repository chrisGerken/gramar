package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Node;

public class IfTag extends TagHandler implements ITagHandler {

	public IfTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		
		try {
			String test = getRawAttribute("test");
			String var = getStringAttribute("var", context, "");

			Object prevValue = null;
			if (var.length()>0) {
				prevValue = context.getVariable(var);
			}

			boolean result = context.resolveToBoolean(test);
			
			if (result) {
				
				Node node = null;
				try { node = context.resolveToNode(test); } catch (Throwable t) { }
				if (node != null) {
					context.setVariable(var, node);
				}
				
				processChildren(stream, context);
				
				if (node != null) {
					context.unsetVariable(var);
				}
			}
			
			if (var.length()>0) {
				if (prevValue == null) {
					context.unsetVariable(var);
				} else {
					context.setVariable(var, prevValue);
				}
			}

		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}

	}

	@Override
	public String getTagName() {
		return "if";
	}

}
