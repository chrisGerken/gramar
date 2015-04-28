package org.gramar.eclipse.ui.dialog;

import java.util.ArrayList;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.gramar.IGramar;
import org.gramar.gramar.GramarScore;

public class ChooseGramarDialog extends Dialog {

	public static final String TEXT_TITLE = "Gramar Selection";
	public static final String TEXT_GRAMAR_LABEL = "Gramar";
	public static final String TEXT_APPLY_LABEL = "Apply";
	public static final String TEXT_CLOSE_LABEL = "Cancel";
	
	public static final int BUTTON_ID_CANCEL = IDialogConstants.CANCEL_ID;
	public static final int BUTTON_ID_APPLY = 102;
	
	private Combo 	gramarCombo;
	private List 	gramarList;
	private Label 	statusLabel;
	private Button	applyButton;
	private Button	closeButton;
	
	private GramarScore scored[];
	private IGramar selected;
	
	public ChooseGramarDialog(Shell parentShell, GramarScore[] scored) {
		super(parentShell);
		this.scored = scored;
	}

	@Override
	public void create() {

		super.create();

		Shell shell= getShell();

		shell.setText(TEXT_TITLE);

	}

	@Override
	protected Control createContents(Composite parent) {

		Composite panel= new Composite(parent, SWT.NULL);
		GridLayout layout= new GridLayout();
		layout.numColumns= 1;
		layout.makeColumnsEqualWidth= true;
		panel.setLayout(layout);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

//		Composite inputPanel= createGramarSelectionPanel(panel);
//		setGridData(inputPanel, SWT.FILL, true, SWT.TOP, false);

		Composite listPanel= createGramarListPanel(panel);
		setGridData(listPanel, SWT.FILL, true, SWT.TOP, false);

		Composite statusBar= createStatusAndButtons(panel);
		setGridData(statusBar, SWT.FILL, true, SWT.BOTTOM, false);

		updateButtonState();

		applyDialogFont(panel);

		return panel;
	}

