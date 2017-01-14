package com.gerken.xaa.mpe.editor;

import java.util.ArrayList;
import java.util.Hashtable;

import com.gerken.xaa.mpe.constraint.ConstraintFailure;
import com.gerken.xaa.mpe.constraint.IConstraintListener;

import com.gerken.xaa.mpe.core.AbstractListSection;
import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.ModelAccess;
import com.gerken.xaa.mpe.core.SectionMessageAreaComposite;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element; 
import org.w3c.dom.Node;

import org.eclipse.ui.PlatformUI;

public class PackageListSection extends AbstractListSection implements IConstraintListener {

	public PackageListSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected String getListTitle() {
		return "Packages";
	}

	public String getListDescription() {
		return "Manage the java packages below";
	}

	public String getSourceExpression() {
		return ".";
	}

	protected String getItemName(Object element) {
		return bind((Node)element,"{@name}");
	}

	protected String getExtractorExpression() {
		return "/xform/group[@name!=\"ignored\"]/createFile/javaPkg";
	}

	protected String getTargetName() {
		return "package";
	}

	protected boolean isSectionExpanded() {
		return true;
	}
	
	protected void addNode() {} 

	protected void notifyDependents(Node node) { 
		((PackagePage)getPage()).getPackageDetailsSection().loadFrom(node);
		((PackagePage)getPage()).getPackageToolsSection().loadFrom(node);
	}

	protected void clearDependents() {
		if (getPage().isDirty()) {
			((PackagePage)getPage()).getPackageDetailsSection().clear();
			((PackagePage)getPage()).getPackageToolsSection().clear();
		}
	}

	protected boolean isPrimary() {
		return true;
	}

	public void setDependentSelection(Node node) {
		((PackagePage)getPage()).getPackageDetailsSection().setSelection(node);
		((PackagePage)getPage()).getPackageToolsSection().setSelection(node);
	}
	
	public void constraintsChecked(ArrayList<ConstraintFailure> problems) {
	
	}
	
	public int getTableHightHint() {
		return 300;
	}
	
	protected void refreshList() {
		Node newList[] = ModelAccess.getNodes(getSourceNode(),getExtractorExpression());
		Hashtable<String, Node> ht = new Hashtable<String, Node>();
		for (int i = 0; i < newList.length; i++) {
			String name = ModelAccess.getAttribute(newList[i], "@name");
			ht.put(name,newList[i]);
		}
		newList = new Node[ht.size()];
		ht.values().toArray(newList);
		refreshList(newList);
	}

}
