package org.gramar.eclipse.ui.wizards;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.gramar.IGramar;
import org.gramar.ISampleModel;
import org.gramar.eclipse.platform.EclipsePlatform;
import org.gramar.exception.NoSuchResourceException;

/**
 * The "New" wizard page allows setting the container for the new file as well
 * as the file name. The page will only accept file name without the extension
 * OR with the extension that matches the expected one (mpe).
 */

public class NewModelWizardPage extends WizardPage {
	private Text containerText;

	private Text fileText;
	
	private List gramarList;
	
	private List modelList;

	private ISelection selection;
	
	private IGramar gramar[];
	
	private IGramar 		selectedGramar;
	private ISampleModel	sampleModel[];
	private ISampleModel  	selectedSampleModel;

	/**
	 * Constructor for SampleNewWizardPage.
	 * 
	 * @param pageName
	 */
	public NewModelWizardPage(ISelection selection) {
		super("modelPage");
		setTitle("Gramar Model File");
		setDescription("This wizard creates a new Gramar model file with *.xml extension.");
		this.selection = selection;
		EclipsePlatform platform = new EclipsePlatform();
		gramar = platform.getKnownGramars();
	}

	/**
	 * @see IDialogPage#createControl(Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 3;
		layout.verticalSpacing = 9;
		Label label = new Label(container, SWT.NULL);
		label.setText("&Container:");

		containerText = new Text(container, SWT.BORDER | SWT.SINGLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		containerText.setLayoutData(gd);
		containerText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		Button button = new Button(container, SWT.PUSH);
		button.setText("Browse...");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleBrowse();
			}
		});
		label = new Label(container, SWT.NULL);
		label.setText("&File name:");

		fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fileText.setLayoutData(gd);
		fileText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		label = new Label(container, SWT.NULL);

		
		label = new Label(container, SWT.NULL);
		label.setText("Gramar:");

		gramarList = new List(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 160;
		gramarList.setLayoutData(gd);
		gramarList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				exec(arg0);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec(arg0);
			}
			private void exec(SelectionEvent arg0) {
				selectedGramar = gramar[gramarList.getSelectionIndex()];
				sampleModel = new ISampleModel[selectedGramar.getSamples().size()];
				selectedGramar.getSamples().toArray(sampleModel);
				String sampleDesc[] = new String[sampleModel.length];
				int i = 0;
				for (ISampleModel sm: sampleModel) {
					sampleDesc[i] = sm.getDescription();
					i++;
				}
				modelList.setItems(sampleDesc);
				selectedSampleModel = null;
				dialogChanged();
			}
		});
		String gramarLabel[] = new String[gramar.length];
		for (int i = 0; i < gramar.length; i++) {
			gramarLabel[i] = gramar[i].getLabel();
		}
		gramarList.setItems(gramarLabel);
		label = new Label(container, SWT.NULL);

		
		label = new Label(container, SWT.NULL);
		label.setText("Sample Model:");

		modelList = new List(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 160;
		modelList.setLayoutData(gd);
		modelList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				exec(arg0);
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec(arg0);
			}
			private void exec(SelectionEvent arg0) {
				selectedSampleModel = sampleModel[modelList.getSelectionIndex()];
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
		if (selection != null && selection.isEmpty() == false
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) selection;
			if (ssel.size() > 1)
				return;
			Object obj = ssel.getFirstElement();
			if (obj instanceof IResource) {
				IContainer container;
				if (obj instanceof IContainer)
					container = (IContainer) obj;
				else
					container = ((IResource) obj).getParent();
				containerText.setText(container.getFullPath().toString());
			}
		}
		fileText.setText("new_model.xml");
	}

	/**
	 * Uses the standard container selection dialog to choose the new value for
	 * the container field.
	 */

	private void handleBrowse() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(
				getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
				"Select new file container");
		if (dialog.open() == ContainerSelectionDialog.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 1) {
				containerText.setText(((Path) result[0]).toString());
			}
		}
	}

	/**
	 * Ensures that both text fields are set.
	 */

	private void dialogChanged() {
		IResource container = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(getContainerName()));
		String fileName = getFileName();

		if (getContainerName().length() == 0) {
			updateStatus("File container must be specified");
			return;
		}
		if (container == null
				|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
			updateStatus("File container must exist");
			return;
		}
		if (!container.isAccessible()) {
			updateStatus("Project must be writable");
			return;
		}
		if (fileName.length() == 0) {
			updateStatus("File name must be specified");
			return;
		}
		if (fileName.replace('\\', '/').indexOf('/', 1) > 0) {
			updateStatus("File name must be valid");
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			String ext = fileName.substring(dotLoc + 1);
			if (ext.equalsIgnoreCase("xml") == false) {
				updateStatus("File extension must be \"xml\"");
				return;
			}
		}
		if (selectedGramar==null) {
			updateStatus("A gramar must be selected");
			return;
		}
		if (selectedSampleModel==null) {
			updateStatus("A sample model must be selected");
			return;
		}
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getContainerName() {
		return containerText.getText();
	}

	public String getFileName() {
		return fileText.getText();
	}

	public String getSampleModelText() {
		try {
			return selectedGramar.getTemplateSource(selectedSampleModel.getPath());
		} catch (NoSuchResourceException e) {
			return "Could not find sample model";
		}
	}
}