package com.gerken.xaa.wizard.compare;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.gerken.xaa.constants.Constants;

public class CompareExemplarlWizardPage extends WizardPage {
	
	private Combo		exemplar1Combo;
	private Combo		exemplar2Combo;
	
	private IProject	selectedExemplar1;
	private IProject	selectedExemplar2;
	
	private IProject    exemplarProject[];

	public CompareExemplarlWizardPage(CompareExemplarWizard xformModelWizard) {
		super("ExemplarChoice");
		setTitle("Source Reference Implementation");
		setDescription("Select a reference implementation.");
	}

	public void createControl(Composite parent) {
		Composite frame = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		frame.setLayout(layout);
		
		Label label;
		
		label = new Label(frame, SWT.NULL);
		label.setText("Reference Exemplar Project");

		exemplar1Combo = new Combo(frame, SWT.DROP_DOWN);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		exemplar1Combo.setLayoutData(gd);
		exemplar1Combo.setItems(getExemplarNames(getExemplarProjects()));
		exemplar1Combo.addModifyListener(new ModifyListener() {
			private void exec(ModifyEvent e) {
				int sel = exemplar1Combo.getSelectionIndex();
				selectedExemplar1 = exemplarProject[sel];
				dialogChanged();
			}
			public void modifyText(ModifyEvent e) {
				exec(e);
			}
		});
		exemplar1Combo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				exec(e);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				exec(e);
			}
			private void exec(SelectionEvent e) {
				int sel = exemplar1Combo.getSelectionIndex();
				selectedExemplar1 = exemplarProject[sel];
				dialogChanged();
			}
		});

		exemplar2Combo = new Combo(frame, SWT.DROP_DOWN);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		exemplar2Combo.setLayoutData(gd);
		exemplar2Combo.setItems(getExemplarNames(getExemplarProjects()));
		exemplar2Combo.addModifyListener(new ModifyListener() {
			private void exec(ModifyEvent e) {
				int sel = exemplar2Combo.getSelectionIndex();
				selectedExemplar2 = exemplarProject[sel];
				dialogChanged();
			}
			public void modifyText(ModifyEvent e) {
				exec(e);
			}
		});
		exemplar2Combo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				exec(e);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				exec(e);
			}
			private void exec(SelectionEvent e) {
				int sel = exemplar2Combo.getSelectionIndex();
				selectedExemplar2 = exemplarProject[sel];
				dialogChanged();
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

		if (exemplar1Combo.getText().trim().length() == 0) {
			updateStatus("Must choose a first exemplar");
			return;
		}

		if (exemplar2Combo.getText().trim().length() == 0) {
			updateStatus("Must choose a second exemplar");
			return;
		}
		
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public IProject getSelectedExemplar1() {
		return selectedExemplar1;
	}

	public IProject getSelectedExemplar2() {
		return selectedExemplar2;
	}

}