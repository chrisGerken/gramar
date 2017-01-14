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

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo; 
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

public class NamingDetailsSection extends AbstractDetailsSection implements IConstraintListener {

	private Text   widget_purpose;
	private String value_purpose = "";
//	private Combo   widget_project;
//	private String value_project = "";
	private Text   widget_projectExpr;
	private String value_projectExpr = "";
	private Text   widget_folderExpr;
	private String value_folderExpr = "";
	private Text   widget_nameExpr;
	private String value_nameExpr = "";
	private Text   widget_locationExpr;
	private String value_locationExpr = "";
	
	private Button projectMarkupButton;
	private Button folderMarkupButton;
	private Button fileMarkupButton;
	private Button locationMarkupButton;

	private boolean _loading = false;
	private SectionMessageAreaComposite mcomp;
	
	private Button button;
	
	public NamingDetailsSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void createClient(Section section, FormToolkit toolkit) {
		section.setText("Artifact Naming"); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription("Provide naming convention information below"); 
		
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

// Begin purpose layout and validation

		label = toolkit.createLabel(client, "Purpose");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_purpose = toolkit.createText(client, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_purpose, "com.gerken.xaa.mpe.naming_purpose");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_purpose.setLayoutData(td);

		widget_purpose.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_purpose = widget_purpose.getText();
				setAttribute(getSourceNode(),"purpose",value_purpose);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"purpose");
			}
		});

// End purpose layout and validation
// Begin project layout and validation
/*
		label = toolkit.createLabel(client, "Project");
		label.setForeground(toolkit.getColors().getColor(FormColors.TITLE));
		td = new TableWrapData();
		td.valign = TableWrapData.MIDDLE;
		label.setLayoutData(td);

		widget_project = new Combo(client,SWT.NONE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_project, "com.gerken.xaa.mpe.naming_project");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.colspan = 2;
		td.valign = TableWrapData.MIDDLE;
		widget_project.setLayoutData(td);

		widget_project.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) { exec(); }
			public void widgetSelected(SelectionEvent arg0) { exec(); }
			private void exec() {
				if (_loading) { return; }
				value_project = getPage().getMpeEditor().getProjectsDomain().getKey(widget_project.getSelectionIndex());
				setAttribute(getSourceNode(),"project",value_project);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"project");
			}
		});
*/
// End project layout and validation
// Begin projectExpr layout and validation

		label = toolkit.createLabel(client, "Project Name Expression");
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

		widget_projectExpr = toolkit.createText(doubleWide, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_projectExpr, "com.gerken.xaa.mpe.naming_projectExpr");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		widget_projectExpr.setLayoutData(td);

		widget_projectExpr.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_projectExpr = widget_projectExpr.getText();
				setAttribute(getSourceNode(),"projectExpr",value_projectExpr);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"projectExpr");
			}
		});
		
		projectMarkupButton = toolkit.createButton(doubleWide, "Markup", SWT.PUSH);
		td = new TableWrapData(TableWrapData.RIGHT);
		td.maxWidth = 100;
		
		projectMarkupButton.setLayoutData(td);
		
		projectMarkupButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}
			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}
			private void exec() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
//				String original = folderFrom(ModelAccess.getAttribute(getSourceNode(),"@oPath"));
				String original = ModelAccess.getAttribute(getSourceNode(),"@oPath");
				String current = ModelAccess.getAttribute(getSourceNode(),"@projExpr");
				String proposals[] = ModelAccess.markupProposals(getModel(), original, current);
				MarkupProposalListDialog dialog = new MarkupProposalListDialog(shell,current,proposals);
				dialog.setBlockOnOpen(true);
				dialog.open();
				if (dialog.getReturnCode() == Window.OK) {
					String newExpr = dialog.getProposal();
					widget_projectExpr.setText(newExpr);
					setText(getSourceNode(),"projectExpr",newExpr);
					markDirty();
					getPage().getMpeEditor().propertyChanged(getSourceNode(),"projectExpr");
				}
			}
			
		});

// End projectExpr layout and validation
// Begin folderExpr layout and validation

		label = toolkit.createLabel(client, "Folder Expression");
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

		widget_folderExpr = toolkit.createText(doubleWide, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_folderExpr, "com.gerken.xaa.mpe.naming_folderExpr");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
//		td.maxWidth = 800;
//		td.grabHorizontal = true;
//		td.colspan = 2;
		widget_folderExpr.setLayoutData(td);

		widget_folderExpr.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_folderExpr = widget_folderExpr.getText();
				setAttribute(getSourceNode(),"folderExpr",value_folderExpr);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"folderExpr");
			}
		});
		
		folderMarkupButton = toolkit.createButton(doubleWide, "Markup", SWT.PUSH);
		td = new TableWrapData(TableWrapData.RIGHT);
		td.maxWidth = 100;
		
		folderMarkupButton.setLayoutData(td);
		
		folderMarkupButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}
			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}
			private void exec() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				String original = folderFrom(ModelAccess.getAttribute(getSourceNode(),"@oPath"));
				String current = ModelAccess.getAttribute(getSourceNode(),"@folderExpr");
				String proposals[] = ModelAccess.markupProposals(getModel(), original, current);
				MarkupProposalListDialog dialog = new MarkupProposalListDialog(shell,current,proposals);
				dialog.setBlockOnOpen(true);
				dialog.open();
				if (dialog.getReturnCode() == Window.OK) {
					String newExpr = dialog.getProposal();
					widget_folderExpr.setText(newExpr);
					setText(getSourceNode(),"folderExpr",newExpr);
					markDirty();
					getPage().getMpeEditor().propertyChanged(getSourceNode(),"folderExpr");
				}
			}
			
		});

