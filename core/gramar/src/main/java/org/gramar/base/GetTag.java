package org.gramar.base;

import org.gramar.ICustomTagHandler;
import org.gramar.IGramarContext;
import org.gramar.extension.TagHandler;
import org.gramar.filestore.MergeStream;


public class GetTag extends TagHandler implements ICustomTagHandler {

	public GetTag() {

	}

	@Override
	public String getTagName() {
		return "get";
	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String select = getAttributes().get("select");
			String value = context.resolveToString(select);
			stream.append(value);
		} catch (Exception e) {
			context.error(e);
		}

	}

}
