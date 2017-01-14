package com.gerken.xaa.mpe.core;

import com.gerken.xaa.mpe.editor.XaaEditor;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class AbstractFormPage extends FormPage {

	private boolean newStyleHeader = true;

	public AbstractFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}
	
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		//form.setBackgroundImage(PDEPlugin.getDefault().getLabelProvider().get(
		//		PDEPluginImages.DESC_FORM_BANNER));
		FormToolkit toolkit = managedForm.getToolkit();
		FormColors colors = toolkit.getColors();
		form.getForm().setSeparatorColor(colors.getColor(FormColors.TB_BORDER));
		if (newStyleHeader) {
			colors.initializeSectionToolBarColors();
			Color gbg = colors.getColor(FormColors.TB_GBG);
			Color bg = colors.getBackground();
			form.getForm().setTextBackground(new Color[]{bg, gbg}, new int [] {100}, true);
			form.getForm().setSeparatorVisible(true);
		}
		final String href = getHelpResource();
		if (href != null) {
			IToolBarManager manager = form.getToolBarManager();
			Action helpAction = new Action("help") { //$NON-NLS-1$
				public void run() {
					BusyIndicator.showWhile(form.getDisplay(), new Runnable() {
						public void run() {
							PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(href);
						}
					});
				}
			};
			helpAction.setToolTipText("Help"); 
//			helpAction.setImageDescriptor(PDEPluginImages.DESC_HELP);
			manager.add(helpAction);
			form.updateToolBar();
		}
	}

	public XaaEditor getMpeEditor() {
		return (XaaEditor) getEditor();
	}
	
	protected abstract String getHelpResource();
	
	public Document getModel() {
		return getMpeEditor().getModel();
	}

	public Element getRoot() {
		return getMpeEditor().getRoot();
	}

	public abstract void setSelection(Node node);

}
