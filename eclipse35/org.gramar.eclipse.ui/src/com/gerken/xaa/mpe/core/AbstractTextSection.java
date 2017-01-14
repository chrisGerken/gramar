package com.gerken.xaa.mpe.core;

import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.w3c.dom.Node;

public abstract class AbstractTextSection extends AbstractSection {

	private Text   textWidget;
	private String longTextValue = "";
	private boolean loading = false;
	private SectionMessageAreaComposite mcomp;

	public AbstractTextSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void createClient(Section section, FormToolkit toolkit) {
		section.setText(getSectionText()); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription(getSectionDescription()); 
		section.setExpanded(isSectionExpanded());
		
		Composite client = toolkit.createComposite(section);
		TableWrapLayout layout = new TableWrapLayout();
		layout.leftMargin = layout.rightMargin = toolkit.getBorderStyle() != SWT.NULL ? 0 : 2;
		layout.numColumns = 1;
		client.setLayout(layout);
		section.setClient(client);

		mcomp = new SectionMessageAreaComposite();
		mcomp.createControl(client, toolkit, 1);
		
		TableWrapData td;
		
//		IActionBars actionBars = getPage().getMpeEditor().getEditorSite().getActionBars();

// Begin long text layout

		textWidget = toolkit.createText(client, "", SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.valign = TableWrapData.MIDDLE;
		td.heightHint = getDesiredHeight();
		textWidget.setLayoutData(td);

		textWidget.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (getSourceNode() == null) { return; }
				longTextValue = textWidget.getText();
				writeLongTextToModel();
				markDirty();
			}
		});

// End long text layout
		
		toolkit.paintBordersFor(client);
		
//		IBaseModel model = getPage().getModel();
//		if (model instanceof IModelChangeProvider)
//			((IModelChangeProvider) model).addModelChangedListener(this);
	}

	protected void writeLongTextToModel() {
		if (loading) { return; }
		StringTokenizer st = new StringTokenizer(longTextValue,"\r\n");
		Vector<String> v = new Vector<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (token.trim().length() > 0) {
				v.addElement(token);
			}
		}

		Node[] desc = ModelAccess.getNodes(getSourceNode(), getExtractorEspression());
		for (int i = 0; i < desc.length; i++) {
			desc[i].getParentNode().removeChild(desc[i]);
		}
		
		for (Enumeration<String> mune = v.elements(); mune.hasMoreElements(); ) {
			String buffer = mune.nextElement();
			Node newDesc = getSourceNode().getOwnerDocument().createElement(getExtractorEspression());
			getSourceNode().appendChild(newDesc);
			org.w3c.dom.Text newText = getSourceNode().getOwnerDocument().createTextNode(buffer);
			newDesc.appendChild(newText);
		}
		
	}

	protected void commit() {
		super.commit();
	}

	public void loadFrom(Node source) {
		setSourceNode(source);
		Node[] desc = ModelAccess.getNodes(source, getExtractorEspression());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < desc.length; i++ ) {
			if (i > 0) { sb.append("\r\n\r\n"); }
			try {
				org.w3c.dom.Text child = (org.w3c.dom.Text) desc[i].getFirstChild();
				String buffer = child.getNodeValue();
				sb.append(buffer);
			} catch (RuntimeException e) {
				System.out.println(e);
			}
		}
		longTextValue = sb.toString();

		updateScreen();
		enableSection(true);
	}

	public void clear() {
		longTextValue = "";
		updateScreen();
		enableSection(false);
		clearSourceNode();
	}

	private void updateScreen() {
		loading = true;
		textWidget.setText(longTextValue);
		loading = false;
	}
	
	private void enableSection(boolean enable) {
		textWidget.setEnabled(enable);
	}

	public abstract String getSectionText();

	public abstract String getSectionDescription();
	
	public final String getSourceExpression() {
		return ".";
	}
	
	public abstract String getExtractorEspression();
	
	public abstract boolean isSectionExpanded();
	
	public int getDesiredHeight() {
		return 300;
	}
	
}
