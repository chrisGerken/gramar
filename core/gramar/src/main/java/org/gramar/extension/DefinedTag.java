package org.gramar.extension;


public class DefinedTag {

	private String 		tagName;
	private String		fqClassName;
	private boolean		controlTag;
	private String 		extensionId;
	
	public DefinedTag(String tagName, String fqClassName, boolean controlTag, String extensionId) {
		super();
		this.tagName = tagName;
		this.fqClassName = fqClassName;
		this.controlTag = controlTag;
		this.extensionId = extensionId;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public String getFQClassName() {
		return fqClassName;
	}
	
	public boolean isControlTag() {
		return controlTag;
	}
	
	public String getExtensionId() {
		return extensionId;
	}
}
