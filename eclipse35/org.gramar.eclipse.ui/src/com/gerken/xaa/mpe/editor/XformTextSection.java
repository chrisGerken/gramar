package com.gerken.xaa.mpe.editor;

import com.gerken.xaa.mpe.core.AbstractTextSection;
import com.gerken.xaa.mpe.core.AbstractFormPage;

import org.eclipse.swt.widgets.Composite;

public class XformTextSection extends AbstractTextSection {

	public XformTextSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	public String getSectionText() {
		return "Description";
	}

	public String getSectionDescription() {
		return "Describe the selected root below";
	}
	
	public String getExtractorEspression() {
		return "desc";
	}

	public boolean isPrimary() {
		return false;
	}

	public boolean isSectionExpanded() {
		return true;
	}
	
	public int getDesiredHeight() {
		return 120;
	}
}
