package org.gramar;

/**
 *  Represents and describes one of the sample models deployed
 *  with a gramar
 */
public interface ISampleModel {

	/**
	 * @return the unique, single-token name of the sample model
	 */
	public String getName();

	/**
	 * @return the gramar-relative path of the resource containing
	 * the sample model
	 */
	public String getPath();

	/**
	 * @return a short description of the model
	 */
	public String getDescription();
	
}
