package com.gerken.xaa.mpe.editor;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
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
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.constraint.ConstraintFailure;
import com.gerken.xaa.mpe.constraint.IConstraintListener;
import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.AbstractListSection;
import com.gerken.xaa.mpe.core.ModelAccess;

public class TokenListSection extends AbstractListSection implements
		IConstraintListener {

	private Button ignoreButton;
	private Button moveButton;
	private Button moveToSameButton;
	private Button addButton;
	private Node lastGroup = null;

	public TokenListSection(AbstractFormPage page, Composite parent) {
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

		moveToSameButton = toolkit.createButton(buttonContainer, "Move",
				SWT.PUSH);
		moveToSameButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}

			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}

			private void exec() {
				if (lastGroup != null) {
					moveToLastGroup();
				}
				moveNode();
			}

		});
		moveToSameButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		addButton = toolkit.createButton(buttonContainer, "New", SWT.PUSH);
		addButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}

			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}

			private void exec() {
				addNode();
			}

		});
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

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
			Node movee = (Node) selection[i].getData();
			movee.getParentNode().removeChild(movee);
			lastGroup.appendChild(movee);
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
		addButton.setEnabled(true);
		ignoreButton.setEnabled(hasSelection);
		moveButton.setEnabled(hasSelection);
		moveToSameButton.setEnabled(hasSelection);
	}

	public int getTableHightHint() {
		return 240;
	}

	protected String getListTitle() {
		return "Token List";
	}

	public String getListDescription() {
		return "Manage the token elements below";
	}

	public String getSourceExpression() {
		return ".";
	}

	protected String getItemName(Object element) {
		return bind((Node) element, "{@name}");
	}

	protected String getExtractorExpression() {
		return "token";
	}

	protected String getTargetName() {
		return "token";
	}

	protected boolean isSectionExpanded() {
		return true;
	}

	protected void addNode() {

		Element kid = getSourceNode().getOwnerDocument().createElement(
				getTargetName());
		getSourceNode().appendChild(kid);

		kid.setAttribute("name", "tokenName");
		kid.setAttribute("derived", "false");
		kid.setAttribute("formula", "");
		kid.setAttribute("desc", "");

		getPage().getMpeEditor().elementAdded(kid);

		// Begin custom initializations

		// End custom initializations
		refreshList();
		select(kid);
		updateButtons();
	}

	public void enableSection(boolean enable) {
		nodesViewer.getTable().setEnabled(enable);
		ignoreButton.setEnabled(enable);
		moveButton.setEnabled(enable);
		moveToSameButton.setEnabled(enable);
	}

	protected void notifyDependents(Node node) {
		((TokenPage) getPage()).getTokenDetailsSection().loadFrom(node);
		((TokenPage) getPage()).getTokenTextSection().loadFrom(node);
		((TokenPage) getPage()).getTokenToolsSection().loadFrom(node);
	}

	protected void clearDependents() {
		if (getPage().isDirty()) {
			((TokenPage) getPage()).getTokenDetailsSection().clear();
			((TokenPage) getPage()).getTokenToolsSection().clear();
			((TokenPage) getPage()).getTokenTextSection().clear();
		}
	}

	protected boolean isPrimary() {
		return false;
	}

	public void setDependentSelection(Node node) {
		((TokenPage) getPage()).getTokenDetailsSection().setSelection(node);
		((TokenPage) getPage()).getTokenTextSection().setSelection(node);
		((TokenPage) getPage()).getTokenToolsSection().setSelection(node);
	}

	public void constraintsChecked(ArrayList<ConstraintFailure> problems) {

	}

}
