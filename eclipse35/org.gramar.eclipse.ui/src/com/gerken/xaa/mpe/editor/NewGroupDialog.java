package com.gerken.xaa.mpe.editor;
	

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
	
	public class NewGroupDialog extends Dialog {
	
	private Text   widget_name;
	private String value_name = "";


		/**
		 * @param parentShell
		 */
		public NewGroupDialog(Shell parentShell) {
			super(parentShell);
		}

		protected Control createDialogArea(Composite parent) {
	
			Composite frame = new Composite(parent, SWT.NONE);
			GridLayout gl = new GridLayout();
			gl.numColumns = 2;
			GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
			gd.horizontalSpan = 2;
			frame.setLayout(gl);
			frame.setLayoutData(gd);

			Label __label = new Label(frame,SWT.NONE);
			__label.setText("Add a new group");
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			__label.setLayoutData(gd);

			Label label;
						
			label = new Label(frame,SWT.SHADOW_NONE);
			label.setText("Name");
			gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING); 
			label.setLayoutData(gd);

			widget_name = new Text(frame,SWT.SINGLE | SWT.SIMPLE | SWT.BORDER);
//			PlatformUI.getWorkbench().getHelpSystem().setHelp(widget_name, "com.gerken.xaa.mpe.tokenspec_name");
			gd = new GridData(GridData.FILL_HORIZONTAL); 
			gd.widthHint = 150;
			widget_name.setLayoutData(gd);
			widget_name.setText("");
			widget_name.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent arg0) {
					value_name = widget_name.getText();
					enableOK(validatePage());
				}
			});

			return frame;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 	*/
		protected void createButtonsForButtonBar(Composite parent) {
			super.createButtonsForButtonBar(parent);
			enableOK(validatePage());
		}
	
		private void enableOK(boolean pageIsValid) {
			Button ok = getButton(IDialogConstants.OK_ID);
			if (ok == null) { return; }
			ok.setEnabled(pageIsValid);
		}
	
		/**
		 * Validate the page.  Return whether or not the data entered onto the page
		 * is correct.  By default, all text fields are required and all combo boxes
		 * must have a selection.
		 */
		public boolean validatePage() {
			try {
				if (value_name.trim().length() == 0) { return false; }
			} catch (Exception e) {
				return false;
			}
			return true;
		}
		
		/**
		 * @see SelectionListener#widgetSelected(SelectionEvent)
		 */
		public void widgetSelected(SelectionEvent arg0) {
			enableOK(validatePage());
		}
	
		public String getName() {
			return value_name;
		}

	}
