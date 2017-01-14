package com.gerken.xaa.mpe.core;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public abstract class AbstractSection extends SectionPart {

	private AbstractFormPage 	page;
	private Node			sourceNode;
	
	public AbstractSection(AbstractFormPage page, Composite parent) {
		super(parent, page.getEditor().getToolkit(), Section.DESCRIPTION | 
														ExpandableComposite.TITLE_BAR | 
														ExpandableComposite.TWISTIE  | 
														ExpandableComposite.EXPANDED );
		this.page = page;
		initialize(page.getManagedForm());
		getSection().clientVerticalSpacing = 4;
		getSection().setData("part", this); //$NON-NLS-1$
		createClient(getSection(), page.getEditor().getToolkit());
	}

	protected AbstractFormPage getPage() {
		return page;
	}
	
	protected abstract void createClient(Section section, FormToolkit toolkit);

	protected void commit() {
		refresh();
	}
	
	public void refreshSelected() {}

	public Document getModel() {
		return getPage().getModel();
	}

	public Element getRoot() {
		return getPage().getRoot();
	}
	
	public abstract void loadFrom(Node source);
	
	public abstract void clear();
	
	public abstract String getSourceExpression();

	
	public Node retrieveNode(String expr) {
		Node result[] = getNodes(expr);
		if (result.length == 0) {
			return null;
		}
		return result[0];
	}
	
	public Node[] getNodes(String expr) {
		return ModelAccess.getNodes(getRoot(), expr);
	}
	
	public String bind(Node target,String pattern) {
		String buffer = pattern;
		int i = buffer.indexOf("{");
		while (i > -1) {
			int j = buffer.indexOf("}",i);
			
			if (j > -1) {
				String before = buffer.substring(0,i);
				String expr   = buffer.substring(i+1,j);
				String after  = buffer.substring(j+1);
				buffer = before + ModelAccess.getAttribute(target,expr) + after;
				i = buffer.indexOf("{");
			} else {
				i = -1;
			}
		}
		return buffer;
	}

	protected void setAttribute(Node node, String name, String value) {
		ModelAccess.setAttribute(node, name, value);
	}

	protected void setText(Node node, String name, String value) {
		ModelAccess.setText(node, name, value);
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public void clearSourceNode() {
		setSourceNode(null);
	}

	public void refresh() {
		super.refresh();
		if (isPrimary()) {
			loadFrom(retrieveNode(getSourceExpression()));  
		} else {
			if (getSourceNode() != null) {
				loadFrom(getSourceNode());
				enableWidgets();
			} else {
				setSourceNode(null);
				disableWidgets();
			}
		}
	}

	protected abstract boolean isPrimary();

	protected void disableWidgets() {
		setWidgetsEnabled(false);
	}

	protected void enableWidgets() {
		setWidgetsEnabled(true);
	}

	protected void setWidgetsEnabled(boolean b) {}

	public void setSelection(Node node) {}
	
	public boolean isStale() { return true; }
	
	public boolean navigates() { return false; }

}
