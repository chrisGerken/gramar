package com.gerken.xaa.mpe.core;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
		
	
	public class SectionMessageAreaComposite {
	
			private Label		messageLabel;
			private Color		red;
					
		public SectionMessageAreaComposite() {
		}
	
		public void setError(String message) {
			messageLabel.setText(message);
		}
		
		public void resetError() {
			messageLabel.setText("");
		}

		public void createControl(Composite parent, FormToolkit toolkit, int colspan) {

			messageLabel = toolkit.createLabel(parent,"");
			TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
			td.valign = TableWrapData.MIDDLE;
			td.colspan = colspan;
			messageLabel.setLayoutData(td);

			red = toolkit.getColors().createColor("MPE_ERROR_FOREGROUND", 250, 20, 20 );
			messageLabel.setForeground(red);
		}
			
	}
	