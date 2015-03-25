package org.gramar.platform;

import org.gramar.IGramarApplicationStatus;
import org.gramar.IGramarContext;
import org.gramar.IGramarStatus;


public class GramarApplicationResult implements IGramarApplicationStatus {

	private IGramarContext  context;
	
	public GramarApplicationResult(IGramarContext context) {
		this.context = context;
	}
	
	public int getStatus() {
		return context.getMaxStatus();
	}
	
	public boolean hadErrors() {
		return context.getMaxStatus() <= IGramarStatus.SEVERITY_ERROR;
	}

	@Override
	public int getModelAccesses() {
		return context.getModelAccessCount();
	}

}
