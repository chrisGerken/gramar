package org.gramar.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gramar.ITagHandler;
import org.gramar.IGramarContext;
import org.gramar.ast.SourceRegion;
import org.gramar.ast.TagInfo;
import org.gramar.exception.GramarException;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.filestore.MergeStream;


public abstract class TagHandler implements ITagHandler {

	protected ITagHandler parent;
	protected ArrayList<ITagHandler> children = new ArrayList<ITagHandler>();
	protected HashMap<String, String> attributes;
	
	public TagHandler() {
		// Needs to be no-argument
	}

	@Override
	public ITagHandler glomUsing(SourceRegion region, IGramarContext context) throws GramarException {

		if (region.isTag()) {
			TagInfo info = region.getTagInfo();
			String namespace = info.getNamespace();
			String tagName = info.getTagName();
			ITagHandler handler = context.getTagHandler(namespace, tagName);
			handler.setAttributes(region.getAttributes());
			handler.setParent(this);
			addChild(handler);
			return handler;
		} else if (region.isEndTag()) {
			if (region.getTagInfo().getTagName().equalsIgnoreCase(getTagName())) {
				return parent;
			}
			throw new IllFormedTemplateException(region,"Missing end tag: "+getTagName());
		} else if (region.isEmptyTag()) {
			TagInfo info = region.getTagInfo();
			String namespace = info.getNamespace();
			String tagName = info.getTagName();
			ITagHandler handler = context.getTagHandler(namespace, tagName);
			handler.setAttributes(region.getAttributes());
			handler.setParent(this);
			addChild(handler);
			return this;
		} else if (region.isText()) {
			StaticTextTag handler = new StaticTextTag(this, region.getContent());
			addChild(handler);
			return this;
		} else if (region.isDirective()) {
			return this;
		} 
		
		throw new IllFormedTemplateException(region, "Unknown source region type");
		
	}

	public abstract String getTagName();

	private void addChild(ITagHandler handler) {
		children.add(handler);
	}

	@Override
	public List<ITagHandler> getChildren() {
		return children;
	}

	public ITagHandler getParent() {
		return parent;
	}

	public void setParent(ITagHandler parent) {
		this.parent = parent;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(HashMap<String, String> attributes) {
		this.attributes = attributes;
	}
	
	/*
	 * Merge all of the receiver's child handlers with the given stream
	 */
	public void processChildren(MergeStream stream, IGramarContext context) {
		for (ITagHandler child: children) {
			child.mergeTo(stream, context);
		}
	}
	
	/*
	 * Create a new stream and merge all of the receiver's child handlers
	 * with that stream.  Return the stream.
	 */
	public MergeStream processChildren(IGramarContext context) {
		MergeStream stream = new MergeStream();
		processChildren(stream, context);
		return stream;
	}
	
	/*
	 * Answers the first parent (direct or indirect) with the given tag name
	 */
	public ITagHandler parentNamed(String name) {
		if (getTagName().equalsIgnoreCase(name)) {
			return this;
		} else if (getParent() == null) {
			return null;
		} else {
			return getParent().parentNamed(name);
		}
	}

}
