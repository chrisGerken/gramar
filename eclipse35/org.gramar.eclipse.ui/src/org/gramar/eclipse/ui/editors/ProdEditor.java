package org.gramar.eclipse.ui.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class ProdEditor extends TextEditor {

	private ColorManager colorManager;

	public ProdEditor() {
		super();
		colorManager = new ColorManager();
		setSourceViewerConfiguration(new ProdConfiguration(colorManager));
		setDocumentProvider(new ProdDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
