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

public class OverviewPage extends AbstractFormPage {

	XformDetailsSection sectionXformDetailsSection;
	GroupTocSection sectionGroupTocSection;
	OverviewToolslSection sectionOverviewToolsSection;
	ProblemSection sectionAllProblemsSection;
 
	public static String PAGE_ID = "OVERVIEW";
	
	public OverviewPage(FormEditor editor) {
		super(editor, PAGE_ID, "Overview");
	}

	protected String getHelpResource() {
		return "/com.gerken.xaa.mpe/html/guide/mp_editor/Overview.htm"; //$NON-NLS-1$
	}
	
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Overview"); 
		fillBody(managedForm, toolkit);
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(form.getBody(), "com.gerken.xaa.mpe.Xaa.Overview_page");		
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

		sectionXformDetailsSection = new XformDetailsSection(this,column);
		managedForm.addPart(sectionXformDetailsSection);

		sectionGroupTocSection = new GroupTocSection(this,column);
		managedForm.addPart(sectionGroupTocSection);


			// Populate column 3
			
		column = toolkit.createComposite(body);
		layout = new TableWrapLayout();
		layout.verticalSpacing = 20;
		column.setLayout(layout);
		column.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		sectionOverviewToolsSection = new OverviewToolslSection(this,column);
		managedForm.addPart(sectionOverviewToolsSection);

		sectionAllProblemsSection = new ProblemSection(this,column);
		managedForm.addPart(sectionAllProblemsSection);

		
	}
	
	public XformDetailsSection getXformDetailsSection() {
		return sectionXformDetailsSection;
	}
	
	public GroupTocSection getGroupTocSection() {
		return sectionGroupTocSection;
	}
	
	public OverviewToolslSection getOverviewToolsSection() {
		return sectionOverviewToolsSection;
	}
	
	public ProblemSection getProblemSection() {
		return sectionAllProblemsSection;
	}

	public void markStale() {
		if (sectionXformDetailsSection != null) { sectionXformDetailsSection.markStale(); }
		if (sectionGroupTocSection != null) { sectionGroupTocSection.markStale(); }
		if (sectionOverviewToolsSection != null) { sectionOverviewToolsSection.markStale(); }
		if (sectionAllProblemsSection != null) { sectionAllProblemsSection.markStale(); }
	}		
	
	public void setSelection(Node node) {

		sectionXformDetailsSection.setSelection(node);
		sectionGroupTocSection.setSelection(node);
		sectionOverviewToolsSection.setSelection(node);
		sectionAllProblemsSection.setSelection(node);
		
	}				
			
}
