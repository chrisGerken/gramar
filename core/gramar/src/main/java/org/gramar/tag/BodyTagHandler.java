package org.gramar.tag;

import org.gramar.IGramarContext;
import org.gramar.extension.TagHandler;
import org.gramar.filestore.MergeStream;


public abstract class BodyTagHandler extends TagHandler {

	public BodyTagHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		boolean execute = doBeforeBody(stream, context);
		
		if (execute) {
			doBody(stream, context);
		}
		
		doAfterBody(stream, context);
	
	}

	protected boolean doBeforeBody(MergeStream stream, IGramarContext context) {
		return true;
	}

	protected void doBody(MergeStream stream, IGramarContext context) {
		
	}

	protected void doAfterBody(MergeStream stream, IGramarContext context) {
		
	}


}
