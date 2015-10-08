package org.gramar.ast;

import java.util.HashMap;
import java.util.Stack;

import org.gramar.ITagHandler;
import org.gramar.exception.IllFormedTemplateException;
import org.gramar.model.DocumentHelper;
import org.gramar.model.ModelAccess;
import org.gramar.tag.StaticTextTag;
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
	public static int TYPE_COMMENT   	= 5;
	
	private String content;
	private int start;
	private int end;
	private int type;
	private boolean error = false;
	private String errorText = "";
	
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
	public HashMap<String, String> getAttributes() throws IllFormedTemplateException {
		if (isEndTag() | isText() | isComment()) {
			return new HashMap<String, String>();
		}
		if (attrs == null) {
			if (isDirective()) {
				String buf = content.substring(3,content.length()-2);
				attrs = attributesFrom(buf);
			}
			if (isTag() | isEmptyTag()) {
				int index = 0;
				while ((index<content.length()) && !Character.isWhitespace(content.charAt(index))) {
					index++;
				}
				if (index > -1) {
					String buf = content.substring(index);
					index = Math.max(buf.lastIndexOf("\""), buf.lastIndexOf("'")) + 1;
					buf = buf.substring(0,index);
					attrs = attributesFrom(buf);
				} else {
					attrs = new HashMap<String, String>();
				}
			}
		}
		return attrs;
	}

	/*
	 * given a String of the form:   a="" b='' c=""
	 * return a hashmap of attribute names and literal values
	 */
	private HashMap<String, String> attributesFrom(String innards) throws IllFormedTemplateException {

		HashMap<String, String> map = new HashMap<String, String>();

		try {
			String tag = "<root><tag " + innards + "/></root>";
			Document document = DocumentHelper.buildModel(tag);
			
			Node[] node = ModelAccess.getDefault().getNodes(document, "root/tag/attribute::*", false, null);
			
			for (Node n: node) {
				map.put(n.getNodeName(), String.valueOf(n.getNodeValue()));
			}
		} catch (Exception e) {
			throw new IllFormedTemplateException(this, "Invalid tag syntax");
		}
		
		return map;
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
	
	public boolean isComment() {
		return type == TYPE_COMMENT;
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

	/*
	 * Answers whether all characters up to the first new line are all whitespace.
	 * Answers false if the region does not contain a new line char.
	 */
	public boolean firstLineEntirelyWhitespace() {
		
		int index = content.indexOf('\n');
		if (index == -1) { return false; }
		for (int i = 0; i < index; i++) {
			if (!Character.isWhitespace(content.charAt(i))) {
				return false;
			}
		}
		return true;
		
	}

	/*
	 * Answers whether all characters after the last new line are all whitespace.
	 * Answers false if the region does not contain a new line char.
	 */
	public boolean lastLineEntirelyWhitespace() {
		
		int index = content.lastIndexOf('\n');
		if (index == -1) { return false; }
		for (int i = index; i < content.length(); i++) {
			if (!Character.isWhitespace(content.charAt(i))) {
				return false;
			}
		}
		return true;
		
	}

	public boolean isControlTag() {
		return (tagInfo!=null) && (tagInfo.getTagDef()!= null) && (tagInfo.getTagDef().isControlTag());
	}

	/*
	 * Truncate trailing whitespace after the last new line char, but not the newline itself
	 */
	public void truncateTrailingWhitespace() {

		int index = content.lastIndexOf('\n');
		content = content.substring(0,index+1);
		
	}

	/*
	 * Truncate leading whitespace up to and including the first new line char
	 */
	public void truncateLeadingWhitespace() {

		int index = content.indexOf('\n');
		content = content.substring(index+1);
		
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
		error = true;
	}

	public boolean isError() {
		return error;
	}
	
	public static void validateNesting(SourceRegion region[]) {

		Stack<SourceRegion> stack = new Stack<SourceRegion>();
		
		for (SourceRegion sr: region) {
			if (sr.isTag()) {
				stack.push(sr);
			} else if (sr.isEndTag()) {
				SourceRegion current = stack.peek();
				if (sr.getTagInfo().getTagName().equalsIgnoreCase(current.getTagInfo().getTagName())) {
					stack.pop();
				} else {
					current.setErrorText("Missing close tag");
					sr.setErrorText("Mismatched close tag");
				}
			} else if (sr.isEmptyTag()) {

			} else if (sr.isText()) {

			} else if (sr.isDirective()) {

			} else if (sr.isComment()) {

			}
		}
		
		while (!stack.isEmpty()) {
			stack.pop().setErrorText("Missing close tag");
		}
		
	}

}
