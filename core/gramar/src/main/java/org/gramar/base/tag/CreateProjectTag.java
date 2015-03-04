package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UpdateProject;
import org.gramar.tag.TagHandler;


public class CreateProjectTag extends TagHandler implements ITagHandler {

	public CreateProjectTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String path = getAttributes().get("path");
			path = context.resolveExpressions(path);

			String altPath = getAttributes().get("altPath");
			altPath = context.resolveExpressions(altPath);
			if ((altPath==null)||(altPath.trim().length()==0)) {
				altPath = "";
			}

			context.getFileStore().addUpdate(new UpdateProject(path,altPath));
			
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "createFile";
	}

}
