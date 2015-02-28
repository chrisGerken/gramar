package org.gramar.ast;

import java.util.ArrayList;

import org.gramar.ICustomTagHandler;
import org.gramar.IGramarContext;
import org.gramar.exception.GramarException;
import org.gramar.exception.InvalidTemplateExtensionException;
import org.gramar.exception.NamespaceNotDefinedException;
import org.gramar.exception.NoSuchTemplatingExtensionException;
import org.gramar.exception.TemplatingExtensionNotDefinedException;



public class Parser {

	private IGramarContext patternContext;
	
	private boolean compressRegions = true;
	
	public Parser(IGramarContext patternContext) {
		this.patternContext = patternContext;
	}

	public SourceRegion[] parse(String source) {

		ArrayList<SourceRegion> regions = new ArrayList<SourceRegion>();
		
		int begin = source.indexOf("<");
		int end = -1;
		while (begin > -1) {
			TagInfo tagInfo = tagAfter(source,begin);
			if (tagInfo.isWellFormed()) {
				SourceRegion region = new SourceRegion(source.substring(end+1,begin),end+1,begin-1,SourceRegion.TYPE_TEXT);
				regions.add(region);
				
				int prevEnd = end;	
				end = begin + tagInfo.getTagLength();
				int type = SourceRegion.TYPE_TEXT;
				if (validate(tagInfo)) {
					type = SourceRegion.TYPE_TAG;
					if (tagInfo.isEndTag()) {
						type = SourceRegion.TYPE_END_TAG;
					} else if (tagInfo.isEmptyTag()) {
						type = SourceRegion.TYPE_EMPTY_TAG;
					} 
				}
				region = new SourceRegion(tagInfo.getTagSource(),tagInfo.getTagStartOffset(),tagInfo.getTagEndOffset(),type);
				region.setTagInfo(tagInfo);
				regions.add(region);
				begin = end + 1;

			} else if (directiveBeginsAt(source,begin+1)) {
				int prevEnd = end;	
				end = source.indexOf("%>",begin);
				if (end > -1) {
					SourceRegion region = new SourceRegion(source.substring(prevEnd+1,begin),begin,end,SourceRegion.TYPE_DIRECTIVE);
					regions.add(region);
					executeDirective(region);
					begin = end + 1;
				} else {
					SourceRegion region = new SourceRegion(source.substring(end+1),end+1,source.length()-1,SourceRegion.TYPE_TEXT);
					regions.add(region);
					end = source.length() - 1;
					begin = source.length();
				}
			} else {
				begin = begin + 1;
			}
			begin = source.indexOf("<",begin);
		}
		
		if (end+1 < source.length()) {
			SourceRegion region = new SourceRegion(source.substring(end+1),end+1,source.length()-1,SourceRegion.TYPE_TEXT);
			regions.add(region);
		}

		if (compressRegions) {
			regions = compress(regions);
		}
		SourceRegion sr[] = new SourceRegion[regions.size()];
		regions.toArray(sr);
		return sr;
	}
		
	/*
	 * Collapse adjacent text regions into single text regions.  Then remove
	 * leading and trailing whitespace for any control tag on its own line.
	 */
	private ArrayList<SourceRegion> compress(ArrayList<SourceRegion> regions) {
		ArrayList<SourceRegion> result = new ArrayList<SourceRegion>();
		SourceRegion last = null;
		for (SourceRegion sr: regions) {
			if (sr.isText()) {
				if (last == null) {
					last = sr;
				} else {
					last = last.append(sr);
				}
			} else {
				if (last != null) {
					result.add(last);
				}
				last = null;
				result.add(sr);
			}
		}
		if (last != null) {
			result.add(last);
		}
		
		for (int i = 0; i < regions.size(); i++) {
			
		}
		
		return result;
	}

	/*
	 * Interact with the pattern context add the taglib described by this directive
	 */
	private void executeDirective(SourceRegion region) {
		
		//    <%@taglib id="org.eclipse.jet.controlTags" prefix="cc"%>
		
	}

	private boolean validate(TagInfo tagInfo) {
		try {
			if (!tagInfo.isNamespaced()) {
				return false;
			}
			ICustomTagHandler handler = patternContext.getCustomTagHandler(tagInfo.getNamespace(), tagInfo.getTagName());
			if (handler == null) {
				return false;
			}
			tagInfo.setHandler(handler);
			return true;
		} catch (TemplatingExtensionNotDefinedException e) {
			
		} catch (NamespaceNotDefinedException e) {

		} catch (NoSuchTemplatingExtensionException e) {

		} catch (InvalidTemplateExtensionException e) {

		} catch (GramarException e) {

		}
		return false;
	}

	/*
	 * Check to see if the substring of source, beginning at offset, contains a
	 * custom tag (the opening angle bracket should be at offset) and if so, return the
	 * length of the tag body (up to and including the closing angle bracket).  Otherwise
	 * return -1. 
	 */
	private TagInfo tagAfter(String source, int offset) {
		
		// If we're at the end of source then return false
		if (offset >= source.length()) { return TagInfo.notATag(); }
		
		// Create a copy of the remaining source that we can modify, then blank out any
		// escaped characters
		StringBuffer sb = new StringBuffer(source.length()-offset+2);
		for (int i = offset; i < source.length(); i++) {
			if (source.charAt(i)=='\\') {
				sb.append("  ");
				i++;
			} else {
				sb.append(source.charAt(i));
			}
		}
		String buf = sb.toString();
		
		// Look for the first right angle bracket ('>') not in quotes.
		// Kludge: count '/" and stop on '>' only if both counts are even
		
		int sq = 0;
		int dq = 0;
		int tagEnd = -1;
		
		for (int i = 0; ((i < buf.length()) && (tagEnd < 0)); i++) {
		    char c = buf.charAt(i);
		    if (c == '\'') {
		    	sq++;
		    } else if (c == '"') {
		    	dq++;
		    } else if (c == '>') {
		    	if (((sq%2)==0) && ((dq%2)==0)) { 
		    		tagEnd = i; 
		    	}
		    } 
		}

		if (tagEnd < 0) {
			return TagInfo.notATag();
		}

		return new TagInfo(offset, offset+tagEnd, source.substring(offset, offset+tagEnd+1));

	}
	
	private boolean directiveBeginsAt(String source, int i) {
		
		//    <%@taglib id="org.eclipse.jet.controlTags" prefix="cc"%>
		
		if (i >= source.length()) { return false; }
		return (source.substring(i).startsWith("%@taglib"));
	}

	public boolean isCompressRegions() {
		return compressRegions;
	}

	public void setCompressRegions(boolean compressRegions) {
		this.compressRegions = compressRegions;
	}

}
