package org.gramar.gramar;

public class TagLibSpec {

	private String extensionId;
	private String namespace;

	public TagLibSpec(String extensionId, String namespace) {
		super();
		this.extensionId = extensionId;
		this.namespace = namespace;
	}
	
	public String getExtensionId() {
		return extensionId;
	}
	
	public void setExtensionId(String extensionId) {
		this.extensionId = extensionId;
	}
	
	public String getNamespace() {
		return namespace;
	}
	
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
