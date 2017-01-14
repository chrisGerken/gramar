package com.gerken.xaa.wizard.refimpl;

import java.io.File;
import java.util.StringTokenizer;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MonolithExemplarWizardPage extends WizardPage {

	private Text monolithFileNameText;
	
	private String monolithFileName;
	
	private Composite frame;
	
	private ISelection selection;

	public MonolithExemplarWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Exemplar Monolith Specification");
		setDescription("This wizard creates a exemplar from an exemplar monolith on your file system.");
		this.selection = selection;
	}

	public void createControl(Composite parent) {
		frame = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		frame.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;

		Label label = new Label(frame, SWT.NULL);
		label.setText("Exemplar Monolih File Name");


		monolithFileNameText = new Text(frame, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		monolithFileNameText.setLayoutData(gd);
		monolithFileNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		Button browseButton = new Button(frame,SWT.PUSH); 
		gd = new GridData();
		browseButton.setLayoutData(gd);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				browse();
			}
			public void widgetSelected(SelectionEvent e) {
				browse();
			}
		});

		initialize();
		dialogChanged();
		setControl(frame);
	}

	public void browse() {
		FileDialog fd = new FileDialog(frame.getShell());
		String fn = fd.open();
		if (fn != null) {
			monolithFileNameText.setText(fn);
			dialogChanged();
		}
	}

	private void initialize() {

	}

	private void dialogChanged() {

		monolithFileName = monolithFileNameText.getText();

		if (monolithFileName.length() == 0) {
			updateStatus("Exemplar Monolith file name must be specified");
			return;
		}

		File ml = new File(monolithFileName);
		if (ml.isDirectory()) {
			updateStatus("Exemplar Monolith must be a file");
			return;
		}
		if (!ml.exists()) {
			updateStatus("Exemplar Monolith must exist");
			return;
		}
		
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

	public String getMonolithFileName() {
		return monolithFileName;
	}

	public void setMonolithFileName(String monolithFileName) {
		this.monolithFileName = monolithFileName;
	}

}