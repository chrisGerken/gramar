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

public class ProjectInterchangeExemplarWizardPage extends WizardPage {

	private Text exemplarIDText;
	private Text exemplarPIZipText;
	
	private Composite frame;
	
	private String exemplarID;
	private String selected[] = new String[1];
	
	private ISelection selection;

	public ProjectInterchangeExemplarWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Project Interchange Exemplar Specification");
		setDescription("This wizard creates a exemplar from a project interchange zip file on your file system.");
		this.selection = selection;
	}

	public void createControl(Composite parent) {
		frame = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		frame.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		
		Label label = new Label(frame, SWT.NULL);
		label.setText("Exemplar ID");

		exemplarIDText = new Text(frame, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		exemplarIDText.setLayoutData(gd);
		exemplarIDText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(frame, SWT.NULL);
		label.setText("Exemplar PI Zip");


		exemplarPIZipText = new Text(frame, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		exemplarPIZipText.setLayoutData(gd);
		exemplarPIZipText.addModifyListener(new ModifyListener() {
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
		String ext[] = new String[1];
		ext[0] = "zip";
		fd.setFilterExtensions(ext);
		String zip = fd.open();
		if (zip != null) {
			exemplarPIZipText.setText(zip);
			dialogChanged();
		}
	}

	private void initialize() {

	}

	private void dialogChanged() {
		String id = exemplarIDText.getText();
		String zip = exemplarPIZipText.getText();

		if (id.length() == 0) {
			updateStatus("Exemplar ID must be specified");
			return;
		}
		if (!validPackageName(id)) {
			updateStatus("Exemplar ID must be a valid java package name");
			return;
		}
		if (zip.length() == 0) {
			updateStatus("PI Zip file must be specified");
			return;
		}

		File root = new File(zip);
		if (root.isDirectory()) {
			updateStatus("PI Zip must be a file");
			return;
		}
		if (!root.exists()) {
			updateStatus("PI Zip file must exist");
			return;
		}
		
		exemplarID = id;
		selected[0] = zip;
		
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