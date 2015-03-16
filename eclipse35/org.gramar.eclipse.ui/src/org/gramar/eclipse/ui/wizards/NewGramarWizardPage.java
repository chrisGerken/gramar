package org.gramar.eclipse.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (fred).
 */

public class NewGramarWizardPage extends WizardPage {

	private Text idText;
	private Text labelText;
	private Text providerText;

//	private ISelection selection;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewGramarWizardPage(ISelection selection) {
		super("wizardPage");
		setTitle("Gramar Project");
		setDescription("This wizard creates and configures a new gramar project.");
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		Label label;
		GridData gd;
		
		label = new Label(container, SWT.NULL);
		label.setText("Gramar ID");

		idText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		idText.setLayoutData(gd);
		idText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("Description");

		labelText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		labelText.setLayoutData(gd);
		labelText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		label = new Label(container, SWT.NULL);
		label.setText("Provider");

		providerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		providerText.setLayoutData(gd);
		providerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		
		initialize();
		dialogChanged();
		setControl(container);
	}

	/**
	 * Tests if the current workbench selection is a suitable container to use.
	 */

	private void initialize() {
		idText.setText("some.dot.delimited.name");
		labelText.setText("What this gramar will generate");
		providerText.setText("a.maven.group.id");
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		String id = getGramarId();
		String label = getGramarLabel();
		String provider = getGramarProvider();

		
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(getGramarId());
		if (project.exists()) {
			updateStatus("Project already exists");
			return;
		}
		if ((label == null) || (label.trim().length() == 0)) {
			updateStatus("Enter a short description");
			return;
		}
		if ((provider == null) || (provider.trim().length() == 0)) {
			updateStatus("Enter a provider");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getGramarId() {
		return idText.getText();
	}

	public String getGramarLabel() {
		return labelText.getText();
	}

	public String getGramarProvider() {
		return providerText.getText();
	}

}