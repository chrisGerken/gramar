package org.gramar;

public interface IGramarApplicationStatus {

	public int getStatus();
	
	public int getModelAccesses();
	
	public boolean hadErrors();
	
}
