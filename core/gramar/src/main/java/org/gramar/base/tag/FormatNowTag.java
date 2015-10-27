package org.gramar.base.tag;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;

public class FormatNowTag extends TagHandler implements ITagHandler {

	public FormatNowTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
		
			String pattern = getStringAttribute("pattern", context);

			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			String buffer = sdf.format(new Date());

			stream.append(buffer);

		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}
		
	}

	@Override
	public String getTagName() {
		return "formatNow";
	}

}
