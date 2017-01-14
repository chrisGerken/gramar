package com.gerken.xaa.wizard.model;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.gerken.xaa.sme.IXaaMentor;
import com.gerken.xaa.sme.IXaaMentorWizardPage;

public class MentorChoicesWizardPage extends WizardPage {
	
	private Button[]	enableMentorButton;
	
	private XformModelWizard xmWizard;

	public MentorChoicesWizardPage(XformModelWizard xformModelWizard) {
		super("MentorSelection");
		setTitle("Authoring Mentors Selection");
		setDescription("Confirm the recommended set of Mentors to apply");
		this.xmWizard = xformModelWizard;
	}

	public void createControl(Composite parent) {
		Composite frame = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		frame.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		IXaaMentor[] mentor = xmWizard.mentors();
		enableMentorButton = new Button[mentor.length];
		for (int m = 0; m < mentor.length; m++) {
			enableMentorButton[m] = new Button(frame,SWT.CHECK);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			enableMentorButton[m].setText(mentor[m].getName());
			enableMentorButton[m].setLayoutData(gd);
			enableMentorButton[m].setData(new Integer(m));
			enableMentorButton[m].setSelection(mentor[m].isEnabled());
			enableMentorButton[m].addSelectionListener(new SelectionListener() {
				private void exec(SelectionEvent e) {
					Button button = (Button) e.widget;
					int sel = ((Integer)(button.getData())).intValue();
					xmWizard.mentors()[sel].setEnabled(button.getSelection());
					getWizard().getContainer().updateButtons();
				}
				public void widgetDefaultSelected(SelectionEvent e) {
					exec(e);
				}
				public void widgetSelected(SelectionEvent e) {
					exec(e);
				}
			});
		}

		initialize();
		dialogChanged();
		setControl(frame);
	}

	private void initialize() {
		IXaaMentor[] mentor = xmWizard.mentors();
		for (int m = 0; m < mentor.length; m++) {
			enableMentorButton[m].setSelection(mentor[m].isEnabled());
		}
	}

	private void dialogChanged() {
		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public void updateSelections() {
		IXaaMentor[] mentor = xmWizard.mentors();
		for (int m = 0; m < mentor.length; m++) {
			enableMentorButton[m].setSelection(mentor[m].isEnabled());
		}
	}

}