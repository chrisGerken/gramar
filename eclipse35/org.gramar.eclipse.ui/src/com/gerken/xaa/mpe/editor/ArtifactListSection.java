package com.gerken.xaa.mpe.editor;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.constraint.ConstraintFailure;
import com.gerken.xaa.mpe.constraint.IConstraintListener;
import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.AbstractListSection;
import com.gerken.xaa.mpe.core.ModelAccess;

public class ArtifactListSection extends AbstractListSection implements
		IConstraintListener {

	private Button ignoreButton;
	private Button moveButton;
	private Button copyButton;
	private Node lastGroup = null;

	public ArtifactListSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void createClient(Section section, FormToolkit toolkit) {

		section.setText(getListTitle());
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription(getListDescription());
		section.setExpanded(isSectionExpanded());

		Composite container = toolkit.createComposite(section);

		TableWrapLayout tl = new TableWrapLayout();
		tl.leftMargin = tl.rightMargin = toolkit.getBorderStyle() != SWT.NULL ? 0
				: 2;
		tl.numColumns = 2;
		container.setLayout(tl);

		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		container.setLayoutData(td);

		// table viewer

		nodesViewer = new TableViewer(container, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | toolkit.getBorderStyle());
		nodesViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent e) {
						nodeSelected((IStructuredSelection) e.getSelection());
					}
				});

		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = getTableHightHint();
		nodesViewer.getTable().setLayoutData(td);

		// buttons

		Composite buttonContainer = toolkit.createComposite(container);
		td = new TableWrapData(TableWrapData.FILL);
		buttonContainer.setLayoutData(td);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		buttonContainer.setLayout(layout);

		ignoreButton = toolkit
				.createButton(buttonContainer, "Ignore", SWT.PUSH);
		ignoreButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}

			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}

			private void exec() {
				ignoreNode();
			}

		});
		ignoreButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		moveButton = toolkit.createButton(buttonContainer, "Move...", SWT.PUSH);
		moveButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}

			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}

			private void exec() {
				moveNode();
			}

		});
		moveButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		copyButton = toolkit.createButton(buttonContainer, "Copy", SWT.PUSH);
		copyButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}

			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}

			private void exec() {
				copyNode();
			}

		});
		copyButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

