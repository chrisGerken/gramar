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

public class ArtifactPage extends AbstractFormPage {

	GroupTreeArtifactSection sectionGroupTreeArtifactSection;
	ArtifactListSection sectionArtifactListSection;
	NamingDetailsSection sectionNamingDetailsSection;
	FileDetailsSection sectionFileDetailsSection;
	OriginDetailsSection sectionOriginDetailsSection;
	ArtifactTextSection sectionArtifactTextSection;
 
	public static String PAGE_ID = "ARTIFACT";
	
	public ArtifactPage(FormEditor editor) {
		super(editor, PAGE_ID, "Artifacts");
	}

	protected String getHelpResource() {
		return "/com.gerken.xaa.mpe/html/guide/mp_editor/Artifact.htm"; //$NON-NLS-1$
	}
	
	protected void createFormContent(IManagedForm managedForm) {
		super.createFormContent(managedForm);
		ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("Artifact"); 
		fillBody(managedForm, toolkit);
//		PlatformUI.getWorkbench().getHelpSystem().setHelp(form.getBody(), "com.gerken.xaa.mpe.Xaa.Artifact_page");		
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

		sectionGroupTreeArtifactSection = new GroupTreeArtifactSection(this,column);
		managedForm.addPart(sectionGroupTreeArtifactSection);

		sectionArtifactListSection = new ArtifactListSection(this,column);
		managedForm.addPart(sectionArtifactListSection);


			// Populate column 3
			
		column = toolkit.createComposite(body);
		layout = new TableWrapLayout();
		layout.verticalSpacing = 20;
		column.setLayout(layout);
		column.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		sectionNamingDetailsSection = new NamingDetailsSection(this,column);
		managedForm.addPart(sectionNamingDetailsSection);

		sectionFileDetailsSection = new FileDetailsSection(this,column);
		managedForm.addPart(sectionFileDetailsSection);

		sectionOriginDetailsSection = new OriginDetailsSection(this,column);
		managedForm.addPart(sectionOriginDetailsSection);

		sectionArtifactTextSection = new ArtifactTextSection(this,column);
		managedForm.addPart(sectionArtifactTextSection);

		
	}
	
	public GroupTreeArtifactSection getGroupTreeArtifactSection() {
		return sectionGroupTreeArtifactSection;
	}
	
	public ArtifactListSection getArtifactListSection() {
		return sectionArtifactListSection;
	}
	
	public NamingDetailsSection getNamingDetailsSection() {
		return sectionNamingDetailsSection;
	}
	
	public FileDetailsSection getFileDetailsSection() {
		return sectionFileDetailsSection;
	}
	
	public OriginDetailsSection getOriginDetailsSection() {
		return sectionOriginDetailsSection;
	}
	
	public ArtifactTextSection getArtifactTextSection() {
		return sectionArtifactTextSection;
	}

	public void markStale() {
		if (sectionGroupTreeArtifactSection != null) { sectionGroupTreeArtifactSection.markStale(); }
		if (sectionArtifactListSection != null) { sectionArtifactListSection.markStale(); }
		if (sectionNamingDetailsSection != null) { sectionNamingDetailsSection.markStale(); }
		if (sectionFileDetailsSection != null) { sectionFileDetailsSection.markStale(); }
		if (sectionOriginDetailsSection != null) { sectionOriginDetailsSection.markStale(); }
		if (sectionArtifactTextSection != null) { sectionArtifactTextSection.markStale(); }
	}		
	
	public void setSelection(Node node) {

		sectionGroupTreeArtifactSection.setSelection(node);
		
	}				
			
}
