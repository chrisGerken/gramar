package org.gramar.platform;

import org.gramar.IGramarApplicationStatus;
import org.gramar.IGramarContext;


public class GramarApplicationResult implements IGramarApplicationStatus {

	private int status;
	
	public GramarApplicationResult(IGramarContext context) {

	}
	
	public int getStatus() {
		return status;
	}

}
