package org.gramar.base.tag;

import java.util.StringTokenizer;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.ITemplate;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;

public class IncludeTag extends TagHandler implements ITagHandler {

	public IncludeTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String templateId = getStringAttribute("template", context); 
			String restore = getStringAttribute("restore", context, "");
			
			StringTokenizer st = new StringTokenizer(restore, " ,");
			
			String name[] = new String[st.countTokens()];
			Object value[] = new Object[st.countTokens()];

			int i = 0;
			while (st.hasMoreTokens()) {
				String var = st.nextToken();
				name[i] = var;
				value[i] = context.getVariable(var);
				i++;
			}
			
			ITemplate template = context.getPattern().getTemplate(templateId, context);
			template.mergeTo(stream, context);
			
			for (i=0; i < name.length; i++) {
				if (value[i] == null) {
					context.unsetVariable(name[i]);
				} else {
					context.setVariable(name[i], value[i]);
				}
			}
		} catch (Exception e) {
			context.error(e);
		}
	}

	@Override
	public String getTagName() {
		return "include";
	}

}
