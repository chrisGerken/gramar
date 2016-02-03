package org.gramar.base.tag;

import java.io.InputStream;

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

			Boolean replace = getBooleanAttribute("replace", context, true);

			String srcContext = getStringAttribute("srcContext", context, "gramar");

			if (srcContext.equalsIgnoreCase("plugin")) {
				srcContext = "gramar";
			}

			if (srcContext.equalsIgnoreCase("pattern")) {
				srcContext = "gramar";
			}
			
			byte[] content = null;
			if (srcContext.equalsIgnoreCase("gramar")) {
				content = GramarHelper.getBytes(context.getGramar().getTemplateBinary(src));
			} else {
				content = GramarHelper.getBytes(context.getFileStore().getFileByteContent(src));
			}
			
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
