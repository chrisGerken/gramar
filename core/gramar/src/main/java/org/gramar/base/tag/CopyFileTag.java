package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UpdateBinary;
import org.gramar.tag.TagHandler;
import org.gramar.util.GramarHelper;


public class CopyFileTag extends TagHandler implements ITagHandler {

	public CopyFileTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String target = getStringAttribute("target", context);

			String src = getStringAttribute("src", context);
			byte[] content = GramarHelper.getBytes(context.getPattern().getTemplateBinary(src));

			Boolean replace = getBooleanAttribute("replace", context, true);
			
			context.getFileStore().addUpdate(new UpdateBinary(target, content, replace));
			
		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}

	}

	@Override
	public String getTagName() {
		return "copyFile";
	}

}
