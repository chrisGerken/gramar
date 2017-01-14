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

public class PackageDetailsSection extends AbstractDetailsSection implements IConstraintListener {

	private Text   widget_name;
	private String value_name = "";
	private Text   widget_purpose;
	private String value_purpose = "";

	Node samepkg[];

	private boolean _loading = false;
	private SectionMessageAreaComposite mcomp;
	
	public PackageDetailsSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void createClient(Section section, FormToolkit toolkit) {
		section.setText("Package Details"); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription("Provide package information below"); 
		
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
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_name, "dm.com.gerken.xaa.mpe.package_name");
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
// Begin purpose layout and validation

		label = toolkit.createLabel(client, "Purpose");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_purpose = toolkit.createText(client, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_purpose, "com.gerken.xaa.mpe.package_purpose");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_purpose.setLayoutData(td);

		widget_purpose.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_purpose = widget_purpose.getText();
				samePackages();
				for (int s = 0; s < samepkg.length; s++) {
					setAttribute(samepkg[s],"purpose",value_purpose);
				}
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"purpose");
			}
		});

// End purpose layout and validation
		
		toolkit.paintBordersFor(client);
		
		getPage().getMpeEditor().getConstraintManager().addConstraintListener(this);
	}

	protected void commit() {
		super.commit();
	}

	public String getName() {
		return value_name;
	}

	public String getPurpose() {
		return value_purpose;
	}

	public void loadFrom(Node source) {
		_loading = true;
		setSourceNode(source);
		value_name = ModelAccess.getAttribute(getSourceNode(),"@name");
		value_purpose = ModelAccess.getAttribute(getSourceNode(),"@purpose");

		updateScreen();
		enableSection(true);
		displayFirst(getPage().getMpeEditor().getConstraintManager().getCurrentProblems());
		_loading = false;
	}
	
	public void samePackages() {

		String xpath = "/xform/group[@name!=\"ignored\"]/createFile/javaPkg[@name=\""+value_name+"\"]";
		samepkg = ModelAccess.getNodes(getModel(), xpath);
		
	}

	public void clear() {
		clearSourceNode();
		value_name = "";
		value_purpose = "";
		
		updateScreen();
		enableSection(false);
		mcomp.resetError();
	}

	private void enableSection(boolean enable) {
		widget_name.setEnabled(false);
		widget_purpose.setEnabled(enable);
	}

	private void updateScreen() {
	
		widget_name.setText(value_name);

		widget_purpose.setText(value_purpose);

	}
	
	public void markDirty() {
		super.markDirty();
		((PackagePage)getPage()).getPackageListSection().refreshSelected();
	}		

	protected boolean isPrimary() {
		return false;
	}

	public static ConstraintSet getConstraintSet() {
		ConstraintSet cs = new ConstraintSet(XaaEditor.Section_PackageDetails);
		cs.addConstraint(new Constraint("name",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("purpose",Constraint.CONSTRAINT_REQUIRED));
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
			if (candidate.getReportingConstraintSet() == XaaEditor.Section_PackageDetails ) {
				if (getSourceNode() == candidate.getTarget()) {
					mcomp.setError(candidate.getMessage());
					return;
				}
			}
		}
		mcomp.resetError();
	}
	
}
