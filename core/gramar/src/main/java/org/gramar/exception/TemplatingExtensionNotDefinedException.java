package org.gramar.exception;

public class TemplatingExtensionNotDefinedException extends GramarException {

	private String namespace;
	
	public TemplatingExtensionNotDefinedException(String namespace) {
		super("No templating extension with prefix "+namespace+" has been defined");
		this.namespace = namespace;
	}

	public String getNamespace() {
		return namespace;
	}

}
