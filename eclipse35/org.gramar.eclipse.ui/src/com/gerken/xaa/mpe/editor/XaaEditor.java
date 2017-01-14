package com.gerken.xaa.mpe.editor;

import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.gramar.eclipse.ui.Activator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.constraint.ConstraintManager;
import com.gerken.xaa.mpe.core.ModelAccess;
import com.gerken.xaa.mpe.core.ModelLoader;
import com.gerken.xaa.mpe.core.SourcePage;
import com.gerken.xaa.mpe.domain.PolymorphicKeysEnumeration;
import com.gerken.xaa.mpe.domain.ProjectsEnumeration;

public class XaaEditor extends FormEditor {
	
	private	Document 			modelDoc;
	private ConstraintManager	constraintManager = new ConstraintManager();
	private	int					sourcePageIndex;
	
	private	OverviewPage	overviewPage;
	private	GroupPage	groupPage;
	private	ArtifactPage	artifactPage;
	private	TokenPage	tokenPage;
	private	PackagePage	packagePage;
	private	ReplacementPage	replacementPage;
	private SourcePage  _sourcePage;

	private ProjectsEnumeration domainProjects = new ProjectsEnumeration(this);
	private PolymorphicKeysEnumeration domainPolymorphicKeys = new PolymorphicKeysEnumeration(this);

	public static int Section_XformDetails = 1;
	public static int Section_GroupDetails = 2;
	public static int Section_NamingDetails = 3;
	public static int Section_FileDetails = 4;
	public static int Section_OriginDetails = 5;
	public static int Section_ReplacementDetails = 6;
	public static int Section_TokenDetails = 7;
	public static int Section_PackageDetails = 8;

	public XaaEditor() {
		super();
	}

	protected void addPages() {
		
		getToolkit().getColors().createColor(getRedKey(), 200, 10, 10);
		getToolkit().getColors().createColor(getBlueKey(), 10, 10, 200);
		getToolkit().getColors().createColor(getGreenKey(), 10, 200, 10);
		getToolkit().getColors().createColor(getYellowKey(), 200, 200, 10);

		modelDoc = getModel();
		try {
			overviewPage = new OverviewPage(this);
			addPage(overviewPage);
			
			groupPage = new GroupPage(this);
			addPage(groupPage);
			
			artifactPage = new ArtifactPage(this);
			addPage(artifactPage);
			
			tokenPage = new TokenPage(this);
			addPage(tokenPage);
			
			packagePage = new PackagePage(this);
			addPage(packagePage);
			
			replacementPage = new ReplacementPage(this);
			addPage(replacementPage);
			
			_sourcePage = new SourcePage(this);
			_sourcePage.init(getEditorSite(), getEditorInput());
			sourcePageIndex = addPage(_sourcePage);
			
		} catch (PartInitException e) {
			e.printStackTrace();
		} 
	}

	public String getRedKey() {
		return "mpe_red";
	}

	public String getBlueKey() {
		return "mpe_blue";
	}

	public String getGreenKey() {
		return "mpe_green";
	}

	public String getYellowKey() {
		return "mpe_yellow";
	}

	private boolean isSourcePageActive() {
		return getActivePage() == sourcePageIndex;
	}
	
	public void doSave(IProgressMonitor monitor) {
		if (isSourcePageActive()) {
			_sourcePage.writeToModel();
		} else {
			_sourcePage.writeModel();
		}
		_sourcePage.doSave(monitor);
	}

	public void doSaveAs() {}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public Document getModel() {
		if (modelDoc == null) {
			modelDoc = buildModel();
			if (modelDoc.getDocumentElement().getNodeName() != "xform") {
				modelDoc = ModelLoader.getInstance().buildEmptyModel();
			}
		}
		return modelDoc;
	}
	
	public Element getRoot() {
		return getModel().getDocumentElement();
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		super.init(site, input);
		if (input instanceof IFileEditorInput) {
			setPartName(((IFileEditorInput)input).getFile().getProject().getName());
		} else {
			setPartName("Gramar");
		}
	}

	private Document buildModel() {
		Document newDoc = null;
		if (getEditorInput() instanceof IFileEditorInput) {
			try {
				IFileEditorInput modelFile = (IFileEditorInput)getEditorInput();
				IFile ifile = modelFile.getFile();
				InputStream is = ifile.getContents();
				newDoc = ModelLoader.getInstance().buildModel(is);
			} catch (Exception e) {
				newDoc = ModelLoader.getInstance().buildEmptyModel();
			}
			getConstraintManager().modelChanged(newDoc);
		}
		
		return newDoc;
	}

	public void markStale() {
		overviewPage.markStale();
		groupPage.markStale();
		artifactPage.markStale();
		tokenPage.markStale();
		replacementPage.markStale();
	}
	
	public void setModel(String content) throws Exception {
		modelDoc = ModelLoader.getInstance().buildModel(content);
	}

	public ProjectsEnumeration getProjectsDomain() {
		return domainProjects;
	}

	public PolymorphicKeysEnumeration getPolymorphicKeysDomain() {
		return domainPolymorphicKeys;
	}
	

	public void propertyChanged(Node target, String field) {
		if (target == null) { return; }
		constraintManager.modelChanged(getModel());
		Activator.getXformAccess().setModel(getXformProject(),getXformNode()); 
	}

	public void elementAdded(Node target) {
		if (target == null) { return; }
		constraintManager.elementAdded(target);
	}

	public void modelChanged() {
		constraintManager.modelChanged(getModel());
	}
	
	public ConstraintManager getConstraintManager() {
		return constraintManager;
	}

	public boolean isDirty() {
		return true;
	}
	
	private Node getXformNode() {
		return ModelAccess.getNodes(getModel(),"xform")[0];
	}
	
	public String getNextID() {
		Node xform = getModel();
		xform = ModelAccess.getNodes(xform,"xform")[0];
		String nextID = ModelAccess.getAttribute(xform, "@nextID");
		int val = Integer.parseInt(nextID);
		val++;
		String nextNextID = String.valueOf(val);
		ModelAccess.setAttribute(xform, "nextID", nextNextID);
		return nextID;
	}

	public String getXformProject() {
		Node xform = getModel();
		xform = ModelAccess.getNodes(xform,"xform")[0];
		String id = ModelAccess.getAttribute(xform, "@xformProject");
		return id;
	}

		// Begin custom editor logic
		
		// End custom editor logic
	
}
