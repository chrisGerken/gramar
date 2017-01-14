package com.gerken.xaa.mpe.editor;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.constraint.Constraint;
import com.gerken.xaa.mpe.constraint.ConstraintFailure;
import com.gerken.xaa.mpe.constraint.ConstraintSet;
import com.gerken.xaa.mpe.constraint.IConstraintListener;
import com.gerken.xaa.mpe.core.AbstractDetailsSection;
import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.ModelAccess;
import com.gerken.xaa.mpe.core.SectionMessageAreaComposite;

public class FileDetailsSection extends AbstractDetailsSection implements IConstraintListener {

	private Button   widget_replace;
	private String   value_replace = "";
	private Button   widget_binary;
	private String   value_binary = "";
	private Button   widget_changeableName;
	private String   value_changeableName = "";
	private Button   widget_purposeAsTokenName;
	private String   value_purposeAsTokenName = "";

	private boolean _loading = false;
	private SectionMessageAreaComposite mcomp;
	
	public FileDetailsSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void createClient(Section section, FormToolkit toolkit) {
		section.setText("File Details"); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription("Provide file generation options below"); 
		
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

// Begin replace layout and validation

		label = toolkit.createLabel(client, "Replace");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_replace = toolkit.createButton(client, "Replace file on re-generation", SWT.CHECK);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_replace, "com.gerken.xaa.mpe.fileoptions_replace");
		widget_replace.setText("Replace file on re-generation");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_replace.setLayoutData(td);

		widget_replace.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
			public void widgetSelected(SelectionEvent arg0) { exec(); }
			private void exec() {
				if (_loading) { return; }
				value_replace = String.valueOf(widget_replace.getSelection());
				setAttribute(getSourceNode(),"replace",value_replace);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"replace");
			}
		});

// End replace layout and validation
// Begin binary layout and validation

		label = toolkit.createLabel(client, "Binary");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_binary = toolkit.createButton(client, "Binary file contents", SWT.CHECK);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_binary, "com.gerken.xaa.mpe.fileoptions_binary");
		widget_binary.setText("Binary file contents");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_binary.setLayoutData(td);

		widget_binary.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
			public void widgetSelected(SelectionEvent arg0) { exec(); }
			private void exec() {
				if (_loading) { return; }
				value_binary = String.valueOf(widget_binary.getSelection());
				setAttribute(getSourceNode(),"binary",value_binary);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"binary");
			}
		});

// End binary layout and validation
// Begin changeableName layout and validation

		label = toolkit.createLabel(client, "ChangeableName");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_changeableName = toolkit.createButton(client, "File name is not constant", SWT.CHECK);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_changeableName, "com.gerken.xaa.mpe.fileoptions_changeableName");
		widget_changeableName.setText("File name is not constant");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_changeableName.setLayoutData(td);

		widget_changeableName.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
			public void widgetSelected(SelectionEvent arg0) { exec(); }
			private void exec() {
				if (_loading) { return; }
				value_changeableName = String.valueOf(widget_changeableName.getSelection());
				setAttribute(getSourceNode(),"changeableName",value_changeableName);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"changeableName");
			}
		});

// End changeableName layout and validation
// Begin purposeAsTokenName layout and validation

		label = toolkit.createLabel(client, "PurposeAsTokenName");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_purposeAsTokenName = toolkit.createButton(client, "Create xform token from purpose", SWT.CHECK);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_purposeAsTokenName, "com.gerken.xaa.mpe.fileoptions_purposeAsTokenName");
		widget_purposeAsTokenName.setText("Create xform token from purpose");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_purposeAsTokenName.setLayoutData(td);

		widget_purposeAsTokenName.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
			public void widgetSelected(SelectionEvent arg0) { exec(); }
			private void exec() {
				if (_loading) { return; }
				value_purposeAsTokenName = String.valueOf(widget_purposeAsTokenName.getSelection());
				setAttribute(getSourceNode(),"purposeAsTokenName",value_purposeAsTokenName);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"purposeAsTokenName");
			}
		});

// End purposeAsTokenName layout and validation
		
		toolkit.paintBordersFor(client);
		
		getPage().getMpeEditor().getConstraintManager().addConstraintListener(this);
	}

	protected void commit() {
		super.commit();
	}

	public String getReplace() {
		return value_replace;
	}

	public String getBinary() {
		return value_binary;
	}

	public String getChangeableName() {
		return value_changeableName;
	}

	public String getPurposeAsTokenName() {
		return value_purposeAsTokenName;
	}

	public void loadFrom(Node source) {
		_loading = true;
		setSourceNode(source);
		value_replace = ModelAccess.getAttribute(getSourceNode(),"@replace");
		value_binary = ModelAccess.getAttribute(getSourceNode(),"@binary");
		value_changeableName = ModelAccess.getAttribute(getSourceNode(),"@changeableName");
		value_purposeAsTokenName = ModelAccess.getAttribute(getSourceNode(),"@purposeAsTokenName");

		updateScreen();
		enableSection(true);
		displayFirst(getPage().getMpeEditor().getConstraintManager().getCurrentProblems());
		_loading = false;
	}

	public void clear() {
		clearSourceNode();
		value_replace = "";
		value_binary = "";
		value_changeableName = "";
		value_purposeAsTokenName = "";
		
		updateScreen();
		enableSection(false);
		mcomp.resetError();
	}

	private void enableSection(boolean enable) {
		widget_replace.setEnabled(enable);
		widget_binary.setEnabled(enable);
		widget_changeableName.setEnabled(enable);
		widget_purposeAsTokenName.setEnabled(enable);
	}

	private void updateScreen() {
	
		widget_replace.setSelection("true".equalsIgnoreCase(value_replace));
		widget_binary.setSelection("true".equalsIgnoreCase(value_binary));
		widget_changeableName.setSelection("true".equalsIgnoreCase(value_changeableName));
		widget_purposeAsTokenName.setSelection("true".equalsIgnoreCase(value_purposeAsTokenName));
	}
	
	public void markDirty() {
		super.markDirty();
		((ArtifactPage)getPage()).getArtifactListSection().refreshSelected();
	}		

	protected boolean isPrimary() {
		return false;
	}

	public static ConstraintSet getConstraintSet() {
		ConstraintSet cs = new ConstraintSet(XaaEditor.Section_FileDetails);
		cs.addConstraint(new Constraint("replace",Constraint.CONSTRAINT_REQUIRED_IF_FILE));
		cs.addConstraint(new Constraint("binary",Constraint.CONSTRAINT_REQUIRED_IF_FILE));
		cs.addConstraint(new Constraint("changeableName",Constraint.CONSTRAINT_REQUIRED_IF_FILE));
		cs.addConstraint(new Constraint("purposeAsTokenName",Constraint.CONSTRAINT_REQUIRED_IF_FILE));
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
			if (candidate.getReportingConstraintSet() == XaaEditor.Section_FileDetails ) {
				if (getSourceNode() == candidate.getTarget()) {
					mcomp.setError(candidate.getMessage());
					return;
				}
			}
		}
		mcomp.resetError();
	}
	
}
