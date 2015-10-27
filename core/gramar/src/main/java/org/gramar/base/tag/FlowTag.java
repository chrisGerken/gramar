package org.gramar.base.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UserRegion;
import org.gramar.tag.TagHandler;

public class FlowTag extends TagHandler implements ITagHandler {

	private UserRegion	userRegion;
	private int			width;
	private String		before;
	private String		after;
	
	public FlowTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		MergeStream content = new MergeStream();
		
		userRegion = new UserRegion(content);
		userRegion.markUserRegionStart();
		
		processChildren(content, context);
		
		userRegion.markUserRegionEnd();
		
		String merged = content.toString();

		before = merged.substring(userRegion.getUserRegionStart(), userRegion.getInitialCodeStart());
		String toFlow = merged.substring(userRegion.getInitialCodeStart(), userRegion.getInitialCodeEnd());
		after  = merged.substring(userRegion.getInitialCodeEnd(), userRegion.getUserRegionEnd());

		String lines[] = split(toFlow);
		
		try {
			for (int line = 0; line < lines.length; line++) {
				stream.append(before);
				stream.append(lines[line]);
				stream.append(after);
				if (line < (lines.length-1)) {
					stream.append("\n");
				}
			}
		} catch (IOException e) {
			context.error(e);
			logStackTrace(context);
		}
		
		
	}

	private String[] split(String toFlow) {
		ArrayList<String> lines = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(toFlow, "\n");
		while (st.hasMoreTokens()) {
			String buf = st.nextToken();
			if (buf.length() == 0) {
				lines.add("");
			} else {
				while (buf.length() > 0) {
					if (buf.length() <= width) {
						lines.add(buf);
						buf = "";
					} else {
						int index = width - 1;
						while ((index > -1) && (!Character.isWhitespace(buf.charAt(index)))) {
							index--;
						}
						if (index < 0) {
							index = width - 1;
						}
						lines.add(buf.substring(0,index+1));
						buf = buf.substring(index+1);
					}
				}
			}
		}
		String result[] = new String[lines.size()];
		lines.toArray(result);
		return result;
	}

	@Override
	public String getTagName() {
		return "flow";
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public UserRegion getUserRegion() {
		return userRegion;
	}

}
