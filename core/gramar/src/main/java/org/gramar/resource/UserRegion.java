package org.gramar.resource;

public class UserRegion {

	private MergeStream stream;
	
	private int userRegionStart;
	private int initialCodeStart;
	private int initialCodeEnd;
	private int userRegionEnd;
	
	public UserRegion(MergeStream stream) {
		this.stream = stream;
	}

	public void markUserRegionStart() {
		userRegionStart = stream.position();
	}

	public void markUserRegionEnd() {
		userRegionEnd = stream.position();
	}

	public void markInitialCodeStart() {
		initialCodeStart = stream.position();
	}

	public void markInitialCodeEnd() {
		initialCodeEnd = stream.position();
	}

	public int getUserRegionStart() {
		return userRegionStart;
	}

	public int getInitialCodeStart() {
		return initialCodeStart;
	}

	public int getInitialCodeEnd() {
		return initialCodeEnd;
	}

	public int getUserRegionEnd() {
		return userRegionEnd;
	}

	public UserRegion offset(MergeStream existingStream, int offset) {
		UserRegion region = new UserRegion(existingStream);
		region.initialCodeEnd 	= initialCodeEnd + offset;
		region.initialCodeStart = initialCodeStart + offset;
		region.userRegionEnd 	= userRegionEnd + offset;
		region.userRegionStart 	= userRegionStart + offset;
		return region;
	}

	
}
