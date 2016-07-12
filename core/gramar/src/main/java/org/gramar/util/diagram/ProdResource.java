package org.gramar.util.diagram;

public class ProdResource implements Prod {

	private String kind;
	private String path;
	
	public ProdResource(String kind, String path) {
		this.kind = kind;
		this.path = path;
	}

	@Override
	public String getEntryLabel() {
		return kind+" "+escape(path);
	}

	public static String escape(String buf) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length(); i++) {
			char c = buf.charAt(i);
			if ((c == '{') || (c == '}')) {
				sb.append("\\");
			}
			sb.append(c);
		}
		return sb.toString();
	}

	@Override
	public boolean isList() {
		return false;
	}

}
