package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UserRegion;
import org.gramar.tag.TagHandler;

public class UserRegionTag extends TagHandler implements ITagHandler {

	private UserRegion userRegion;
	
	public UserRegionTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		userRegion = new UserRegion(stream);
		userRegion.markUserRegionStart();
		
		processChildren(stream, context);
		
		userRegion.markUserRegionEnd();
		
		stream.addUserRegion(userRegion);
	}
	
	protected UserRegion getUserRegion() {
		return userRegion;
	}

	@Override
	public String getTagName() {
		return "userRegion";
	}

}
