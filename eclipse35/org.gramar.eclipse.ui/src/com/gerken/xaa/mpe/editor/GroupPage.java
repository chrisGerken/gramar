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

public class GroupPage extends AbstractFormPage {

	GroupTreeSection sectionGroupTreeSection;
	GroupDetailsSection sectionGroupDetailsSection;
	GroupTextSection sectionGroupTextSection;
 
	public static String PAGE_ID = "GROUP";
	
	public GroupPage(FormEditor editor) {
		super(editor, PAGE_ID, "Groups");
	}

	protected String getHelpResource() {
		return "/com.gerken.xaa.mpe/html/guide/mp_editor/Group.htm"; //$NON-NLS-1$
	}
	
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Group"); 
		fillBody(managedForm, toolkit);
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(form.getBody(), "com.gerken.xaa.mpe.Xaa.Group_page");		
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

		sectionGroupTreeSection = new GroupTreeSection(this,column);
		managedForm.addPart(sectionGroupTreeSection);


			// Populate column 3
			
		column = toolkit.createComposite(body);
		layout = new TableWrapLayout();
		layout.verticalSpacing = 20;
		column.setLayout(layout);
		column.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		sectionGroupDetailsSection = new GroupDetailsSection(this,column);
		managedForm.addPart(sectionGroupDetailsSection);

		sectionGroupTextSection = new GroupTextSection(this,column);
		managedForm.addPart(sectionGroupTextSection);

		
	}
	
	public GroupTreeSection getGroupTreeSection() {
		return sectionGroupTreeSection;
	}
	
	public GroupDetailsSection getGroupDetailsSection() {
		return sectionGroupDetailsSection;
	}
	
	public GroupTextSection getGroupTextSection() {
		return sectionGroupTextSection;
	}

	public void markStale() {
		if (sectionGroupTreeSection != null) { sectionGroupTreeSection.markStale(); }
		if (sectionGroupDetailsSection != null) { sectionGroupDetailsSection.markStale(); }
		if (sectionGroupTextSection != null) { sectionGroupTextSection.markStale(); }
	}		
	
	public void setSelection(Node node) {

		sectionGroupTreeSection.setSelection(node);
		
	}				
			
}
