package org.gramar.base.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.ITemplate;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UpdateFile;
import org.gramar.tag.TagHandler;


public class FileTag extends TagHandler implements ITagHandler {

	public FileTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String path = getStringAttribute("path", context);

			String templateName = getStringAttribute("template", context);

			Boolean replace = getBooleanAttribute("replace", context, true);
			
			MergeStream newStream = new MergeStream();
			
			ITemplate template = context.getGramar().getTemplate(templateName, context);
			template.mergeTo(newStream, context);
			
			context.getFileStore().addUpdate(new UpdateFile(path, newStream, replace));
			
		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}

	}

	@Override
	public String getTagName() {
		return "createFile";
	}

}
