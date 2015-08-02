package org.gramar.eclipse.platform;

import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.gramar.IGramarApplicationStatus;
import org.gramar.IGramarPlatform;
import org.gramar.IModel;
import org.gramar.model.XmlModel;
import org.gramar.platform.GramarPlatform;
import org.gramar.plugin.ClasspathPluginSource;

public class EclipsePlatform extends GramarPlatform implements IGramarPlatform {

	private static MessageConsole console;
	
	public static final String CONSOLE_NAME = "gramar.eclipse.console";
	
	public EclipsePlatform() {
		super();
	}

	@Override
	protected void loadExtensions() {
		addPluginSource(new EclipseWorkspacePluginSource());
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
	
	public static void log(String message) {
		MessageConsoleStream stream = getConsole().newMessageStream();
		stream.print(message+"\n");
		try { stream.close(); } catch (Throwable t) { }
	}
	
}