/*		downButton = toolkit.createButton(buttonContainer, "Down", SWT.PUSH);
		downButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}
			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}
			private void exec() {
				moveDown();
			}

		});
		downButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		 */

		nodesViewer.setContentProvider(new NodesContentProvider());
		nodesViewer.setLabelProvider(new NodesLabelProvider());
		toolkit.paintBordersFor(container);
		section.setClient(container);

		nodesViewer.setInput(getModel());
		updateButtons();

		getPage().getMpeEditor().getConstraintManager()
				.addConstraintListener(this);

	}

	protected void ignoreNode() {

		Node ignored = null;
		Node[] node = ModelAccess.getNodes(getPage().getModel(),
				"/xform/ignored");
		if (node.length == 0) {
			node = ModelAccess.getNodes(getPage().getModel(), "/xform");
			ignored = ModelAccess.addNewChild(node[0], "ignored");
		} else {
			ignored = node[0];
		}

		int sels[] = nodesViewer.getTable().getSelectionIndices();
		// int nextSel = sels[sels.length-1] - sels.length + 1;
		Table table = nodesViewer.getTable();
		TableItem[] selection = table.getSelection();
		for (int i = 0; i < selection.length; i++) {
			Node art = (Node) selection[i].getData();
			art.getParentNode().removeChild(art);
			ignored.appendChild(art);
		}
		refreshList();
		// nodesViewer.getTable().setSelection(nextSel);

		int items = nodesViewer.getTable().getItemCount();
		if (items > 0) {
			if (items < (sels[0] + 2)) {
				nodesViewer.getTable().setSelection(sels[0]);
			} else {
				nodesViewer.getTable().setSelection(items - 1);
			}
		}
	}

	protected void moveNode() {
		DestinationGroupDialog dgd = new DestinationGroupDialog(new Shell(),
				getSourceNode());
		dgd.setBlockOnOpen(true);
		if (dgd.open() == dgd.OK) {
			lastGroup = dgd.getDestinationGroup();
			moveToLastGroup();
		}

	}

	protected void moveToLastGroup() {
		int sels[] = nodesViewer.getTable().getSelectionIndices();
		Table table = nodesViewer.getTable();
		TableItem[] selection = table.getSelection();
		for (int i = 0; i < selection.length; i++) {
			Node art = (Node) selection[i].getData();
			art.getParentNode().removeChild(art);
			lastGroup.appendChild(art);
		}

		refreshList();

		int items = nodesViewer.getTable().getItemCount();
		if (items > 0) {
			if (items < (sels[0] + 2)) {
				nodesViewer.getTable().setSelection(sels[0]);
			} else {
				nodesViewer.getTable().setSelection(items - 1);
			}
		}
	}

	protected void updateButtons() {
		Table table = nodesViewer.getTable();
		TableItem[] selection = table.getSelection();
		boolean hasSelection = selection.length > 0;
		boolean hasOneSelection = selection.length == 1;
		ignoreButton.setEnabled(hasSelection);
		moveButton.setEnabled(hasSelection);
		copyButton.setEnabled(hasOneSelection);
	}

	protected String getListTitle() {
		return "Artifacts";
	}

	public String getListDescription() {
		return "Manage the artifacts below";
	}

	public String getSourceExpression() {
		return ".";
	}

	protected String getItemName(Object element) {
		return bind((Node) element, "{@purpose}"); // was oPath
	}

	protected void refreshList() {
		Node l1[] = ModelAccess.getNodes(getSourceNode(), "createProject");
		Node l2[] = ModelAccess.getNodes(getSourceNode(), "createFolder");
		Node l3[] = ModelAccess.getNodes(getSourceNode(), "createFile");
		listObjects = new Node[l1.length + l2.length + l3.length];
		int offset = 0;
		for (int i = 0; i < l1.length; i++) {
			listObjects[offset] = l1[i];
			offset++;
		}
		for (int i = 0; i < l2.length; i++) {
			listObjects[offset] = l2[i];
			offset++;
		}
		for (int i = 0; i < l3.length; i++) {
			listObjects[offset] = l3[i];
			offset++;
		}
		nodesViewer.setInput(getRoot());
		clearDependents();
	}

	protected String getExtractorExpression() {
		return "x";
	}

	protected String getTargetName() {
		return "x";
	}

	protected boolean isSectionExpanded() {
		return true;
	}

	public int getTableHightHint() {
		return 350;
	}

	protected void copyNode() {

		TableItem[] selection = nodesViewer.getTable().getSelection();
		if (selection.length != 1) {
			return;
		}

		Node sourceNode = (Node) selection[0].getData();

		Element kid = getSourceNode().getOwnerDocument().createElement(
				sourceNode.getNodeName());
		getSourceNode().appendChild(kid);

		NamedNodeMap map = sourceNode.getAttributes();
		for (int i = 0; i < map.getLength(); i++) {
			Node attr = map.item(i);
			kid.setAttribute(attr.getNodeName(), attr.getNodeValue());
		}

		kid.setAttribute("id", getPage().getMpeEditor().getNextID());
		getPage().getMpeEditor().elementAdded(kid);

		// src="com.ravel.metrics/refimpl/art000205"

		refreshList();
		select(kid);
		updateButtons();

	}

	protected void notifyDependents(Node node) {
		((ArtifactPage) getPage()).getNamingDetailsSection().loadFrom(node);
		((ArtifactPage) getPage()).getFileDetailsSection().loadFrom(node);
		((ArtifactPage) getPage()).getOriginDetailsSection().loadFrom(node);
		((ArtifactPage) getPage()).getArtifactTextSection().loadFrom(node);
	}

	protected void clearDependents() {
		if (getPage().isDirty()) {
			((ArtifactPage) getPage()).getNamingDetailsSection().clear();
			((ArtifactPage) getPage()).getFileDetailsSection().clear();
			((ArtifactPage) getPage()).getOriginDetailsSection().clear();
			((ArtifactPage) getPage()).getArtifactTextSection().clear();
		}
	}

	protected boolean isPrimary() {
		return false;
	}

	public void setDependentSelection(Node node) {
		((ArtifactPage) getPage()).getNamingDetailsSection().setSelection(node);
		((ArtifactPage) getPage()).getFileDetailsSection().setSelection(node);
		((ArtifactPage) getPage()).getOriginDetailsSection().setSelection(node);
		((ArtifactPage) getPage()).getArtifactTextSection().setSelection(node);
	}

	public void constraintsChecked(ArrayList<ConstraintFailure> problems) {

	}

	public void enableSection(boolean enable) {
		nodesViewer.getTable().setEnabled(enable);
		ignoreButton.setEnabled(enable);
		moveButton.setEnabled(enable);
		copyButton.setEnabled(enable);
	}

	@Override
	protected void addNode() {
		// TODO Auto-generated method stub

	}

}
