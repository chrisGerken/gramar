package com.gerken.xaa.sme;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.gerken.xaa.model.xform.CreateProject;
import com.gerken.xaa.model.xform.Group;
import com.gerken.xaa.model.xform.Xform;

public class BasicMentorWizardPage extends AbstractMentorWizardPage implements
		IXaaMentorWizardPage {
	
	private String[]	projectName;
	private String[]	purpose;
	private String		groupName = "";
	
	private Text[]		purposeText;
	private Text		groupNameText;
	
	public BasicMentorWizardPage(String pageName, IXaaMentor mentor) {
		super(pageName,mentor);
	}

	public void createControl(Composite parent) {
		Composite frame = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		frame.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		
		Label label = new Label(frame, SWT.NULL);
		label.setText("Artifact Set Name");

		groupNameText = new Text(frame, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		groupNameText.setLayoutData(gd);
		groupNameText.addModifyListener(new ModifyListener() {
			private void exec(ModifyEvent e) {
				groupName = groupNameText.getText().trim();
				dialogChanged();
			}
			public void modifyText(ModifyEvent e) {
				exec(e);
			}
		});

		purposeText = new Text[purpose.length];

		for (int p = 0; p < purpose.length; p++) {
			
			label = new Label(frame, SWT.NULL);
			label.setText(projectName[p]);
			
			purposeText[p] = new Text(frame,SWT.NONE);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			purposeText[p].setLayoutData(gd);
			purposeText[p].setData(new Integer(p));
			purposeText[p].addModifyListener(new ModifyListener() {
				private void exec(ModifyEvent e) {
					Text text = (Text) e.widget;
					int sel = ((Integer)(text.getData())).intValue();
					purpose[sel] = text.getText();
					dialogChanged();
				}
				public void modifyText(ModifyEvent e) {
					exec(e);
				}
			});
			purpose[p] = "";
		}

		dialogChanged();
		setControl(frame);
	}

	public void dialogChanged() {

		if (groupName.length() == 0) {
			updateStatus("Must enter a name for the full collection of artifacts");
			return;
		}

		if (!validName(groupName)) {
			updateStatus("Artifact set name numst be a valid, single-token name");
			return;
		}

		for (int p = 0; p < purpose.length; p++) {
			if (purpose[p].trim().length() == 0) {
				updateStatus("A purpose is required for each reference implementation project");
				return;
			}
			if (!validName(purpose[p])) {
				updateStatus("Purpose must be a single token name");
				return;
			}
		}
		
		updateStatus(null);
	}

	public void prepareUsing(Xform xform) {
		ArrayList<String> names = new ArrayList<String>();
		Group group[] = xform.getGroupArray();
		for (int g = 0; g < group.length; g++) {
			CreateProject cp[] = group[g].getCreateProjectArray();
			for (int c = 0; c < cp.length; c++) {
				names.add(cp[c].getOPath());
			}
		}
		
		projectName = new String[names.size()];
		names.toArray(projectName);
		purpose = new String[projectName.length];
		for (int i = 0; i < purpose.length; i++) {
			purpose[i] = "";
		}
	}

	public String[] getProjectName() {
		return projectName;
	}

	public void setProjectName(String[] projectName) {
		this.projectName = projectName;
	}

	public String[] getPurpose() {
		return purpose;
	}

	public void setPurpose(String[] purpose) {
		this.purpose = purpose;
	}

	public String getGroupName() {
		return groupName;
	}

}
