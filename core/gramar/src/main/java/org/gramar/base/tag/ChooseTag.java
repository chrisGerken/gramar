package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.exception.MissingRequiredAttributeException;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;

public class ChooseTag extends TagHandler implements ITagHandler {

	private Object 		select;
	private boolean		caseMet;
	private MergeStream caseContent;
	
	public ChooseTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		
		caseMet = false;
		caseContent = null;
		
		try {
			try {
				select = getObjectAttribute("select", context);
			} catch (MissingRequiredAttributeException e) {
				select = null;
			}
			
			// Process child tags, but ignore the resulting content.
			MergeStream content = processChildren(context);

			// If one of the when or otherwise tags is processed, that child will 
			// push the resulting MergeStream into this tag's caseContent variable.
			if (caseMet) {
				stream.append(caseContent);
			}
			
		} catch (Exception e) {
			context.error(e);
		}
		
	}

	@Override
	public String getTagName() {
		return "choose";
	}

	public boolean isCaseMet() {
		return caseMet;
	}

	public void setCaseMet(boolean caseMet) {
		this.caseMet = caseMet;
	}

	public Object getSelect() {
		return select;
	}

	public void setCaseContent(MergeStream caseContent) {
		this.caseContent = caseContent;
	}

}
