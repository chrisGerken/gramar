package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.exception.MissingRequiredAttributeException;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;

public class WhenTag extends TagHandler implements ITagHandler {

	public WhenTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			ChooseTag chooseTag = (ChooseTag) parentNamed("choose");
			if (chooseTag == null) {
				throw new IllFormedTemplateException(null, "When tag not nested in a choose tag");
			}
			
			if (chooseTag.isCaseMet()) {
				// Some previous when tag beet us to it
				return;
			}

			boolean match = false;
			
			// Check to see if a select arg was provided on the choose tag
			if (chooseTag.getSelect() != null) {
				
				// There was a select arg specified.  See if the test arg on this
				// tag has the same value
				Object test = getObjectAttribute("test", context);
				Object select = chooseTag.getSelect();
				if (select.equals(test)) {
					match = true;
				}
				
			} else {
				
				// No select value specified, resolve the test arg as a boolean
				String testArg = null;
				try {
					testArg = getRawAttribute("test");
				} catch (Exception e) {
					throw new MissingRequiredAttributeException("Error retireving attribute 'test'", e);
				}
				match = context.resolveToBoolean(testArg);
				
			}

			if (match) {
				MergeStream content = processChildren(context);
				chooseTag.setCaseContent(content);
				chooseTag.setCaseMet(true);
			}
		} catch (Exception e) {
			context.error(e);
		}
		
	}

	@Override
	public String getTagName() {
		return "when";
	}

}