	/**
	 * Creates the panel where the user chooses the Gramar
	 * to apply against the selected model.
	 *
	 * @param parent the parent composite
	 * @return the gramar selection panel
	 */
	private Composite createGramarSelectionPanel(Composite parent) {

		Composite panel= new Composite(parent, SWT.NULL);
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		panel.setLayout(layout);

		Label gramarLabel= new Label(panel, SWT.LEFT);
		gramarLabel.setText(TEXT_GRAMAR_LABEL);
		setGridData(gramarLabel, SWT.LEFT, false, SWT.CENTER, false);

		gramarCombo = new Combo(panel, SWT.DROP_DOWN | SWT.BORDER);
		setGridData(gramarCombo, SWT.FILL, true, SWT.CENTER, false);
		
		ArrayList<String> labels = new ArrayList<String>();
		for (GramarScore gs: scored) {
			labels.add(gs.toString());
		}
		String label[] = new String[labels.size()];
		labels.toArray(label);
		gramarCombo.setItems(label);
		
		SelectionListener listener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				updateButtonState();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				updateButtonState();
			}
		};
		
		gramarCombo.addSelectionListener(listener);

		return panel;
	}

	/**
	 * Creates the panel where the user chooses the Gramar
	 * to apply against the selected model.
	 *
	 * @param parent the parent composite
	 * @return the gramar selection panel
	 */
	private Composite createGramarListPanel(Composite parent) {

		Composite panel= new Composite(parent, SWT.NULL);
		GridLayout layout= new GridLayout();
		layout.numColumns= 2;
		panel.setLayout(layout);

		Label gramarLabel= new Label(panel, SWT.LEFT);
		gramarLabel.setText(TEXT_GRAMAR_LABEL);
		setGridData(gramarLabel, SWT.LEFT, false, SWT.TOP, false);

		gramarList = new List(panel, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL);
		GridData gd = setGridData(gramarList, SWT.FILL, true, SWT.CENTER, false);
		gd.heightHint = 200;
				
		ArrayList<String> labels = new ArrayList<String>();
		for (GramarScore gs: scored) {
			labels.add(gs.toString());
		}
		String label[] = new String[labels.size()];
		labels.toArray(label);
		gramarList.setItems(label);
		
		SelectionListener listener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				updateButtonState();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				updateButtonState();
			}
		};
		
		gramarList.addSelectionListener(listener);

		return panel;
	}

	/**
	 * Creates the status label and buttons section of the dialog.
	 *
	 * @param parent the parent composite
	 * @return the status/button panel
	 */
	private Composite createStatusAndButtons(Composite parent) {

		Composite panel= new Composite(parent, SWT.NULL);
		GridLayout layout= new GridLayout();
		layout.numColumns= 3;
		layout.marginWidth= 0;
		layout.marginHeight= 0;
		panel.setLayout(layout);

		statusLabel= new Label(panel, SWT.LEFT);
		setGridData(statusLabel, SWT.FILL, true, SWT.CENTER, false);

		applyButton= createButton(panel, BUTTON_ID_APPLY, TEXT_APPLY_LABEL, false);
		setGridData(applyButton, SWT.RIGHT, false, SWT.CENTER, false);

		closeButton= createButton(panel, BUTTON_ID_CANCEL, TEXT_CLOSE_LABEL, false);
		setGridData(closeButton, SWT.RIGHT, false, SWT.CENTER, false);

		return panel;
	}

	/**
	 * Attaches the given layout specification to the <code>component</code>.
	 *
	 * @param component the component
	 * @param horizontalAlignment horizontal alignment
	 * @param grabExcessHorizontalSpace grab excess horizontal space
	 * @param verticalAlignment vertical alignment
	 * @param grabExcessVerticalSpace grab excess vertical space
	 */
	private GridData setGridData(Control component, int horizontalAlignment, boolean grabExcessHorizontalSpace, int verticalAlignment, boolean grabExcessVerticalSpace) {
		GridData gd;
		if (component instanceof Button && (((Button)component).getStyle() & SWT.PUSH) != 0) {
			setButtonDimensionHint((Button)component);
			gd= (GridData)component.getLayoutData();
		} else {
			gd= new GridData();
			component.setLayoutData(gd);
			gd.horizontalAlignment= horizontalAlignment;
			gd.grabExcessHorizontalSpace= grabExcessHorizontalSpace;
		}
		gd.verticalAlignment= verticalAlignment;
		gd.grabExcessVerticalSpace= grabExcessVerticalSpace;
		return gd;
	}

	/**
	 * Sets width and height hint for the button control.
	 * <b>Note:</b> This is a NOP if the button's layout data is not
	 * an instance of <code>GridData</code>.
	 *
	 * @param button	the button for which to set the dimension hint
	 */
	public static void setButtonDimensionHint(Button button) {
		Assert.isNotNull(button);
		Object gd= button.getLayoutData();
		if (gd instanceof GridData) {
			((GridData)gd).widthHint= getButtonWidthHint(button);
			((GridData)gd).horizontalAlignment = GridData.FILL;
		}
	}
	
	/**
	 * Returns a width hint for the given button.
	 *
	 * @param button the button
	 * @return the width hint for the button
	 */
	public static int getButtonWidthHint(Button button) {
		button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter= new PixelConverter(button);
		int widthHint= converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}

	/**
	 * Updates the enabled state of the buttons.
	 */
	private void updateButtonState() {
		applyButton.setEnabled(gramarList.getSelectionIndex()>-1);
		closeButton.setEnabled(true);
	}

	/*
	 * @see Dialog#buttonPressed
	 */
	protected void buttonPressed(int buttonID) {
		if (buttonID == BUTTON_ID_CANCEL) {
			cancelPressed();
		}
		if (buttonID == BUTTON_ID_APPLY) {
			int index = gramarList.getSelectionIndex();
			selected = scored[index].getGramar();
			close();
		}
	}

	public IGramar getSelected() {
		return selected;
	}

}
