package com.gerken.xaa.mpe.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.AbstractToolSection;
import com.gerken.xaa.mpe.core.ModelAccess;

public class GroupTocSection extends AbstractToolSection implements IHyperlinkListener {

	private Node[]	index;
	
	public GroupTocSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	public String getTextContent() {

		StringBuffer sb = new StringBuffer();
		sb.append("<form>");

			// Begin toc content generation

		index = getNodes("/xform/group");
		for (int i = 0; i < index.length; i++) {

				// Retrieve the node's display label
			String linkedText = bind(index[i],"{@name}");
			
				// write the indentation and bullet
			sb.append("<li style=\"text\" bindent=\"5\">");
			
				// write part of the line
			sb.append("");
			
				// write the link tag
			sb.append("<a href=\""+i+"\">");
			
				// write the linked text
			sb.append(linkedText);
			
				// write the close link tag
			sb.append("</a>");
			
				// end the line item
			sb.append("</li>");

		}
			
			// End toc generation

		sb.append("</form>");

		return sb.toString();
	}

	public void linkActivated(HyperlinkEvent e) {
		String href = e.getHref().toString();


		int i = Integer.parseInt(href);
		((AbstractFormPage)getPage().getMpeEditor().setActivePage(GroupPage.PAGE_ID)).setSelection(index[i]);

	}

	public void linkEntered(HyperlinkEvent e) {}

	public void linkExited(HyperlinkEvent e) {}

	protected boolean isPrimary() {
		return true;
	}

	public String getText() {
		return "Defined group elements";
	}
	
	public String getDescription() {
		return "View the group elements below";
	}

	public boolean isSectionExpanded() {
		return true;
	}

}
