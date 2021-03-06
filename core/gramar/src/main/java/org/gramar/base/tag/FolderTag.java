package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UpdateFolder;
import org.gramar.tag.TagHandler;


public class FolderTag extends TagHandler implements ITagHandler {

	public FolderTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String path = getStringAttribute("path", context);

			context.getFileStore().addUpdate(new UpdateFolder(path));
			
		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}

	}

	@Override
	public String getTagName() {
		return "createFolder";
	}

}