// End folderExpr layout and validation
// Begin nameExpr layout and validation

		label = toolkit.createLabel(client, "Name Expression");
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

		widget_nameExpr = toolkit.createText(doubleWide, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_nameExpr, "com.gerken.xaa.mpe.naming_nameExpr");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		widget_nameExpr.setLayoutData(td);

		widget_nameExpr.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_nameExpr = widget_nameExpr.getText();
				setAttribute(getSourceNode(),"nameExpr",value_nameExpr);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"nameExpr");
			}
		});
		
		fileMarkupButton = toolkit.createButton(doubleWide, "Markup", SWT.PUSH);
		td = new TableWrapData(TableWrapData.RIGHT);
		td.maxWidth = 100;
		
		fileMarkupButton.setLayoutData(td);
		
		fileMarkupButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}
			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}
			private void exec() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				String original = fileFrom(ModelAccess.getAttribute(getSourceNode(),"@oPath"));
				String current = ModelAccess.getAttribute(getSourceNode(),"@nameExpr");
				String proposals[] = ModelAccess.markupProposals(getModel(), original, current);
				MarkupProposalListDialog dialog = new MarkupProposalListDialog(shell,current,proposals);
				dialog.setBlockOnOpen(true);
				dialog.open();
				if (dialog.getReturnCode() == Window.OK) {
					String newExpr = dialog.getProposal();
					widget_nameExpr.setText(newExpr);
					setText(getSourceNode(),"nameExpr",newExpr);
					markDirty();
					getPage().getMpeEditor().propertyChanged(getSourceNode(),"nameExpr");
				}
			}
			
		});

// End nameExpr layout and validation
// Begin locationExpr layout and validation

		label = toolkit.createLabel(client, "Location Expression");
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

		widget_locationExpr = toolkit.createText(doubleWide, "", SWT.SINGLE);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_locationExpr, "com.gerken.xaa.mpe.naming_locationExpr");
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		widget_locationExpr.setLayoutData(td);

		widget_locationExpr.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (_loading) { return; }
				value_locationExpr = widget_locationExpr.getText();
				setAttribute(getSourceNode(),"locationExpr",value_locationExpr);
				markDirty();
				getPage().getMpeEditor().propertyChanged(getSourceNode(),"locationExpr");
			}
		});
		
		locationMarkupButton = toolkit.createButton(doubleWide, "Markup", SWT.PUSH);
		td = new TableWrapData(TableWrapData.RIGHT);
		td.maxWidth = 100;
		
		locationMarkupButton.setLayoutData(td);
		
		locationMarkupButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}
			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}
			private void exec() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				String original = fileFrom(ModelAccess.getAttribute(getSourceNode(),"@oLocation"));
				String current = ModelAccess.getAttribute(getSourceNode(),"@locationExpr");
				String proposals[] = ModelAccess.markupProposals(getModel(), original, current);
				MarkupProposalListDialog dialog = new MarkupProposalListDialog(shell,current,proposals);
				dialog.setBlockOnOpen(true);
				dialog.open();
				if (dialog.getReturnCode() == Window.OK) {
					String newExpr = dialog.getProposal();
					widget_locationExpr.setText(newExpr);
					setText(getSourceNode(),"locationExpr",newExpr);
					markDirty();
					getPage().getMpeEditor().propertyChanged(getSourceNode(),"locationExpr");
				}
			}
			
		});

// End locationExpr layout and validation
		
		toolkit.paintBordersFor(client);
		
		getPage().getMpeEditor().getConstraintManager().addConstraintListener(this);
	}

	protected String folderFrom(String text) {
		String buf = text.replace('\\','/');
		int index = buf.lastIndexOf('/');
		if (index == -1) {
			return "";
		}
		return text.substring(0,index);
	}

	protected String fileFrom(String text) {
		String buf = text.replace('\\','/');
		int index = buf.lastIndexOf('/');
		if (index == -1) {
			return text;
		}
		return text.substring(index+1);
	}

	protected void commit() {
		super.commit();
	}

	public String getPurpose() {
		return value_purpose;
	}
