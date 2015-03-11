package org.gramar.platform;

import org.gramar.IGramarApplicationStatus;
import org.gramar.IGramarContext;


public class GramarApplicationResult implements IGramarApplicationStatus {

	private IGramarContext  context;
	private int status;
	
	public GramarApplicationResult(IGramarContext context) {
		this.context = context;
	}
	
	public int getStatus() {
		return status;
	}

	@Override
	public int getModelAccesses() {
		return context.getModelAccessCount();
	}

}
