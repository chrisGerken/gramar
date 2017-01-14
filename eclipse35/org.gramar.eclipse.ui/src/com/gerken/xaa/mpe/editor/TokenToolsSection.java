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

public class TokenToolsSection extends AbstractToolSection implements IHyperlinkListener {

	public TokenToolsSection(TokenPage page, Composite parent) {
		super(page, parent);
	}

	public String getTextContent() {

		String content = "<form>" +
							"<li style=\"text\" bindent=\"5\"><a href=\"token.replacements\">Build replacements</a> for current token</li>" +
//							"<li style=\"text\" bindent=\"5\"><a href=\"sample.xform\">Generate</a> Placeholder</li>" +
							"</form>";

		return content;
	}

	public void linkActivated(HyperlinkEvent e) {
		String href = e.getHref().toString();


		if (href.equals("token.replacements")) {
			tokenReplacements();			
			return;
		}

	}

	private void tokenReplacements() {
		TokenReplacementsDialog trd = new TokenReplacementsDialog(new Shell());
		trd.setBlockOnOpen(true);
		trd.open();
		if (trd.getReturnCode() == Dialog.OK) {

			Document doc = getPage().getModel();
			Node xform = ModelAccess.getNodes(doc, "/xform")[0];
			
			String groupName = ModelAccess.getAttribute(getSourceNode().getParentNode(),"@name");
			String tokenName = ModelAccess.getAttribute(getSourceNode(),"@name");

			if (trd.requestUppercaseFirst()) {
				Element replacement = xform.getOwnerDocument().createElement("replacement");
				xform.appendChild(replacement);
				replacement.setAttribute("newString","{uppercaseFirst($"+groupName+"/@"+tokenName+")}");
				replacement.setAttribute("oldString",trd.replacementUppercaseFirst());
			}

			if (trd.requestLowercase()) {
				Element replacement = xform.getOwnerDocument().createElement("replacement");
				xform.appendChild(replacement);
				replacement.setAttribute("newString","{lower-case($"+groupName+"/@"+tokenName+")}");
				replacement.setAttribute("oldString",trd.replacementLowercase());
			}

			if (trd.requestDirectory()) {
				Element replacement = xform.getOwnerDocument().createElement("replacement");
				xform.appendChild(replacement);
				replacement.setAttribute("newString","{translate($"+groupName+"/@"+tokenName+",'.','/')}");
				replacement.setAttribute("oldString",trd.replacementDirectory());
			}

			if (trd.requestVanilla()) {
				Element replacement = xform.getOwnerDocument().createElement("replacement");
				xform.appendChild(replacement);
				replacement.setAttribute("newString","{$"+groupName+"/@"+tokenName+"}");
				replacement.setAttribute("oldString",trd.replacementVanilla());
			}
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
