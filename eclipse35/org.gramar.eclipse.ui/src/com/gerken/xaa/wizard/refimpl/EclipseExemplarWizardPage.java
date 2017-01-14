package com.gerken.xaa.wizard.refimpl;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

public class EclipseExemplarWizardPage extends WizardPage {

	private Text exemplarIDText;
	private List projectList;
	
	private String exemplarID;
	private String selected[];
	
	private String[] project;

	private ISelection selection;

	public EclipseExemplarWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Eclipse Exemplar Specification");
		setDescription("This wizard creates a exemplar project from one or more projects in your workspace.");
		this.selection = selection;
	}

	public void createControl(Composite parent) {
		Composite frame = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		frame.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		
		Label label = new Label(frame, SWT.NULL);
		label.setText("Exemplar ID");

		exemplarIDText = new Text(frame, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		exemplarIDText.setLayoutData(gd);
		exemplarIDText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(frame, SWT.NULL);
		label.setText("Exemplar Projects");

		projectList = new List(frame, SWT.MULTI);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projectList.setLayoutData(gd);
		project = gatherProjecs();
		projectList.setItems(project);
		projectList.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				dialogChanged();
			}
			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		initialize();
		dialogChanged();
		setControl(frame);
	}

	private String[] gatherProjecs() {
		IProject allProjects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		ArrayList<String> names = new ArrayList<String>();
		for (int i = 0; i < allProjects.length; i++) {
			if (allProjects[i].isOpen()) {
				names.add(allProjects[i].getName());
			}
		}
		String name[] = new String[names.size()];
		names.toArray(name);
		return name;
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {

	}

	private void dialogChanged() {
		String id = exemplarIDText.getText();

		if (id.length() == 0) {
			updateStatus("Exemplar ID must be specified");
			return;
		}
		if (!validPackageName(id)) {
			updateStatus("Exemplar ID must be a valid java package name");
			return;
		}
		if (projectList.getSelectionIndices().length == 0) {
			updateStatus("At least one project must be selected");
			return;
		}
		
		exemplarID = exemplarIDText.getText();
		selected = projectList.getSelection();
		
		updateStatus(null);
	}

	private boolean validPackageName(String buffer) {
		StringTokenizer st = new StringTokenizer(buffer,".",true);
		boolean dotNext = false;
		while (st.hasMoreTokens()) {
			String level = st.nextToken();
			if (dotNext) {
				if (!level.equals(".")) {
					return false;
				}
				dotNext = false;
			} else {
				if (!Character.isJavaIdentifierStart(level.charAt(0))) {
					return false;
				}
				for (int i = 1; i < level.length(); i++) {
					if (!Character.isJavaIdentifierPart(level.charAt(i))) {
						return false;
					}
				}
				dotNext = true;
			}
		}
		if (!dotNext) { return false; }
		return true;
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getExemplarID() {
		return exemplarID;
	}
	
	public String[] getSelected() {
		return selected;
	}

}