package org.gramar.ast;

import java.util.HashMap;

import org.gramar.model.DocumentHelper;
import org.gramar.model.ModelAccess;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * A SourceRegion represents a substring of a template's source.  When the parser parses
 * a template, the parser produces an array of SourceRegions which together represent the
 * entire template source.  The information within a SourceRegion is consumed by compression
 * and DOM construction.  It is also intended to be used by colorization logic in template
 * editors
 * 
 * @author chrisgerken
 *
 */
public class SourceRegion {

	public static int TYPE_TEXT      	= 0;
	public static int TYPE_TAG       	= 1;
	public static int TYPE_END_TAG   	= 2;
	public static int TYPE_EMPTY_TAG	= 3;
	public static int TYPE_DIRECTIVE 	= 4;
	
	private String content;
	private int start;
	private int end;
	private int type;
	
	private int linenum;
	private int col;
	private HashMap<String, String> attrs = null;
	private TagInfo tagInfo;
	
	public SourceRegion(String content, int start, int end, int type) {
		super();
		this.content = content;
		this.start = start;
		this.end = end;
		this.type = type;
	}
	
	public String getContent() {
		return content;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getEnd() {
		return end;
	}
	
	public int getType() {
		return type;
	}
	
	public void setStart(int linenum, int col) {
		this.linenum = linenum;
		this.col = col;
	}
	
	public String toString() {
		String result = "Text";
		if (type == TYPE_DIRECTIVE) { result = "Directive"; }
		if (type == TYPE_TAG) { result = "Tag"; }
		if (type == TYPE_END_TAG) { result = "End Tag"; }
		if (type == TYPE_EMPTY_TAG) { result = "Empty Tag"; }

		String buf = content;
		if (buf.length() > 20) { buf = buf.substring(0,20) + "...";}
		
		return result + ": "+buf;
	}
	
	/*
	 * Return the attributes from the region.  Formats include:
	 * 
	 * TYPE_DIRECTIVE:   <%@taglib id="org.eclipse.jet.controlTags" prefix="cc"%>
	 * TYPE_TEXT:        (none) 
	 * TYPE_EMPTY_TAG:   <prefix:tag  a=""  b=""  />
	 * TYPE_TAG:         <prefix:tag  a=""  b=""  >
	 * TYPE_END_TAG:
	 * 
	 */
	public HashMap<String, String> getAttributes() {
		if (isEndTag() | isText()) {
			return new HashMap<String, String>();
		}
		if (attrs == null) {
			if (isDirective()) {
				String buf = content.substring(3,content.length()-2);
				attrs = attributesFrom(buf);
			}
			if (isTag() | isEmptyTag()) {
				int index = content.indexOf(" "); 
				String buf = content.substring(index);
				index = Math.max(buf.lastIndexOf("\""), buf.lastIndexOf("'")) + 1;
				buf = buf.substring(0,index);
				attrs = attributesFrom(buf);
			}
		}
		return attrs;
	}

	/*
	 * given a String of the form:   a="" b='' c=""
	 * return a hashmap of attribute names and literal values
	 */
	private HashMap<String, String> attributesFrom(String innards) {

		HashMap<String, String> map = new HashMap<String, String>();

		try {
			String tag = "<root><tag " + innards + "/></root>";
			Document document = DocumentHelper.buildModel(tag);
			
			Node[] node = ModelAccess.getDefault().getNodes(document, "root/tag/attribute::*", false, null);
			
			for (Node n: node) {
				map.put(n.getNodeName(), String.valueOf(n.getNodeValue()));
			}
		} catch (Exception e) {

		}
		
		return map;
	}
	
	public static void main(String[] args) {
		SourceRegion sr = new SourceRegion("<sometag a=\"123\" b=\"fred\" c='s\"am' />", 1, 2, TYPE_TAG);
		HashMap<String, String> map = sr.getAttributes();
		int z = map.size();
	}
	
	public boolean isText() {
		return type == TYPE_TEXT;
	}
	
	public boolean isTag() {
		return type == TYPE_TAG;
	}
	
	public boolean isEndTag() {
		return type == TYPE_END_TAG;
	}
	
	public boolean isEmptyTag() {
		return type == TYPE_EMPTY_TAG;
	}
	
	public boolean isDirective() {
		return type == TYPE_DIRECTIVE;
	}

	public boolean closes(SourceRegion region) {
		return (type == TYPE_TAG) &&
				(region.getType() == TYPE_END_TAG) &&
				(region.getTagInfo().getTagName().equalsIgnoreCase(getTagInfo().getTagName()));
	}

	public SourceRegion append(SourceRegion sr) {
		return new SourceRegion(content+sr.content, start, sr.end, type);
	}

	public TagInfo getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(TagInfo tagInfo) {
		this.tagInfo = tagInfo;
	}

}
