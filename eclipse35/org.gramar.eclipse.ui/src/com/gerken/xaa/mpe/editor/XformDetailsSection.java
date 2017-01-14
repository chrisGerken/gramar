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

public class XformDetailsSection extends AbstractDetailsSection implements IConstraintListener {

	private Text   widget_xformId;
	private String value_xformId = "";
	private Text   widget_version;
	private String value_version = "";
	private Text   widget_provider;
	private String value_provider = "";

	private boolean _loading = false;
	private SectionMessageAreaComposite mcomp;
	
	public XformDetailsSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void createClient(Section section, FormToolkit toolkit) {
		section.setText("Xform Details"); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription("Set the XForm properties below"); 
		
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

// Begin xformId layout and validation

		label = toolkit.createLabel(client, "Xform ID");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_xformId = toolkit.createText(client, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_xformId, "com.gerken.xaa.mpe.xform_xformId");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_xformId.setLayoutData(td);

		widget_xformId.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_xformId = widget_xformId.getText();
				setAttribute(getSourceNode(),"xformId",value_xformId);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"xformId");
			}
		});

// End xformId layout and validation
// Begin version layout and validation

		label = toolkit.createLabel(client, "Version");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_version = toolkit.createText(client, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_version, "com.gerken.xaa.mpe.xform_version");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_version.setLayoutData(td);

		widget_version.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_version = widget_version.getText();
				setAttribute(getSourceNode(),"version",value_version);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"version");
			}
		});

// End version layout and validation
// Begin provider layout and validation

		label = toolkit.createLabel(client, "Provider");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_provider = toolkit.createText(client, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_provider, "com.gerken.xaa.mpe.xform_provider");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_provider.setLayoutData(td);

		widget_provider.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_provider = widget_provider.getText();
				setAttribute(getSourceNode(),"provider",value_provider);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"provider");
			}
		});

// End provider layout and validation
		
		toolkit.paintBordersFor(client);
		
		getPage().getMpeEditor().getConstraintManager().addConstraintListener(this);
	}

	protected void commit() {
		super.commit();
	}

	public String getXformId() {
		return value_xformId;
	}

	public String getVersion() {
		return value_version;
	}

	public String getProvider() {
		return value_provider;
	}

	public void loadFrom(Node source) {
		_loading = true;
		setSourceNode(source);
		value_xformId = ModelAccess.getAttribute(getSourceNode(),"@xformId");
		value_version = ModelAccess.getAttribute(getSourceNode(),"@version");
		value_provider = ModelAccess.getAttribute(getSourceNode(),"@provider");

		updateScreen();
		enableSection(true);
		displayFirst(getPage().getMpeEditor().getConstraintManager().getCurrentProblems());
		_loading = false;
	}

	public void clear() {
		clearSourceNode();
		value_xformId = "";
		value_version = "";
		value_provider = "";
		
		updateScreen();
		enableSection(false);
		mcomp.resetError();
	}

	private void enableSection(boolean enable) {
		widget_xformId.setEnabled(enable);
		widget_version.setEnabled(enable);
		widget_provider.setEnabled(enable);
	}

	private void updateScreen() {
	
		widget_xformId.setText(value_xformId);

		widget_version.setText(value_version);

		widget_provider.setText(value_provider);

	}
	
	public void markDirty() {
		super.markDirty();
	}		

	protected boolean isPrimary() {
		return true;
	}

	public static ConstraintSet getConstraintSet() {
		ConstraintSet cs = new ConstraintSet(XaaEditor.Section_XformDetails);
		cs.addConstraint(new Constraint("xformId",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("version",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("provider",Constraint.CONSTRAINT_REQUIRED));
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
			if (candidate.getReportingConstraintSet() == XaaEditor.Section_XformDetails ) {
				if (getSourceNode() == candidate.getTarget()) {
					mcomp.setError(candidate.getMessage());
					return;
				}
			}
		}
		mcomp.resetError();
	}
	
}
