package com.gerken.xaa.mpe.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.core.AbstractFormPage;

public class ReplacementPage extends AbstractFormPage {

	ReplacementListSection sectionReplacementListSection;
	ReplacementDetailsSection sectionReplacementDetailsSection;
	ReplacementToolsSection sectionReplacementToolsSection;

	public static String PAGE_ID = "REPLACEMENT";

	public ReplacementPage(FormEditor editor) {
		super(editor, PAGE_ID, "Replacements");
	}

	protected String getHelpResource() {
		return "/com.gerken.xaa.mpe/html/guide/mp_editor/Replacement.htm"; //$NON-NLS-1$
	}

	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Replacement");
		fillBody(managedForm, toolkit);
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(form.getBody(), "com.gerken.xaa.mpe.Xaa.Replacement_page");
	}

	private void fillBody(IManagedForm managedForm, FormToolkit toolkit) {
		Composite body = managedForm.getForm().getBody();
		TableWrapLayout layout = new TableWrapLayout();
		layout.bottomMargin = 10;
		layout.topMargin = 5;
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		layout.numColumns = 2;
		layout.horizontalSpacing = 10;
		body.setLayout(layout);

		Composite column;

		// Populate column 1

		column = toolkit.createComposite(body);
		layout = new TableWrapLayout();
		layout.verticalSpacing = 20;
		column.setLayout(layout);
		column.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		sectionReplacementListSection = new ReplacementListSection(this, column);
		managedForm.addPart(sectionReplacementListSection);

		// Populate column 2

		column = toolkit.createComposite(body);
		layout = new TableWrapLayout();
		layout.verticalSpacing = 20;
		column.setLayout(layout);
		column.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		sectionReplacementDetailsSection = new ReplacementDetailsSection(this,
				column);
		managedForm.addPart(sectionReplacementDetailsSection);

		sectionReplacementToolsSection = new ReplacementToolsSection(this,
				column);
		managedForm.addPart(sectionReplacementToolsSection);

	}

	public ReplacementListSection getReplacementListSection() {
		return sectionReplacementListSection;
	}

	public ReplacementDetailsSection getReplacementDetailsSection() {
		return sectionReplacementDetailsSection;
	}

	public ReplacementToolsSection getReplacementToolsSection() {
		return sectionReplacementToolsSection;
	}

	public void markStale() {
		if (sectionReplacementListSection != null) {
			sectionReplacementListSection.markStale();
		}
		if (sectionReplacementDetailsSection != null) {
			sectionReplacementDetailsSection.markStale();
		}
	}

	public void setSelection(Node node) {

		sectionReplacementListSection.setSelection(node);

	}

}
