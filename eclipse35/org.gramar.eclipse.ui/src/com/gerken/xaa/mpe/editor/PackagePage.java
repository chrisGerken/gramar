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

public class PackagePage extends AbstractFormPage {

	PackageListSection sectionPackageListSection;
	PackageDetailsSection sectionPackageDetailsSection;
	PackageToolsSection sectionPackageToolsSection;
 
	public static String PAGE_ID = "PACKAGE";
	
	public PackagePage(FormEditor editor) {
		super(editor, PAGE_ID, "Packages");
	}

	protected String getHelpResource() {
		return "/com.gerken.xaa.mpe/html/guide/mp_editor/Package.htm"; //$NON-NLS-1$
	}
	
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Package"); 
		fillBody(managedForm, toolkit);
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(form.getBody(), "com.gerken.xaa.mpe.Xaa.Package_page");		
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

		sectionPackageListSection = new PackageListSection(this,column);
		managedForm.addPart(sectionPackageListSection);


			// Populate column 3
			
		column = toolkit.createComposite(body);
		layout = new TableWrapLayout();
		layout.verticalSpacing = 20;
		column.setLayout(layout);
		column.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		sectionPackageDetailsSection = new PackageDetailsSection(this,column);
		managedForm.addPart(sectionPackageDetailsSection);

		sectionPackageToolsSection = new PackageToolsSection(this,column);
		managedForm.addPart(sectionPackageToolsSection);

		
	}
	
	public PackageListSection getPackageListSection() {
		return sectionPackageListSection;
	}
	
	public PackageDetailsSection getPackageDetailsSection() {
		return sectionPackageDetailsSection;
	}
	
	public PackageToolsSection getPackageToolsSection() {
		return sectionPackageToolsSection;
	}

	public void markStale() {
		if (sectionPackageListSection != null) { sectionPackageListSection.markStale(); }
		if (sectionPackageDetailsSection != null) { sectionPackageDetailsSection.markStale(); }
		if (sectionPackageToolsSection != null) { sectionPackageToolsSection.markStale(); }
	}		
	
	public void setSelection(Node node) {

		sectionPackageListSection.setSelection(node);
		sectionPackageToolsSection.setSelection(node);
		
	}				
			
}
