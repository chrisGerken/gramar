package org.gramar.base;

import org.gramar.ICustomTagHandler;
import org.gramar.IGramarContext;
import org.gramar.ITemplate;
import org.gramar.extension.TagHandler;
import org.gramar.filestore.MergeStream;


public class CreateFileTag extends TagHandler implements ICustomTagHandler {

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
			
			context.getFileStore().setFileContent(path, newStream.asInputStream());
			
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "createFile";
	}

}