//
//	public String getProject() {
//		return value_project;
//	}

	public String getProjectExpr() {
		return value_projectExpr;
	}

	public String getFolderExpr() {
		return value_folderExpr;
	}

	public String getNameExpr() {
		return value_nameExpr;
	}

	public String getLocationExpr() {
		return value_locationExpr;
	}

	public void loadFrom(Node source) {
		_loading = true;
		setSourceNode(source);
		value_purpose = ModelAccess.getAttribute(getSourceNode(),"@purpose");
//		value_project = ModelAccess.getAttribute(getSourceNode(),"@project");
		value_projectExpr = ModelAccess.getAttribute(getSourceNode(),"@projectExpr");
		value_folderExpr = ModelAccess.getAttribute(getSourceNode(),"@folderExpr");
		value_nameExpr = ModelAccess.getAttribute(getSourceNode(),"@nameExpr");
		value_locationExpr = ModelAccess.getAttribute(getSourceNode(),"@locationExpr");

		updateScreen();
		enableSection(true);
		displayFirst(getPage().getMpeEditor().getConstraintManager().getCurrentProblems());
		_loading = false;
	}

	public void clear() {
		clearSourceNode();
		value_purpose = "";
//		value_project = "";
		value_projectExpr = "";
		value_folderExpr = "";
		value_nameExpr = "";
		value_locationExpr = "";
		
		updateScreen();
		enableSection(false);
		mcomp.resetError();
	}

	private void enableSection(boolean enable) {
		
		if (enable) {
			widget_purpose.setEnabled(enable);
			String name = getSourceNode().getNodeName();
			if ("createProject".equals(name)) {
				widget_projectExpr.setEnabled(enable);
				widget_folderExpr.setEnabled(false);
				widget_nameExpr.setEnabled(false);
				widget_locationExpr.setEnabled(enable);
				
				projectMarkupButton.setEnabled(enable);
				folderMarkupButton.setEnabled(false);
				fileMarkupButton.setEnabled(false);
				locationMarkupButton.setEnabled(enable);
			} else if ("createFolder".equals(name)) {
				widget_projectExpr.setEnabled(false);
				widget_folderExpr.setEnabled(enable);
				widget_nameExpr.setEnabled(false);
				widget_locationExpr.setEnabled(false);
				
				projectMarkupButton.setEnabled(false);
				folderMarkupButton.setEnabled(enable);
				fileMarkupButton.setEnabled(false);
				locationMarkupButton.setEnabled(false);
			} else if ("createFile".equals(name)) {
				widget_projectExpr.setEnabled(false);
				widget_folderExpr.setEnabled(enable);
				widget_nameExpr.setEnabled(enable);
				widget_locationExpr.setEnabled(false);
				
				projectMarkupButton.setEnabled(false);
				folderMarkupButton.setEnabled(enable);
				fileMarkupButton.setEnabled(enable);
				locationMarkupButton.setEnabled(false);
			} 
		} else {
			widget_purpose.setEnabled(enable);
//			widget_project.setEnabled(enable);
			widget_projectExpr.setEnabled(enable);
			widget_folderExpr.setEnabled(enable);
			widget_nameExpr.setEnabled(enable);
			widget_locationExpr.setEnabled(enable);
		}
	
	}

	private void updateScreen() {
	
		widget_purpose.setText(value_purpose);

		
		ProjectsEnumeration domainProjects_for_widget_project = getPage().getMpeEditor().getProjectsDomain();
		domainProjects_for_widget_project.setTarget(getSourceNode());
//		widget_project.setItems(domainProjects_for_widget_project.getTexts());
//		widget_project.setText(domainProjects_for_widget_project.textFor(value_project));

		widget_projectExpr.setText(value_projectExpr);

		widget_folderExpr.setText(value_folderExpr);

		widget_nameExpr.setText(value_nameExpr);

		widget_locationExpr.setText(value_locationExpr);

	}
	
	public void markDirty() {
		super.markDirty();
		((ArtifactPage)getPage()).getArtifactListSection().refreshSelected();
	}		

	protected boolean isPrimary() {
		return false;
	}

	public static ConstraintSet getConstraintSet() {
		ConstraintSet cs = new ConstraintSet(XaaEditor.Section_NamingDetails);
		cs.addConstraint(new Constraint("purpose",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("purpose",Constraint.CONSTRAINT_NAME));
//		cs.addConstraint(new Constraint("project",Constraint.CONSTRAINT_REQUIRED));
		cs.addConstraint(new Constraint("projectExpr",Constraint.CONSTRAINT_REQUIRED_IF_PROJECT));
		cs.addConstraint(new Constraint("projectExpr",Constraint.CONSTRAINT_CONTAINS_XPATH));
		cs.addConstraint(new Constraint("folderExpr",Constraint.CONSTRAINT_CONTAINS_XPATH));
		cs.addConstraint(new Constraint("folderExpr",Constraint.CONSTRAINT_LEADING_SLASH_REQUIRED));
		cs.addConstraint(new Constraint("nameExpr",Constraint.CONSTRAINT_REQUIRED_IF_FILE));
		cs.addConstraint(new Constraint("nameExpr",Constraint.CONSTRAINT_CONTAINS_XPATH));
		cs.addConstraint(new Constraint("locationExpr",Constraint.CONSTRAINT_CONTAINS_XPATH));
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
			if (candidate.getReportingConstraintSet() == XaaEditor.Section_NamingDetails ) {
				if (getSourceNode() == candidate.getTarget()) {
					mcomp.setError(candidate.getMessage());
					return;
				}
			}
		}
		mcomp.resetError();
	}
	
}
