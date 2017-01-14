package com.gerken.xaa.mpe.editor;

import java.util.ArrayList;
import java.util.Iterator;

import com.gerken.xaa.mpe.constraint.Constraint;
import com.gerken.xaa.mpe.constraint.ConstraintFailure;
import com.gerken.xaa.mpe.constraint.ConstraintSet;
import com.gerken.xaa.mpe.constraint.IConstraintListener;
import com.gerken.xaa.mpe.core.AbstractDetailsSection;
import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.ModelAccess;
import com.gerken.xaa.mpe.core.SectionMessageAreaComposite;
import com.gerken.xaa.mpe.domain.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo; 
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.w3c.dom.Node;

public class ReplacementDetailsSection extends AbstractDetailsSection implements IConstraintListener {

	private Text   widget_oldString;
	private String value_oldString = "";
	private Text   widget_newString;
	private String value_newString = "";

	private boolean _loading = false;
	private SectionMessageAreaComposite mcomp;
	
	public ReplacementDetailsSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void createClient(Section section, FormToolkit toolkit) {
		section.setText("Replacement Details"); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription("Set the replacement properties below"); 
		
		Composite client = toolkit.createComposite(section);
		TableWrapLayout layout = new TableWrapLayout();
		layout.leftMargin = layout.rightMargin = toolkit.getBorderStyle() != SWT.NULL ? 0 : 2;
		layout.numColumns = 3;
		client.setLayout(layout);
		section.setClient(client);

		mcomp = new SectionMessageAreaComposite();
		mcomp.createControl(client, toolkit, 3);
		
		Label label;
		TableWrapData td;
		
//		IActionBars actionBars = getPage().getMpeEditor().getEditorSite().getActionBars();

// Begin oldString layout and validation

		label = toolkit.createLabel(client, "Replaced String");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_oldString = toolkit.createText(client, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_oldString, "com.gerken.xaa.mpe.replacement_oldString");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_oldString.setLayoutData(td);

		widget_oldString.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_oldString = widget_oldString.getText();
				setAttribute(getSourceNode(),"oldString",value_oldString);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"oldString");
			}
		});

// End oldString layout and validation
// Begin newString layout and validation

		label = toolkit.createLabel(client, "New String");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_newString = toolkit.createText(client, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_newString, "com.gerken.xaa.mpe.replacement_newString");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_newString.setLayoutData(td);

		widget_newString.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_newString = widget_newString.getText();
				setAttribute(getSourceNode(),"newString",value_newString);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"newString");
			}
		});

// End newString layout and validation
		
		toolkit.paintBordersFor(client);
		
		getPage().getMpeEditor().getConstraintManager().addConstraintListener(this);
	}

	protected void commit() {
		super.commit();
	}

	public String getOldString() {
		return value_oldString;
	}

	public String getNewString() {
		return value_newString;
	}

	public void loadFrom(Node source) {
		_loading = true;
		setSourceNode(source);
		value_oldString = ModelAccess.getAttribute(getSourceNode(),"@oldString");
		value_newString = ModelAccess.getAttribute(getSourceNode(),"@newString");

		updateScreen();
		enableSection(true);
		displayFirst(getPage().getMpeEditor().getConstraintManager().getCurrentProblems());
		_loading = false;
	}

	public void clear() {
		clearSourceNode();
		value_oldString = "";
		value_newString = "";
		
		updateScreen();
		enableSection(false);
		mcomp.resetError();
	}

	private void enableSection(boolean enable) {
		widget_oldString.setEnabled(enable);
		widget_newString.setEnabled(enable);
	}

	private void updateScreen() {
	
		widget_oldString.setText(value_oldString);

		widget_newString.setText(value_newString);

	}
	
	public void markDirty() {
		super.markDirty();
		((ReplacementPage)getPage()).getReplacementListSection().refreshSelected();
	}		

	protected boolean isPrimary() {
		return false;
	}

	public static ConstraintSet getConstraintSet() {
		ConstraintSet cs = new ConstraintSet(XaaEditor.Section_ReplacementDetails);
		cs.addConstraint(new Constraint("oldString",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("newString",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("newString",Constraint.CONSTRAINT_CONTAINS_XPATH));
		return cs;
	}
	
	public void constraintsChecked(ArrayList<ConstraintFailure> problems) {
		displayFirst(problems);
	}
	
	public void displayFirst(ArrayList<ConstraintFailure> problems) {
		if (getSourceNode() == null) { return; }
		String currentName = getSourceNode().getNodeName();
		Iterator<ConstraintFailure> iter = problems.iterator();
		while (iter.hasNext()) {
			ConstraintFailure candidate = iter.next();
			if (candidate.getReportingConstraintSet() == XaaEditor.Section_ReplacementDetails ) {
				if (getSourceNode() == candidate.getTarget()) {
					mcomp.setError(candidate.getMessage());
					return;
				}
			}
		}
		mcomp.resetError();
	}
	
}
