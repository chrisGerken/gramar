package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;

public class OtherwiseTag extends TagHandler implements ITagHandler {

	public OtherwiseTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {


		try {
			ChooseTag chooseTag = (ChooseTag) parentNamed("chooseTag");
			if (chooseTag == null) {
				throw new IllFormedTemplateException(null, "Otherwise tag not nested in a choose tag");
			}
			
			if (chooseTag.isCaseMet()) {
				// Some previous when tag beet us to it
				return;
			}

			MergeStream content = processChildren(context);
			chooseTag.setCaseContent(content);
			chooseTag.setCaseMet(true);
		} catch (IllFormedTemplateException e) {
			context.error(e);
		}
		
	}

	@Override
	public String getTagName() {
		return "otherwise";
	}

}
