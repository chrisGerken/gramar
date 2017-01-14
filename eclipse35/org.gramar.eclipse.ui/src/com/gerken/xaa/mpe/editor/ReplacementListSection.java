package com.gerken.xaa.mpe.editor;

import java.util.ArrayList;

import com.gerken.xaa.mpe.constraint.ConstraintFailure;
import com.gerken.xaa.mpe.constraint.IConstraintListener;

import com.gerken.xaa.mpe.core.AbstractListSection;
import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.SectionMessageAreaComposite;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.eclipse.ui.PlatformUI;

public class ReplacementListSection extends AbstractListSection implements
		IConstraintListener {

	public ReplacementListSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected String getListTitle() {
		return "Replacement List";
	}

	public String getListDescription() {
		return "Manage the replacement elements below";
	}

	public String getSourceExpression() {
		return ".";
	}

	protected String getItemName(Object element) {
		return bind((Node) element, "{@oldString}");
	}

	protected String getExtractorExpression() {
		return "replacement";
	}

	protected String getTargetName() {
		return "replacement";
	}

	protected boolean isSectionExpanded() {
		return true;
	}

	public int getTableHightHint() {
		return 300;
	}

	protected void addNode() {

		Element kid = getSourceNode().getOwnerDocument().createElement(
				getTargetName());
		getSourceNode().appendChild(kid);
		// set default values
		kid.setAttribute("oldString", "oldString");
		kid.setAttribute("newString", "newString");
		// check what was entered
		getPage().getMpeEditor().elementAdded(kid);

		// Begin custom initializations

		// End custom initializations
		refreshList();
		select(kid);
		updateButtons();

	}

	protected void notifyDependents(Node node) {
		((ReplacementPage) getPage()).getReplacementDetailsSection().loadFrom(
				node);
		((ReplacementPage) getPage()).getReplacementToolsSection().loadFrom(
				node);
	}

	protected void clearDependents() {
		if (getPage().isDirty()) {
			((ReplacementPage) getPage()).getReplacementDetailsSection().clear();
			((ReplacementPage) getPage()).getReplacementToolsSection().clear();
		}
	}

	protected boolean isPrimary() {
		return true;
	}

	public void setDependentSelection(Node node) {
		((ReplacementPage) getPage()).getReplacementDetailsSection()
				.setSelection(node);
		((ReplacementPage) getPage()).getReplacementToolsSection()
				.setSelection(node);
	}

	public void constraintsChecked(ArrayList<ConstraintFailure> problems) {

	}

}
