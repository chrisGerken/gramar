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
			updated.append(pure.substring(written,region.getInitialCodeStart()+1));
			
			// get the before and after eye catchers
			String before = pure.substring(region.getUserRegionStart()+1, region.getInitialCodeStart()+1);
			String after = pure.substring(region.getInitialCodeEnd()+1, region.getUserRegionEnd()+1);
			
			// look for the strings in the previous revision of the resource
			int beforeIndex = prev.indexOf(before);
			int afterIndex = -1;
			
			if (beforeIndex > -1) {
				afterIndex = prev.indexOf(after, beforeIndex);
				if (afterIndex > -1) {
					String previousCode = prev.substring(beforeIndex+before.length()+1,afterIndex);
					updated.append(previousCode);
					written = region.getInitialCodeEnd() + 1;
				}
			}
			
		}
		
		String remaining = pure.substring(written);
		updated.append(remaining);
		
		return updated;
		
	}
}
