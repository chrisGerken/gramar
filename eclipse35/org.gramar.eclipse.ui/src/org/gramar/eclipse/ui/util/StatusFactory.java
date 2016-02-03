package org.gramar.eclipse.ui.util;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.gramar.IGramarContext;
import org.gramar.IGramarStatus;
import org.gramar.eclipse.ui.Activator;
import org.gramar.platform.GramarStatus;

public class StatusFactory {

	public static IStatus status(IGramarStatus status) {
		
		String message = status.getMessage();
		Throwable cause = status.getCause();
		String pluginId = Activator.PLUGIN_ID;
		
		int severity = IStatus.OK;
		if (status.getSeverity() == GramarStatus.SEVERITY_DEBUG ) { severity = IStatus.OK; }
		if (status.getSeverity() == GramarStatus.SEVERITY_INFO ) { severity = IStatus.INFO; }
		if (status.getSeverity() == GramarStatus.SEVERITY_WARN ) { severity = IStatus.WARNING; }
		if (status.getSeverity() == GramarStatus.SEVERITY_ERROR ) { severity = IStatus.ERROR; }
		if (status.getSeverity() == GramarStatus.SEVERITY_SEVERE ) { severity = IStatus.ERROR; }
			
		if (message == null) {
			try { message = cause.getMessage(); } catch (Throwable t) { }
		}

		if (cause == null) {
			return new Status(severity, pluginId, message);
		}
		return new Status(severity, pluginId, message, cause);
		
	}
	
	public static MultiStatus status(IGramarContext context, int code, String message) {
		List<IGramarStatus> status = context.getStati();
		IStatus stati[] = new IStatus[status.size()];
		for (int i = 0; i < stati.length; i++) {
			stati[i] = status(status.get(i));
		}
		return new MultiStatus(Activator.PLUGIN_ID, code, stati, message, (Throwable)null);
	}

}
