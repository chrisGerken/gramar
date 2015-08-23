package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;


public class MillisecondsTag extends TagHandler implements ITagHandler {

	private static long last = System.currentTimeMillis();
	
	public MillisecondsTag() {

	}

	@Override
	public String getTagName() {
		return "milliseconds";
	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {

			stream.append(String.valueOf(getMilliseconds()));

		} catch (Exception e) {
			context.error(e.getMessage()+" occurred during "+toString());
		}

	}

	private synchronized static long getMilliseconds() {

		long now = System.currentTimeMillis();
		if (last == now) {
			now++;
		}
		last = now;
		return now;
		
	}

}
