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
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
	
	public class MarkupProposalListDialog extends Dialog {
	
	private List  	list;
	private String  current;
	private String  proposals[];		

		public MarkupProposalListDialog(Shell parentShell, String current, String[] proposals) {
			super(parentShell);
			this.current = current;
			this.proposals = proposals;
		}

		protected Control createDialogArea(Composite parent) {
	
			Composite frame = new Composite(parent, SWT.NONE);
			GridLayout gl = new GridLayout();
			gl.numColumns = 2;
			GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
			gd.horizontalSpan = 2;
			gd.widthHint = 500;
			frame.setLayout(gl);
			frame.setLayoutData(gd);
			
			Label __label = new Label(frame,SWT.NONE);
			__label.setText("Choose a marked up expression");
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			__label.setLayoutData(gd);

			Label label;
						
			label = new Label(frame,SWT.SHADOW_NONE);
			label.setText("Expression");
			gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING); 
			label.setLayoutData(gd);

			list = new List(frame,SWT.SINGLE);
			gd = new GridData(GridData.FILL_HORIZONTAL); 
			gd.widthHint = 200;
			gd.heightHint = 150;
			list.setLayoutData(gd);
			list.setItems(proposals);
			list.setSelection(0);
			list.addSelectionListener(new SelectionListener() {
				private void exec() {
					current = list.getItem(list.getSelectionIndex());
					enableOK(validatePage());
				}
				public void widgetDefaultSelected(SelectionEvent arg0) {
					exec();
				}
				public void widgetSelected(SelectionEvent arg0) {
					exec();
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
				if (current.trim().length() == 0) { return false; }
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
	
		public String getProposal() {
			return current;
		}

	}
