package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;

public class SetVariableTag extends TagHandler implements ITagHandler {

	public SetVariableTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		String select = getAttributes().get("select");
		String var = getAttributes().get("var");
		
		try {

			Object result = context.resolveToObject(select);

			if (result != null) {
				context.setVariable(var, result);
			} else {
				context.unsetVariable(var);
			}
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "setVariable";
	}

}
