package org.gramar.base.tag;

import org.gramar.ICustomTagHandler;
import org.gramar.IGramarContext;
import org.gramar.filestore.MergeStream;
import org.gramar.filestore.UserRegion;
import org.gramar.tag.TagHandler;

public class UserRegionTag extends TagHandler implements ICustomTagHandler {

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
