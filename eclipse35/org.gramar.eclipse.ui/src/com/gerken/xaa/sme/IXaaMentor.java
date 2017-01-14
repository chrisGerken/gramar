package com.gerken.xaa.sme;

import com.gerken.xaa.model.xform.Xform;

public interface IXaaMentor {

	public boolean applicable(Xform xform);
	
	public void tweak(Xform xform);
	
	public String getName();
	
	public boolean isEnabled();
	
	public void setEnabled(boolean enabled);
	
	public boolean hasWizardPage();
	
	public IXaaMentorWizardPage getWizardPage();
	
}
