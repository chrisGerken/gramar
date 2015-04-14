package org.gramar.gramar;

import org.gramar.ISampleModel;

/**
 * Represents and describes one of the sample models deployed with a gramar.  Tools can
 * ask a gramar for a list of its deployed sample models and jumpstart users by creating
 * a file with one of those models.
 */
public class SampleModel implements ISampleModel {

	private String name;
	private String path;
	private String description;
	
	public SampleModel(String name, String path, String description) {
		this.name = name;
		this.path = path;
		this.description = description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getDescription() {
		return description;
	}

}
