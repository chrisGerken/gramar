package com.gerken.xaa.mpe.editor;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.constraint.ConstraintFailure;
import com.gerken.xaa.mpe.constraint.IConstraintListener;
import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.AbstractToolSection;

public class ProblemSection extends AbstractToolSection implements IConstraintListener, IHyperlinkListener {

	private ArrayList<ConstraintFailure> problems;

	public ProblemSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
		page.getMpeEditor().getConstraintManager().addConstraintListener(this);
	}

	public String getTextContent() {

		StringBuffer sb = new StringBuffer();
	
		problems = getPage().getMpeEditor().getConstraintManager().getCurrentProblems();
		sb.append("<form>");

		for (int i = 0; i < problems.size(); i++) {

				// write the indentation and bullet
			sb.append("<li style=\"text\" bindent=\"5\">");
			
				// write the link tag
			sb.append("<a href=\""+i+"\">");
			
				// write the linked text
			sb.append(problems.get(i).getPageName());
			
				// write the close link tag
			sb.append("</a>");

			sb.append(" page ");

			sb.append(": <span color=\""+getColorKey(problems.get(i))+"\">"+problems.get(i).getMessage()+"</span>");
			
				// end the line item
			sb.append("</li>");

		}
		sb.append("</form>");

		return sb.toString();
	}
	
	public String getColorKey(ConstraintFailure problem) {
		if (problem.getSeverity() == ConstraintFailure.SEVERITY_ERROR) {
			return getRedKey();
		} else if (problem.getSeverity() == ConstraintFailure.SEVERITY_WARNING) {
			return getYellowKey();
		} 
		return getBlueKey();
	}

	public void linkActivated(HyperlinkEvent e) {
		String href = e.getHref().toString();


		int i = Integer.parseInt(href);
		((AbstractFormPage)getPage().getMpeEditor().setActivePage(problems.get(i).getPageID())).setSelection(problems.get(i).getTarget());

	}

	public void linkEntered(HyperlinkEvent e) {}

	public void linkExited(HyperlinkEvent e) {}

	protected boolean isPrimary() {
		return true;
	}

	public String getText() {
		return "Model Errors";
	}
	
	public String getDescription() {
		return "The following errors were detected";
	}

	public boolean isSectionExpanded() {
		return true;
	}

	public void constraintsChecked(ArrayList<ConstraintFailure> problems) {
		if (getTextWidget() == null) { return; }
		try { getTextWidget().setText(getTextContent(), true, false); } 
		catch (Exception e) { getTextWidget().setText("", false, false); } //$NON-NLS-1$
		getSection().getParent().getParent().layout(true);
	}

	@Override
	public void loadFrom(Node source) {
		getPage().getMpeEditor().modelChanged();
		

		super.loadFrom(source);
	}

}
