package com.gerken.xaa.mpe.core;

import org.eclipse.swt.SWTException;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Node;

public abstract class AbstractToolSection extends AbstractSection  implements IHyperlinkListener {

	private FormText 	text;
	private Color		_red;
	
	public AbstractToolSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}
	
	protected void createClient(Section section, FormToolkit toolkit) {

		section.setText(getText()); 
		section.setDescription(getDescription()); 
		section.setExpanded(isSectionExpanded());

		text = toolkit.createFormText(section, false);
		text.setColor(getRedKey(),toolkit.getColors().getColor(getRedKey()));
		try { text.setText(getTextContent(), true, false); } 
		catch (SWTException e) { text.setText("", false, false); } //$NON-NLS-1$
//		text.setImage("page", image);
		section.setClient(text);
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

		text.addHyperlinkListener(this);
		
		text.setColor(getRedKey(),toolkit.getColors().getColor(getRedKey()));
		text.setColor(getBlueKey(),toolkit.getColors().getColor(getBlueKey()));
		text.setColor(getGreenKey(),toolkit.getColors().getColor(getGreenKey()));
		text.setColor(getYellowKey(),toolkit.getColors().getColor(getYellowKey()));

	}


	public void clear() {
		setSourceNode(null);
		
		try { text.setText("<form>  </form>", true, false); } 
		catch (SWTException e) { text.setText("<form>  </form>", false, false); } //$NON-NLS-1$
		getSection().getParent().getParent().layout(true);
	}

	protected void commit() {
		super.commit();
	}

	public String getSourceExpression() {
		return ".";
	}

	public void loadFrom(Node source) {
		setSourceNode(source);
		
		try { text.setText(getTextContent(), true, false); } 
		catch (SWTException e) { text.setText("<form> "+e+" </form>", false, false); } //$NON-NLS-1$
		getSection().getParent().getParent().layout(true);
	}

	public FormText getTextWidget() {
		return text;
	}
	
	public String getRedKey() {
		return getPage().getMpeEditor().getRedKey();
	}
	
	public String getBlueKey() {
		return getPage().getMpeEditor().getBlueKey();
	}
	
	public String getGreenKey() {
		return getPage().getMpeEditor().getGreenKey();
	}
	
	public String getYellowKey() {
		return getPage().getMpeEditor().getYellowKey();
	}

	public abstract String getText();
	
	public abstract String getDescription();

	public abstract String getTextContent();
	
	public abstract boolean isSectionExpanded();
	
}
