package org.gramar.base.tag;

import org.gramar.ICustomTagHandler;
import org.gramar.IGramarContext;
import org.gramar.extension.TagHandler;
import org.gramar.filestore.MergeStream;
import org.gramar.filestore.UpdateProject;


public class CreateProjectTag extends TagHandler implements ICustomTagHandler {

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
