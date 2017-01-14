package com.gerken.xaa.mpe.core;

import org.eclipse.swt.widgets.Composite;

public abstract class AbstractDetailsSection extends AbstractSection {

	public AbstractDetailsSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}
	
	public final String getSourceExpression() {
		return ".";
	}
}
