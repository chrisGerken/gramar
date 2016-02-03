package org.gramar.base.tag;

import java.util.StringTokenizer;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.ITemplate;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.gramar.util.GramarHelper;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

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
				context.debug("store "+name[i]+" = "+GramarHelper.display(value[i]));
				i++;
			}
			
			ITemplate template = context.getGramar().getTemplate(templateId, context);
			context.debug("begin include "+templateId);
			template.mergeTo(stream, context);
			context.debug("end include "+templateId);
			
			for (i=0; i < name.length; i++) {
				context.debug("restore "+name[i]+" = "+GramarHelper.display(value[i]));
				if (value[i] == null) {
					context.unsetVariable(name[i]);
				} else {
					context.setVariable(name[i], value[i]);
				}
			}
		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}
	}

	@Override
	public String getTagName() {
		return "include";
	}

}
