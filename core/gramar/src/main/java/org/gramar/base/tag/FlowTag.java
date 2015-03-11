package org.gramar.base.tag;

import java.util.StringTokenizer;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UserRegion;
import org.gramar.tag.TagHandler;

public class FlowTag extends TagHandler implements ITagHandler {

	private UserRegion	userRegion;
	private int			width;
	private String		before;
	private String		after;
	
	public FlowTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		MergeStream content = new MergeStream();
		
		userRegion = new UserRegion(content);
		userRegion.markUserRegionStart();
		
		processChildren(content, context);
		
		userRegion.markUserRegionEnd();
		
		String merged = content.toString();

		before = merged.substring(userRegion.getUserRegionStart(), userRegion.getInitialCodeStart());
		String toFlow = merged.substring(userRegion.getInitialCodeStart(), userRegion.getInitialCodeEnd());
		after  = merged.substring(userRegion.getInitialCodeEnd(), userRegion.getUserRegionEnd());

		
		
	}

	@Override
	public String getTagName() {
		return "flow";
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public UserRegion getUserRegion() {
		return userRegion;
	}

}
