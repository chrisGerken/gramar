package com.gerken.xaa.mpe.editor;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.core.AbstractToolSection;
import com.gerken.xaa.mpe.core.ModelAccess;

public class ReplacementToolsSection extends AbstractToolSection implements IHyperlinkListener {

	public ReplacementToolsSection(ReplacementPage page, Composite parent) {
		super(page, parent);
	}

	public String getTextContent() {

		String content = "<form>" +
							"<li style=\"text\" bindent=\"5\">Make a <a href=\"make.token\">new token</a>.</li>" +
//							"<li style=\"text\" bindent=\"5\"><a href=\"sample.xform\">Generate</a> Placeholder</li>" +
							"</form>";

		return content;
	}

	public void linkActivated(HyperlinkEvent e) {
		String href = e.getHref().toString();


		if (href.equals("make.token")) {
			makeToken();			
			return;
		}

	}

	private void makeToken() {

		Element replacement = (Element) getSourceNode();
		Node xform = replacement.getParentNode();

		TokenFromReplacementDialog tfrd = new TokenFromReplacementDialog(new Shell(),xform);
		tfrd.setBlockOnOpen(true);
		tfrd.open();
		if (tfrd.getReturnCode() == Dialog.OK) {
			
			String newString = ModelAccess.getAttribute(getSourceNode(),"@newString");
			String tokenName = tfrd.getTokenName();
			String groupId = tfrd.getGroupId();
			String groupName = tfrd.getGroupName();

			ModelAccess.createToken(xform.getOwnerDocument(),tokenName,groupId,true,newString);
			replacement.setAttribute("newString","{$"+groupName+"/@"+tokenName+"}");
			
			((ReplacementPage)getPage()).getReplacementDetailsSection().loadFrom(replacement);
		}
		
	}
	
	public void linkEntered(HyperlinkEvent e) {}

	public void linkExited(HyperlinkEvent e) {}

	public boolean isPrimary() {
		return true;
	}

	public String getText() {
		return "Custom Actions";
	}
	
	public String getDescription() {
		return "Click to perform an action";
	}

	public boolean isSectionExpanded() {
		return true;
	}

}
