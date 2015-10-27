package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UserRegion;
import org.gramar.tag.TagHandler;

public class InitialCodeTag extends TagHandler implements ITagHandler {

	public InitialCodeTag() {
	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			UserRegionTag urTag = (UserRegionTag) parentNamed("userRegion");
			
			if (urTag==null) {
				throw new IllFormedTemplateException(null, "initialCode tag not nested in a userRegion tag");
			}
			
			UserRegion userRegion = urTag.getUserRegion();
			
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
		return "initialCode";
	}

}
