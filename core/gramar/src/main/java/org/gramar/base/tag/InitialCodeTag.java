package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UserRegion;
import org.gramar.tag.TagHandler;

public class InitialCodeTag extends TagHandler implements ITagHandler {

	public InitialCodeTag() {
	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		UserRegionTag urTag = (UserRegionTag) parentNamed("userRegion");
		
		UserRegion userRegion = urTag.getUserRegion();
		
		userRegion.markInitialCodeStart();
		
		processChildren(stream, context);
		
		userRegion.markInitialCodeEnd();
		
	}

	@Override
	public String getTagName() {
		return "initialCode";
	}

}
