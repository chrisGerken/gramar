package org.gramar.eclipse.platform;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.gramar.IGramarApplicationStatus;
import org.gramar.IGramarPlatform;
import org.gramar.IGramarStatus;
import org.gramar.IModel;
import org.gramar.model.XmlModel;
import org.gramar.platform.GramarPlatform;
import org.gramar.plugin.ClasspathPluginSource;

public class EclipsePlatform extends GramarPlatform implements IGramarPlatform {

	private static MessageConsole console;
	
	public static final String CONSOLE_NAME = "gramar.eclipse.console";
	
	private static final Color normalColor = new Color(Display.getCurrent(), new RGB(0, 0, 0));
	private static final Color debugColor = new Color(Display.getCurrent(), new RGB(80, 80, 80));
	private static final Color infoColor = new Color(Display.getCurrent(), new RGB(0, 0, 0));
	private static final Color errorColor = new Color(Display.getCurrent(), new RGB(200, 0, 0));
	private static final Color severeColor = new Color(Display.getCurrent(), new RGB(255, 0, 0));
	private static final Color warningColor = new Color(Display.getCurrent(), new RGB(0, 0, 180));
	
	public EclipsePlatform() {
		super();
	}

	@Override
	protected void loadExtensions() {
		addPluginSource(new EclipseWorkspacePluginSource());
		addPluginSource(new EclipseDeployedPluginSource());
		addPluginSource(new ClasspathPluginSource());
		setDefaultFileStore(new EclipseFileStore());
	}

	public static IGramarApplicationStatus apply(String modelContent, String gramarId) throws Exception {

		EclipsePlatform platform = new EclipsePlatform();
		EclipseFileStore fileStore = (EclipseFileStore) platform.getDefaultFileStore();
		IModel model = new XmlModel(modelContent);
		return platform.apply(model, gramarId, fileStore);

	}
	
	public static MessageConsole getConsole() {
		if (console == null) {
			console = findConsole(CONSOLE_NAME);
		}
		return console;
	}
	
	private static MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName()))
				return (MessageConsole) existing[i];	
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[]{myConsole});
		return myConsole;
	}
	
	public static void log(String message, int severity) {
		MessageConsoleStream stream = getConsole().newMessageStream();
		if (severity==IGramarStatus.SEVERITY_WARN) {
			stream.setColor(warningColor);
		} else if (severity==IGramarStatus.SEVERITY_INFO) {
			stream.setColor(infoColor);
		} else if (severity==IGramarStatus.SEVERITY_NONE) {
			stream.setColor(normalColor);
		} else if (severity==IGramarStatus.SEVERITY_DEBUG) {
			stream.setColor(debugColor);
		} else if (severity==IGramarStatus.SEVERITY_ERROR) {
			stream.setColor(errorColor);
		} else if (severity==IGramarStatus.SEVERITY_SEVERE) {
			stream.setColor(severeColor);
		} else {
			stream.setColor(normalColor);
		}
		stream.print(message+"\n");
		try { 
//			stream.setColor(normalColor);
			stream.close(); 
		} catch (Throwable t) { }
	}
	
}
