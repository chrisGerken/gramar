package org.gramar.resource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;


public class MergeStream {

	private StringBuilder builder;
	
	private ArrayList<UserRegion> userRegions = new ArrayList<UserRegion>();
	
	public MergeStream() {
		builder = new StringBuilder(2000);
	}
	
	public MergeStream(int capacity) {
		builder = new StringBuilder(capacity);
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
	
	public boolean hasUserRegions() {
		return !userRegions.isEmpty();
	}

	/*
	 * For each user region, find the before and after eyecatchers in the
	 * previous content and keep the previous code if found.
	 */
	public MergeStream saveRegionChanges(String prev) throws IOException {

		// pure contains the just-generated content
		String pure = toString();
		
		MergeStream updated = new MergeStream(pure.length());
		int written = 0;

		for (UserRegion region: userRegions) {
			
			// Write unwritten content up to the start of the initial code
			updated.append(pure.substring(written,region.getInitialCodeStart()));
			
			// get the before and after eye catchers
			String before = pure.substring(region.getUserRegionStart(), region.getInitialCodeStart());
			String after = pure.substring(region.getInitialCodeEnd(), region.getUserRegionEnd());
			
			// look for the strings in the previous revision of the resource
			int beforeIndex = prev.indexOf(before);
			int afterIndex = -1;
			
			if (beforeIndex > -1) {
				afterIndex = prev.indexOf(after, beforeIndex);
				if (afterIndex > -1) {
					String previousCode = prev.substring(beforeIndex+before.length(),afterIndex);
					updated.append(previousCode);
					written = region.getInitialCodeEnd();
				}
			}
			
		}
		
		String remaining = pure.substring(written);
		updated.append(remaining);
		
		return updated;
		
	}

	public void append(MergeStream stream) throws IOException {
		
		int offset = position();
		
		append(stream.toString());
		
		for (UserRegion region: stream.userRegions) {
			addUserRegion(region.offset(stream, offset));
		}
		
	}

}
