package org.gramar.tag;

import java.util.ArrayList;
import java.util.List;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.ast.Parser;
import org.gramar.ast.SourceRegion;
import org.gramar.exception.GramarException;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.resource.MergeStream;


public class TagDocument extends TagHandler {

	/**
	 * The gramar-relative path of the production from which this
	 * TagDocument was created.
	 */
	private String productionId;
	
	public TagDocument(String productionId) {
		this.productionId = productionId;
	}

	/**
	 * Return the production ID
	 * 
	 * @return the gramar-relative path of the production from which
	 * this TagDocument was created
	 */
	public String getProductionId() {
		return productionId;
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

	public static TagDocument build(String source, IGramarContext context, String productionId) throws GramarException {
		
		Parser parser = new Parser(context);
		SourceRegion region[] = parser.parse(source);

		TagDocument doc = new TagDocument(productionId);
		
		// Variable current always points to the Tag in the DOM that will process the 
		// next source region
		ITagHandler current = doc;
		
		for (SourceRegion sr: region) {
			sr.setProduction(productionId);
			current = current.glomUsing(sr, context);
		}
		
		if (current != doc) {
			throw new IllFormedTemplateException(region[region.length-1], "Missing end tag");
		}
		return doc;
	
	}

	@Override
	public List<String> stackTrace() {
		ArrayList<String> parents = new ArrayList<String>();
		parents.add("In "+getProductionId());
		return parents;
	}

}
