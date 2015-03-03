package org.gramar.filestore;

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

	
}
