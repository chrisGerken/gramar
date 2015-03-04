package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.ITemplate;
import org.gramar.filestore.UpdateFile;
import org.gramar.filestore.MergeStream;
import org.gramar.tag.TagHandler;


public class CreateFileTag extends TagHandler implements ITagHandler {

	public CreateFileTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String path = getAttributes().get("path");
			path = context.resolveExpressions(path);

			String templateName = getAttributes().get("template");
			
			MergeStream newStream = new MergeStream();
			
			ITemplate template = context.getPattern().getTemplate(templateName, context);
			template.mergeTo(newStream, context);
			
			context.getFileStore().addUpdate(new UpdateFile(path, newStream));
			
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "createFile";
	}

}
