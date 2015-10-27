package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UserRegion;
import org.gramar.tag.TagHandler;

public class FlowContentTag extends TagHandler implements ITagHandler {

	public FlowContentTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			
			int width = getIntegerAttribute("width", context, 50);
			
			FlowTag flowTag = (FlowTag) parentNamed("flow");
			if (flowTag == null) {
				throw new IllFormedTemplateException(null, "flowContent tag not nested in a flow tag");
			}
			
			flowTag.setWidth(width);
			
			UserRegion userRegion = flowTag.getUserRegion();
			
			userRegion.markInitialCodeStart();
			
			processChildren(stream, context);
			
			userRegion.markInitialCodeEnd();
			
		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}
		
	}

	@Override
	public String getTagName() {
		return "flowContent";
	}

}
