package org.gramar.resource;

import org.gramar.IFileStore;
import org.gramar.util.GramarHelper;

public abstract class UpdateResource implements Comparable<UpdateResource> {

	protected String path;
	
	public static final int RESOURCE_FILE = 1;
	public static final int RESOURCE_FOLDER = 2;
	public static final int RESOURCE_PROJECT = 3;
	
	
	public UpdateResource(String path) {
		this.path = path;
	}
	
	public abstract void execute(IFileStore store) throws Exception;

	@Override
	public int compareTo(UpdateResource o) {
		return path.compareTo(o.path);
	}

	public abstract String report();
	
	public String getProject() {
		return GramarHelper.pathSegments(path)[0];
	}
	
	public String getProjectRelativePath() {
		StringBuffer sb = new StringBuffer();
		String segment[] = GramarHelper.pathSegments(path);
		String delim = "";
		for (int i = 1; i < segment.length; i++) {
			sb.append(delim);
			delim = "/";
			sb.append(segment[i]);
		}
		return sb.toString();
	}
	
	public String getContainingFolder() {
		StringBuffer sb = new StringBuffer();
		String segment[] = GramarHelper.pathSegments(path);
		String delim = "";
		for (int i = 0; i < segment.length-1; i++) {
			sb.append(delim);
			delim = "/";
			sb.append(segment[i]);
		}
		return sb.toString();
	}
	
	public String getFileName() {
		String segment[] = GramarHelper.pathSegments(path);
		return segment[segment.length-1];
	}
	
	public boolean isProject() {
		return false;
	}
	
	public boolean isFolder() {
		return false;
	}
	
	public boolean isFile() {
		return false;
	}

}
