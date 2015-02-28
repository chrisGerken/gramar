package org.gramar;

import org.gramar.exception.GramarException;

public interface IGramar {

	public String getId();
	
	public String getLabel();
	
	public String getProvider();
	
	public String getMainTemplateId();
	
	public ITemplate getTemplate(String id, IGramarContext context) throws GramarException;
	
}
