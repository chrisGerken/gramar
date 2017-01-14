package com.gerken.xaa.wizard.model;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.gerken.xaa.constants.Constants;
import com.gerken.xaa.nature.RefImplNature;

public class RefImplChoicelWizardPage extends WizardPage {
	
	private Combo		exemplarCombo;
	private Text		projectText;
	
	private IProject[]	exemplarProject;
	private IProject	selectedExemplar;
	private String		projectName = "";
	
	private XformModelWizard xmWizard;

	public RefImplChoicelWizardPage(XformModelWizard xformModelWizard) {
		super("ExemplarChoice");
		setTitle("Source Reference Implementation");
		setDescription("Select a reference implementation.");
		this.xmWizard = xformModelWizard;
	}

	public void createControl(Composite parent) {
		Composite frame = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		frame.setLayout(layout);
		
		Label label = new Label(frame, SWT.NULL);
		label.setText("Reference Exemplar Project");

		exemplarCombo = new Combo(frame, SWT.DROP_DOWN);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		exemplarCombo.setLayoutData(gd);
		exemplarCombo.setItems(getExemplarNames(getExemplarProjects()));
		exemplarCombo.addModifyListener(new ModifyListener() {
			private void exec(ModifyEvent e) {
				int sel = exemplarCombo.getSelectionIndex();
				selectedExemplar = exemplarProject[sel];
				if (projectName.length() == 0) {
					projectName = selectedExemplar.getName() + ".gramar";
					projectText.setText(projectName);
				}
				dialogChanged();
				xmWizard.useExemplar(selectedExemplar);
			}
			public void modifyText(ModifyEvent e) {
				exec(e);
			}
		});
		exemplarCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				exec(e);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				exec(e);
			}
			private void exec(SelectionEvent e) {
				int sel = exemplarCombo.getSelectionIndex();
				selectedExemplar = exemplarProject[sel];
				if (projectName.length() == 0) {
					projectName = selectedExemplar.getName() + ".gramar";
					projectText.setText(projectName);
				}
				dialogChanged();
				xmWizard.useExemplar(selectedExemplar);
			}
		});
		
		label = new Label(frame, SWT.NULL);
		label.setText("New Xform Project Name");

		projectText = new Text(frame, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		projectText.setLayoutData(gd);
		projectText.addModifyListener(new ModifyListener() {
			private void exec(ModifyEvent e) {
				projectName = projectText.getText().trim();
				dialogChanged();
			}
			public void modifyText(ModifyEvent e) {
				exec(e);
			}
		});

		initialize();
		dialogChanged();
		setControl(frame);
	}

	private IProject[] getExemplarProjects() {
		if (exemplarProject == null) {
			ArrayList<IProject> projects = new ArrayList<IProject>();
			IProject allProjects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (int i = 0; i < allProjects.length; i++) {
				try {
					if ((allProjects[i].isOpen()) && (allProjects[i].hasNature(Constants.RefImplNatureID))) {
						projects.add(allProjects[i]);
					}
				} catch (CoreException e) {}
			}
			exemplarProject = new IProject[projects.size()];
			projects.toArray(exemplarProject);
		}
		return exemplarProject;
	}
	private String[] getExemplarNames(IProject[] prj) {
		String[] result = new String[prj.length];
		for (int i = 0; i < prj.length; i++) {
			result[i] = prj[i].getName();
		}
		return result;
	}

	private void initialize() {

	}

	private void dialogChanged() {

		if (exemplarCombo.getText().trim().length() == 0) {
			updateStatus("Must choose a Reference Implementation");
			return;
		}

		if (projectName.length() == 0) {
			updateStatus("Must enter a name for the new project");
			return;
		}
		IProject prj = null;
		try {
			prj = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		} catch (Exception e) {
			updateStatus("Project name not valid");
			return;
		}
		if (prj.exists()) {
			updateStatus("A project with that name already exists");
			return;
		}
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public IProject getSelectedExemplar() {
		return selectedExemplar;
	}

	public void setSelectedExemplar(IProject selectedExemplar) {
		this.selectedExemplar = selectedExemplar;
	}

	public String getProjectName() {
		return projectName;
	}

}