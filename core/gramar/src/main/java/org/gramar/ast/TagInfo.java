package org.gramar.ast;

import java.util.StringTokenizer;

import org.gramar.ICustomTagHandler;
import org.gramar.extension.DefinedTag;


public class TagInfo {

	private int 	tagStartOffset;
	private int		tagEndOffset;
	private String 	tagSource;
	private String 	namespace;
	private String 	tagName;
	private boolean	wellFormed;
	private boolean namespaced;
	private boolean endTag;
	private boolean emptyTag;
	private DefinedTag	tagDef;;
	
	public TagInfo(int tagStartOffset, int tagEndOffset, String tagSource) {
		super();
		this.tagStartOffset = tagStartOffset;
		this.tagEndOffset = tagEndOffset;
		this.tagSource = tagSource;
		this.wellFormed = true;
		parse();
	}
	
	private void parse() {
		endTag = tagSource.charAt(1) == '/';
		emptyTag = tagSource.charAt(tagSource.length()-2) == '/';
		
		String buf = tagSource.substring(1);
		if (buf.startsWith("/")) {
			buf = buf.substring(1);
		}
		StringTokenizer st = new StringTokenizer(buf," \t\r\f:>",true);
		
		namespaced = true;
		if (st.countTokens() < 3) {
			namespaced = false;
			return;
		}
		
		namespace = st.nextToken();
		if (!st.nextToken().equals(":")) {
			namespaced = false;
		}
		tagName = st.nextToken();		
		
	}

	private TagInfo(boolean validTag) {
		super();
		this.wellFormed = validTag;
	}
	
	public static TagInfo notATag() {
		return new TagInfo(false);
	}

	public int getTagStartOffset() {
		return tagStartOffset;
	}

	public int getTagEndOffset() {
		return tagEndOffset;
	}

	public String getTagSource() {
		return tagSource;
	}

	public String getNamespace() {
		return namespace;
	}

	public String getTagName() {
		return tagName;
	}

	public boolean isEndTag() {
		return endTag;
	}

	public boolean isEmptyTag() {
		return emptyTag;
	}

	public int getTagLength() {
		return tagEndOffset - tagStartOffset + 1;
	}

	public boolean isWellFormed() {
		return wellFormed;
	}

	public boolean isNamespaced() {
		return namespaced;
	}

	public DefinedTag getTagDef() {
		return tagDef;
	}

	public void setTagDef(DefinedTag tagDef) {
		this.tagDef = tagDef;
	}

}
