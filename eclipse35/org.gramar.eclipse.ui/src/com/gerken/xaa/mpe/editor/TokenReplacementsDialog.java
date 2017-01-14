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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Node;
import org.eclipse.ui.PlatformUI;

	public class TokenReplacementsDialog extends Dialog {

	private int VARIENT_UPPERCASEFIRST = 0;
	private int VARIENT_LOWERCASE = 1;
	private int VARIENT_DIRECTORY = 2;
	private int VARIENT_VANILLA = 3;
	
	private Text   	valueText;
	private String 	value = "";
	private Button	optionButton[];
	private String  optionResult[];
	private String  optionLabel[];
	private boolean optionChosen[];
	private int  	options;

		public TokenReplacementsDialog(Shell parentShell) {
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
			__label.setText("Add token replacements");
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			__label.setLayoutData(gd);

			Label label;
						
			label = new Label(frame,SWT.SHADOW_NONE);
			label.setText("Expected Value");
			gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING); 
			label.setLayoutData(gd);

			valueText = new Text(frame,SWT.SINGLE | SWT.SIMPLE | SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL); 
			gd.widthHint = 150;
			valueText.setLayoutData(gd);
			valueText.setText("");
			valueText.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent arg0) {
					value = valueText.getText();
					refreshOptions();
					enableOK(validatePage());
				}

			});

			options = 4;
			optionButton = new Button[options];
			optionResult = new String[options];
			optionChosen = new boolean[options];
			optionLabel = new String[options];
			
			optionLabel[VARIENT_UPPERCASEFIRST] = "uppercaseFirst";
			optionLabel[VARIENT_LOWERCASE] = "lowercase";
			optionLabel[VARIENT_DIRECTORY] = "directory";
			optionLabel[VARIENT_VANILLA] = "(as is)";
			
			for (int i = 0; i < options; i++) {
							
				optionChosen[i] = false;

				label = new Label(frame,SWT.SHADOW_NONE);
				label.setText(optionLabel[i]);
				gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING); 
				label.setLayoutData(gd);

				optionButton[i] = new Button(frame,SWT.CHECK);
				gd = new GridData(GridData.FILL_HORIZONTAL); 
				optionButton[i].setLayoutData(gd);
				optionButton[i].setText("");

				optionButton[i].addSelectionListener(new SelectionListener() {
					public void widgetSelected(SelectionEvent arg0) {
						exec();
					}
					public void widgetDefaultSelected(SelectionEvent arg0) {
						exec();
					}
					private void exec() {
						for (int i = 0; i < options; i++) {
							optionChosen[i] = optionButton[i].getSelection();
						}
						enableOK(validatePage());
					}
				});

			}
			return frame;
		}

		private void refreshOptions() {
			String buffer;
			
			if (value.length() == 0) {
				buffer = "";
			} else if (value.length() == 1) {
				buffer = value.toUpperCase();
			} else {
				buffer = value.substring(0,1).toUpperCase() + value.substring(1);
			}
			optionResult[VARIENT_UPPERCASEFIRST] = buffer;
			optionButton[VARIENT_UPPERCASEFIRST].setText(buffer);
			
			buffer = value.toLowerCase();
			optionResult[VARIENT_LOWERCASE] = buffer;
			optionButton[VARIENT_LOWERCASE].setText(buffer);
			
			buffer = value.replace('.', '/');
			optionResult[VARIENT_DIRECTORY] = buffer;
			optionButton[VARIENT_DIRECTORY].setText(buffer);
			
			buffer = value;
			optionResult[VARIENT_VANILLA] = buffer;
			optionButton[VARIENT_VANILLA].setText(buffer);
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
				if (value.trim().length() == 0) { return false; }
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
			return value;
		}

		public boolean requestUppercaseFirst() {
			return optionChosen[VARIENT_UPPERCASEFIRST];
		}

		public boolean requestDirectory() {
			return optionChosen[VARIENT_DIRECTORY];
		}

		public boolean requestLowercase() {
			return optionChosen[VARIENT_LOWERCASE];
		}

		public boolean requestVanilla() {
			return optionChosen[VARIENT_VANILLA];
		}

		public String replacementUppercaseFirst() {
			return optionResult[VARIENT_UPPERCASEFIRST];
		}

		public String replacementDirectory() {
			return optionResult[VARIENT_DIRECTORY];
		}

		public String replacementLowercase() {
			return optionResult[VARIENT_LOWERCASE];
		}

		public String replacementVanilla() {
			return optionResult[VARIENT_VANILLA];
		}

	}
