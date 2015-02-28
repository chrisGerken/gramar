package org.gramar.tag;

import java.io.IOException;

import org.gramar.ICustomTagHandler;
import org.gramar.IGramarContext;
import org.gramar.extension.TagHandler;
import org.gramar.filestore.MergeStream;


/**
 * Not really a tag, but since we're building a DOM of tag handlers we need a "tag" that 
 * just writes out a block of static text.
 * 
 * @author chrisgerken
 *
 */
public class StaticTextTag extends TagHandler {

		private String content;
		
	public StaticTextTag(ICustomTagHandler parent, String content) {
		this.content = content;
		this.parent = parent;
	}

	@Override
	public String getTagName() {
		return "--StaticText--";
	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			stream.append(content);
		} catch (IOException e) {
			context.error(e);
		}
		
	}

}
