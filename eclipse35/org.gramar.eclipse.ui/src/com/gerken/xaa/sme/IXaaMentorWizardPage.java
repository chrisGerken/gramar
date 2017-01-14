package com.gerken.xaa.sme;

import org.eclipse.jface.wizard.IWizardPage;

import com.gerken.xaa.model.xform.Xform;

public interface IXaaMentorWizardPage extends IWizardPage {
	
	public IXaaMentor getMentor();

	public void prepareUsing(Xform xform);

}
