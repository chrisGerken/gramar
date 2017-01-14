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

public class GroupDetailsSection extends AbstractDetailsSection implements IConstraintListener {

	private Text   widget_name;
	private String value_name = "";
	private Button   widget_impliedSingleton;
	private String   value_impliedSingleton = "";
	private Button   widget_polymorphicSingleton;
	private String   value_polymorphicSingleton = "";
	private Combo   widget_polymorphicKey;
	private String value_polymorphicKey = "";

	private boolean _loading = false;
	private SectionMessageAreaComposite mcomp;
	
	public GroupDetailsSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void createClient(Section section, FormToolkit toolkit) {
		section.setText("Group Details"); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription("Set the group properties below"); 
		
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

// Begin name layout and validation

		label = toolkit.createLabel(client, "Name");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_name = toolkit.createText(client, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_name, "com.gerken.xaa.mpe.group_name");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_name.setLayoutData(td);

		widget_name.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_name = widget_name.getText();
				setAttribute(getSourceNode(),"name",value_name);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"name");
			}
		});

// End name layout and validation
// Begin impliedSingleton layout and validation

		label = toolkit.createLabel(client, "Implied Singleton");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_impliedSingleton = toolkit.createButton(client, "Exactly one instance created automatically", SWT.CHECK);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_impliedSingleton, "com.gerken.xaa.mpe.group_impliedSingleton");
		widget_impliedSingleton.setText("Exactly one instance created automatically");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_impliedSingleton.setLayoutData(td);

		widget_impliedSingleton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
			public void widgetSelected(SelectionEvent arg0) { exec(); }
			private void exec() {
				if (_loading) { return; }
				value_impliedSingleton = String.valueOf(widget_impliedSingleton.getSelection());
				setAttribute(getSourceNode(),"impliedSingleton",value_impliedSingleton);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"impliedSingleton");
			}
		});

// End impliedSingleton layout and validation
// Begin polymorphicSingleton layout and validation

		label = toolkit.createLabel(client, "Polymorphic Singleton");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_polymorphicSingleton = toolkit.createButton(client, "Exactly one instance created per parent attribute", SWT.CHECK);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_polymorphicSingleton, "com.gerken.xaa.mpe.group_polymorphicSingleton");
		widget_polymorphicSingleton.setText("Exactly one instance created per parent attribute");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_polymorphicSingleton.setLayoutData(td);

		widget_polymorphicSingleton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
			public void widgetSelected(SelectionEvent arg0) { exec(); }
			private void exec() {
				if (_loading) { return; }
				value_polymorphicSingleton = String.valueOf(widget_polymorphicSingleton.getSelection());
				setAttribute(getSourceNode(),"polymorphicSingleton",value_polymorphicSingleton);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"polymorphicSingleton");
			}
		});

// End polymorphicSingleton layout and validation
// Begin polymorphicKey layout and validation

		label = toolkit.createLabel(client, "Polymorphic Key");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_polymorphicKey = new Combo(client,SWT.NONE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_polymorphicKey, "com.gerken.xaa.mpe.group_polymorphicKey");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_polymorphicKey.setLayoutData(td);

		widget_polymorphicKey.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
			public void widgetSelected(SelectionEvent arg0) { exec(); }
			private void exec() {
				if (_loading) { return; }
				value_polymorphicKey = getPage().getMpeEditor().getPolymorphicKeysDomain().getKey(widget_polymorphicKey.getSelectionIndex());
				setAttribute(getSourceNode(),"polymorphicKey",value_polymorphicKey);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"polymorphicKey");
			}
		});

// End polymorphicKey layout and validation
		
		toolkit.paintBordersFor(client);
		
		getPage().getMpeEditor().getConstraintManager().addConstraintListener(this);
	}

	protected void commit() {
		super.commit();
	}

	public String getName() {
		return value_name;
	}

	public String getImpliedSingleton() {
		return value_impliedSingleton;
	}

	public String getPolymorphicSingleton() {
		return value_polymorphicSingleton;
	}

	public String getPolymorphicKey() {
		return value_polymorphicKey;
	}

	public void loadFrom(Node source) {
		_loading = true;
		setSourceNode(source);
		value_name = ModelAccess.getAttribute(getSourceNode(),"@name");
		value_impliedSingleton = ModelAccess.getAttribute(getSourceNode(),"@impliedSingleton");
		value_polymorphicSingleton = ModelAccess.getAttribute(getSourceNode(),"@polymorphicSingleton");
		value_polymorphicKey = ModelAccess.getAttribute(getSourceNode(),"@polymorphicKey");

		updateScreen();
		enableSection(true);
		displayFirst(getPage().getMpeEditor().getConstraintManager().getCurrentProblems());
		_loading = false;
	}

	public void clear() {
		clearSourceNode();
		value_name = "";
		value_impliedSingleton = "";
		value_polymorphicSingleton = "";
		value_polymorphicKey = "";
		
		updateScreen();
		enableSection(false);
		mcomp.resetError();
	}

	private void enableSection(boolean enable) {
		widget_name.setEnabled(enable);
		widget_impliedSingleton.setEnabled(enable);
		widget_polymorphicSingleton.setEnabled(enable);
		widget_polymorphicKey.setEnabled(enable);
	}

	private void updateScreen() {
	
		widget_name.setText(value_name);

		widget_impliedSingleton.setSelection("true".equalsIgnoreCase(value_impliedSingleton));
		widget_polymorphicSingleton.setSelection("true".equalsIgnoreCase(value_polymorphicSingleton));
		
		PolymorphicKeysEnumeration domainPolymorphicKeys_for_widget_polymorphicKey = getPage().getMpeEditor().getPolymorphicKeysDomain();
		domainPolymorphicKeys_for_widget_polymorphicKey.setTarget(getSourceNode());
		widget_polymorphicKey.setItems(domainPolymorphicKeys_for_widget_polymorphicKey.getTexts());
		widget_polymorphicKey.setText(domainPolymorphicKeys_for_widget_polymorphicKey.textFor(value_polymorphicKey));

	}
	
	public void markDirty() {
		super.markDirty();
	}		

	protected boolean isPrimary() {
		return false;
	}

	public static ConstraintSet getConstraintSet() {
		ConstraintSet cs = new ConstraintSet(XaaEditor.Section_GroupDetails);
		cs.addConstraint(new Constraint("name",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("polymorphicKey",Constraint.CONSTRAINT_REQUIRED_IF_POLYMORPHIC_SINGLETON));
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
			if (candidate.getReportingConstraintSet() == XaaEditor.Section_GroupDetails ) {
				if (getSourceNode() == candidate.getTarget()) {
					mcomp.setError(candidate.getMessage());
					return;
				}
			}
		}
		mcomp.resetError();
	}
	
}
