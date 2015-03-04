package org.gramar.filestore;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;


public class MergeStream {

	private StringBuilder builder = new StringBuilder(2000);
	
	private ArrayList<UserRegion> userRegions = new ArrayList<UserRegion>();
	
	public MergeStream() {

	}
	
	public void append(String content) throws IOException {
		builder.append(content);
	}
	
	public void addUserRegion(UserRegion userRegion) {
		userRegions.add(userRegion);
	}

	public Reader asReader() {
		return new StringReader(builder.toString());
	}
	
	public String toString() {
		return builder.toString();
	}

	public int position() {
		return builder.length();
	}
}
