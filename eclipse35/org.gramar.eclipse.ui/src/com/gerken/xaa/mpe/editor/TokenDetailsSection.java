package com.gerken.xaa.mpe.editor;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
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

public class TokenDetailsSection extends AbstractDetailsSection implements IConstraintListener {

	private Text   widget_name;
	private String value_name = "";
	private Button   widget_derived;
	private String   value_derived = "";
	private Button   widget_optional;
	private String   value_optional = "";
	private Text   widget_formula;
	private String value_formula = "";
	private Button formulaMarkupButton;

	private boolean _loading = false;
	private SectionMessageAreaComposite mcomp;
	
	public TokenDetailsSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void createClient(Section section, FormToolkit toolkit) {
		section.setText("Token Details"); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription("Set the token properties below"); 
		
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
		Composite doubleWide;
		
//		IActionBars actionBars = getPage().getMpeEditor().getEditorSite().getActionBars();

// Begin name layout and validation

		label = toolkit.createLabel(client, "Name");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_name = toolkit.createText(client, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_name, "com.gerken.xaa.mpe.token_name");
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
		// Begin derived layout and validation

				label = toolkit.createLabel(client, "Derived");
				label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
				td = new TableWrapData();
				td.valign = TableWrapData.MIDDLE;
				label.setLayoutData(td);

				widget_derived = toolkit.createButton(client, "Token is not provided by the xform invocation", SWT.CHECK);
				PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_derived, "com.gerken.xaa.mpe.token_derived");
				widget_derived.setText("Token is not provided by the xform invocation");
				td = new TableWrapData(TableWrapData.FILL_GRAB);
				td.colspan = 2;
				td.valign = TableWrapData.MIDDLE;
				widget_derived.setLayoutData(td);

				widget_derived.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
					public void widgetSelected(SelectionEvent arg0) { exec(); }
					private void exec() {
						if (_loading) { return; }
						value_derived = String.valueOf(widget_derived.getSelection());
						setAttribute(getSourceNode(),"derived",value_derived);
						markDirty();
						getPage().getMpeEditor().propertyChanged(getSourceNode(),"derived");
					}
				});

		// End derived layout and validation
				// Begin optional layout and validation

				label = toolkit.createLabel(client, "Optional");
				label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
				td = new TableWrapData();
				td.valign = TableWrapData.MIDDLE;
				label.setLayoutData(td);

				widget_optional = toolkit.createButton(client, "Calculate using formula if not specified", SWT.CHECK);
				PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_optional, "com.gerken.xaa.mpe.token_optional");
				widget_optional.setText("Calculate using formula if not specified");
				td = new TableWrapData(TableWrapData.FILL_GRAB);
				td.colspan = 2;
				td.valign = TableWrapData.MIDDLE;
				widget_optional.setLayoutData(td);

				widget_optional.addSelectionListener(new SelectionListener() {
					public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
					public void widgetSelected(SelectionEvent arg0) { exec(); }
					private void exec() {
						if (_loading) { return; }
						value_optional = String.valueOf(widget_optional.getSelection());
						setAttribute(getSourceNode(),"optional",value_optional);
						markDirty();
						getPage().getMpeEditor().propertyChanged(getSourceNode(),"optional");
					}
				});

		// End derived layout and validation
// Begin formula layout and validation

		label = toolkit.createLabel(client, "Formula");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);
		
		doubleWide = toolkit.createComposite(client);
		layout = new TableWrapLayout();
		layout.leftMargin = layout.rightMargin = toolkit.getBorderStyle() != SWT.NULL ? 0 : 2;
		layout.numColumns = 2;
		doubleWide.setLayout(layout);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		doubleWide.setLayoutData(td);

		widget_formula = toolkit.createText(doubleWide, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_formula, "com.gerken.xaa.mpe.token_formula");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		widget_formula.setLayoutData(td);

		widget_formula.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_formula = widget_formula.getText();
				setAttribute(getSourceNode(),"formula",value_formula);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"formula");
			}
		});
		
		formulaMarkupButton = toolkit.createButton(doubleWide, "Markup", SWT.PUSH);
		td = new TableWrapData(TableWrapData.RIGHT);
		td.maxWidth = 100;
		
		formulaMarkupButton.setLayoutData(td);
		
		formulaMarkupButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}
			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}
			private void exec() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				String current = ModelAccess.getAttribute(getSourceNode(),"@formula");
				String proposals[] = ModelAccess.markupProposals(getModel(), current, current);
				MarkupProposalListDialog dialog = new MarkupProposalListDialog(shell,current,proposals);
				dialog.setBlockOnOpen(true);
				dialog.open();
				if (dialog.getReturnCode() == Window.OK) {
					String newExpr = dialog.getProposal();
					widget_formula.setText(newExpr);
					setText(getSourceNode(),"formula",newExpr);
					markDirty();
					getPage().getMpeEditor().propertyChanged(getSourceNode(),"formula");
				}
			}
			
		});

// End formula layout and validation
		
		toolkit.paintBordersFor(client);
		
		getPage().getMpeEditor().getConstraintManager().addConstraintListener(this);
	}

	protected void commit() {
		super.commit();
	}

	public String getName() {
		return value_name;
	}

	public String getDerived() {
		return value_derived;
	}

	public String getFormula() {
		return value_formula;
	}

	public void loadFrom(Node source) {
		_loading = true;
		setSourceNode(source);
		value_name = ModelAccess.getAttribute(getSourceNode(),"@name");
		value_derived = ModelAccess.getAttribute(getSourceNode(),"@derived");
		value_optional = ModelAccess.getAttribute(getSourceNode(),"@optional");
		value_formula = ModelAccess.getAttribute(getSourceNode(),"@formula");

		updateScreen();
		enableSection(true);
		displayFirst(getPage().getMpeEditor().getConstraintManager().getCurrentProblems());
		_loading = false;
	}

	public void clear() {
		clearSourceNode();
		value_name = "";
		value_derived = "";
		value_optional = "";
		value_formula = "";
		
		updateScreen();
		enableSection(false);
		mcomp.resetError();
	}

	private void enableSection(boolean enable) {
		widget_name.setEnabled(enable);
		widget_derived.setEnabled(enable);
		widget_optional.setEnabled(enable);
		widget_formula.setEnabled(enable);
		formulaMarkupButton.setEnabled(enable);
	}

	private void updateScreen() {
	
		widget_name.setText(value_name);

		widget_derived.setSelection("true".equalsIgnoreCase(value_derived));
		widget_optional.setSelection("true".equalsIgnoreCase(value_optional));
		widget_formula.setText(value_formula);

	}
	
	public void markDirty() {
		super.markDirty();
		((TokenPage)getPage()).getTokenListSection().refreshSelected();
	}		

	protected boolean isPrimary() {
		return false;
	}

	public static ConstraintSet getConstraintSet() {
		ConstraintSet cs = new ConstraintSet(XaaEditor.Section_TokenDetails);
		cs.addConstraint(new Constraint("name",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("name",Constraint.CONSTRAINT_UNIQUE));
		cs.addConstraint(new Constraint("derived",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("formula",Constraint.CONSTRAINT_REQUIRED_IF_DERIVED));
		cs.addConstraint(new Constraint("formula",Constraint.CONSTRAINT_CONTAINS_XPATH));
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
			if (candidate.getReportingConstraintSet() == XaaEditor.Section_TokenDetails ) {
				if (getSourceNode() == candidate.getTarget()) {
					mcomp.setError(candidate.getMessage());
					return;
				}
			}
		}
		mcomp.resetError();
	}
	
}
