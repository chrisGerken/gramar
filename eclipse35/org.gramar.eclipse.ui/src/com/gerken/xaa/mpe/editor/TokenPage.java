package com.gerken.xaa.mpe.editor;

import com.gerken.xaa.mpe.core.AbstractFormPage;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.w3c.dom.Node;

public class TokenPage extends AbstractFormPage {

	GroupTreeTokenSection sectionGroupTreeTokenSection;
	TokenListSection sectionTokenListSection;
	TokenDetailsSection sectionTokenDetailsSection;
	TokenTextSection sectionTokenTextSection;
	TokenToolsSection sectionTokenToolsSection;
 
	public static String PAGE_ID = "TOKEN";
	
	public TokenPage(FormEditor editor) {
		super(editor, PAGE_ID, "Tokens");
	}

	protected String getHelpResource() {
		return "/com.gerken.xaa.mpe/html/guide/mp_editor/Token.htm"; //$NON-NLS-1$
	}

	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Token"); 
		fillBody(managedForm, toolkit);
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(form.getBody(), "com.gerken.xaa.mpe.Xaa.Token_page");		
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

		sectionGroupTreeTokenSection = new GroupTreeTokenSection(this,column);
		managedForm.addPart(sectionGroupTreeTokenSection);

		sectionTokenListSection = new TokenListSection(this,column);
		managedForm.addPart(sectionTokenListSection);


			// Populate column 3
			
		column = toolkit.createComposite(body);
		layout = new TableWrapLayout();
		layout.verticalSpacing = 20;
		column.setLayout(layout);
		column.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		sectionTokenDetailsSection = new TokenDetailsSection(this,column);
		managedForm.addPart(sectionTokenDetailsSection);

		sectionTokenToolsSection = new TokenToolsSection(this,column);
		managedForm.addPart(sectionTokenToolsSection);

		sectionTokenTextSection = new TokenTextSection(this,column);
		managedForm.addPart(sectionTokenTextSection);

		
	}
	
	public GroupTreeTokenSection getGroupTreeTokenSection() {
		return sectionGroupTreeTokenSection;
	}
	
	public TokenListSection getTokenListSection() {
		return sectionTokenListSection;
	}
	
	public TokenDetailsSection getTokenDetailsSection() {
		return sectionTokenDetailsSection;
	}
	
	public TokenToolsSection getTokenToolsSection() {
		return sectionTokenToolsSection;
	}
	
	public TokenTextSection getTokenTextSection() {
		return sectionTokenTextSection;
	}

	public void markStale() {
		if (sectionGroupTreeTokenSection != null) { sectionGroupTreeTokenSection.markStale(); }
		if (sectionTokenListSection != null) { sectionTokenListSection.markStale(); }
		if (sectionTokenDetailsSection != null) { sectionTokenDetailsSection.markStale(); }
		if (sectionTokenToolsSection != null) { sectionTokenToolsSection.markStale(); }
		if (sectionTokenTextSection != null) { sectionTokenTextSection.markStale(); }
	}		
	
	public void setSelection(Node node) {

		sectionGroupTreeTokenSection.setSelection(node);
		
	}				
			
}
