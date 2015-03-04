package org.gramar.tag;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.ast.Parser;
import org.gramar.ast.SourceRegion;
import org.gramar.exception.GramarException;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.filestore.MergeStream;


public class TagDocument extends TagHandler {

	public TagDocument() {

	}

	@Override
	public String getTagName() {
		return "--Document--";
	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		for (ITagHandler child: children) {
			child.mergeTo(stream, context);
		}
		
	}

	public static TagDocument build(String source, IGramarContext context) throws GramarException {
		
		Parser parser = new Parser(context);
		SourceRegion region[] = parser.parse(source);

		TagDocument doc = new TagDocument();
		
		// Variable current always points to the Tag in the DOM that will process the 
		// next source region
		ITagHandler current = doc;
		
		for (SourceRegion sr: region) {
			current = current.glomUsing(sr, context);
		}
		
		if (current != doc) {
			throw new IllFormedTemplateException(region[region.length-1], "Missing end tag");
		}
		return doc;
	
	}

}
