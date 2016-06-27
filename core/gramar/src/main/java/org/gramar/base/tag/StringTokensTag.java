package org.gramar.base.tag;

import java.util.Arrays;
import java.util.StringTokenizer;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class StringTokensTag extends TagHandler implements ITagHandler {

	public StringTokensTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {
		
		try {

			String string = getStringAttribute("string", context);
			String var = getStringAttribute("var", context);
			String delim = getStringAttribute("delimiter", context, "");
			String delimBy = getStringAttribute("delimitedBy", context, " ");
			boolean reverse = getBooleanAttribute("reverse", context, false);

			Object prevValue = context.getVariable(var);

//			value: <c:get select="$curStr/@value"/>
//		    index: <c:get select="$curStr/@index"/>
//		    delimiter: <c:get select="$curStr/@delimiter"/>
//		    first: <c:get select="$curStr/@first"/>
//		    last: <c:get select="$curStr/@last"/>
			
			Document doc = context.getPrimaryModel();
			Document owner = doc.getOwnerDocument();
			if (owner!=null) {
				doc = owner;
			}
			Element element = doc.createElement(var);

			StringTokenizer st = new StringTokenizer(string,delimBy);
			String token[] = new String[st.countTokens()];
			for  (int i = 0; i < token.length; i++) {
				token[i] = st.nextToken();
			}
			
			if (reverse) {
				String temp[] = new String[token.length];
				for (int i = 0; i < token.length; i++) {
					temp[i] = token[token.length-i-1];
				}
				token = temp;
			}

			for (int i = 0; i < token.length; i++) {
				element.setAttribute("first", ((i==0) ? "true" : "false") );
				element.setAttribute("last", ((i==token.length-1) ? "true" : "false") );
				element.setAttribute("index", String.valueOf(i) );
				element.setAttribute("value", token[i] );
				
				if (var != null) {
					context.setVariable(var, element);
				}
				
				processChildren(stream, context);

				if (i > 0) {
				    stream.append(delim);
				}

				context.unsetVariable(var);
			}
			
			if (prevValue == null) {
				context.unsetVariable(var);
			} else {
				context.setVariable(var, prevValue);
			}
			
		} catch (Exception e) {
			context.error(e);
			logStackTrace(context);
		}

	}

	@Override
	public String getTagName() {
		return "stringTokens";
	}

}
