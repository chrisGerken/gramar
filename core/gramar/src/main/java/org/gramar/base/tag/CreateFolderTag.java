package org.gramar.base.tag;

import org.gramar.ICustomTagHandler;
import org.gramar.IGramarContext;
import org.gramar.filestore.UpdateFolder;
import org.gramar.filestore.MergeStream;
import org.gramar.tag.TagHandler;


public class CreateFolderTag extends TagHandler implements ICustomTagHandler {

	public CreateFolderTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String path = getAttributes().get("path");
			path = context.resolveExpressions(path);

			context.getFileStore().addUpdate(new UpdateFolder(path));
			
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "createFolder";
	}

}
