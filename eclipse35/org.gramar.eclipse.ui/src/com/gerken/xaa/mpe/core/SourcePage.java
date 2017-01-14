package com.gerken.xaa.mpe.core;

import com.gerken.xaa.mpe.editor.XaaEditor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.IFormPage;
import org.w3c.dom.Document;

public class SourcePage extends TextEditor implements IFormPage {

	public static String PAGE_ID = "SOURCE_PAGE";

	private XaaEditor editor;
	private int index;
	private boolean active;
	private Control control;
	private boolean error = false;

	public SourcePage(XaaEditor editor) {
		this.editor = editor;
	}

	public boolean canLeaveThePage() {
		return writeToModel();
	}

	public boolean writeToModel() {
		if (!isDirty()) {
			return true;
		} else {
			error = false;
			try {
				getEditor().setModel(
						getDocumentProvider().getDocument(getEditorInput())
								.get());
				getEditor().markStale();
			} catch (Exception e) {
				MessageDialog.openError(Display.getDefault().getActiveShell(),
						"Model Error", e.getMessage());
				error = true;
			}
			return !error;
		}
	}

	public XaaEditor getEditor() {
		return editor;
	}

	public String getId() {
		return PAGE_ID;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public IManagedForm getManagedForm() {
		return null;
	}

	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		Control[] children = parent.getChildren();
		control = children[children.length - 1];
	}

	public Control getPartControl() {
		return control;
	}

	public void initialize(FormEditor editor) {
		this.editor = (XaaEditor) editor;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isEditor() {
		return false;
	}

	public boolean selectReveal(Object object) {
		return false;
	}

	private Document getModel() {
		return ((XaaEditor) getEditor()).getModel();
	}

	public void writeModel() {
		String contents = ModelFormatter.getInstance().format(getModel());
		getDocumentProvider().getDocument(getEditorInput()).set(contents);
	}

	public void setActive(boolean active) {
		if (error) {
			return;
		}
		this.active = active;
		if (active) {
			writeModel();
		} else {
			try {
				// getEditor().setModel(getDocumentProvider().getDocument(getEditorInput()).get());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		setPartName("Source");
	}

}
