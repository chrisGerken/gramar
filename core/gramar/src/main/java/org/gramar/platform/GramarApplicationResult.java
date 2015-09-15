package org.gramar.platform;

import org.gramar.IFileStore;
import org.gramar.IGramar;
import org.gramar.IGramarApplicationStatus;
import org.gramar.IGramarContext;
import org.gramar.IGramarPlatform;
import org.gramar.IGramarStatus;
import org.gramar.IModel;
import org.gramar.exception.GramarException;


public class GramarApplicationResult implements IGramarApplicationStatus {

	private IGramarContext  context;
	private String			content;
	
	public GramarApplicationResult(IGramarContext context, String content) {
		this.context = context;
		this.content = content;
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

	@Override
	public String mainProductionResult() {
		return content;
	}

	@Override
	public IGramarApplicationStatus reApply(IModel model) throws GramarException {
		IGramarPlatform platform = context.getPlatform();
		IGramar gramar = context.getGramar();
		IFileStore fileStore = context.getFileStore();
		return platform.apply(model, gramar, fileStore);
	}

}
