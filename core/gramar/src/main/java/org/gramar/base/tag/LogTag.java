package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;


public class LogTag extends TagHandler implements ITagHandler {

	public LogTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String severity = getStringAttribute("severity", context, "info");

			MergeStream content = processChildren(context);
			
			if (severity.equalsIgnoreCase("info")) {
				context.info(content.toString());
			} else if (severity.equalsIgnoreCase("warning")) {
				context.warning(content.toString());
			} else if (severity.equalsIgnoreCase("error")) {
				context.error(content.toString());
			} 
			
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "log";
	}

}
