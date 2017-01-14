package com.gerken.xaa.sme;

import java.util.StringTokenizer;

import org.eclipse.jface.wizard.WizardPage;

import com.gerken.xaa.model.xform.Xform;

public abstract class AbstractMentorWizardPage extends WizardPage implements
		IXaaMentorWizardPage {

	private IXaaMentor mentor;
	
	public AbstractMentorWizardPage(String pageName, IXaaMentor mentor) {
		super(pageName);
		this.mentor = mentor;
	}

	public abstract void prepareUsing(Xform xform);
	
	public abstract void dialogChanged();

	public IXaaMentor getMentor() {
		return mentor;
	}

	public void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

		// Reusable validations

	public boolean present(String buffer) {
		return buffer.trim().length() > 0;
	}

	public boolean validName(String buffer) {
		if (buffer.length() == 0) {return true; }
		if (!Character.isJavaIdentifierStart(buffer.charAt(0))) {
			return false;
		}
		for (int i = 1; i < buffer.length(); i++) {
			if (!Character.isJavaIdentifierPart(buffer.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean validNumber(String buffer) {
		if (buffer.length() == 0) {return true; }
		int start = 0;
		if (buffer.charAt(0)=='-') {
			start = 1;
			if (buffer.length() == 1) {return false; }
		}
		for (int i = start; i < buffer.length(); i++) {
			if (!Character.isDigit(buffer.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean validPackageName(String buffer) {
		StringTokenizer st = new StringTokenizer(buffer,".",true);
		boolean dotNext = false;
		while (st.hasMoreTokens()) {
			String level = st.nextToken();
			if (dotNext) {
				if (!level.equals(".")) {
					return false;
				}
				dotNext = false;
			} else {
				if (!Character.isJavaIdentifierStart(level.charAt(0))) {
					return false;
				}
				for (int i = 1; i < level.length(); i++) {
					if (!Character.isJavaIdentifierPart(level.charAt(i))) {
						return false;
					}
				}
				dotNext = true;
			}
		}
		return true;
	}


}
