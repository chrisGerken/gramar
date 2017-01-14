package com.gerken.xaa.mpe.core;

import java.util.ArrayList;

import com.gerken.xaa.mpe.constraint.ConstraintFailure;
import com.gerken.xaa.mpe.constraint.IConstraintListener;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.w3c.dom.Node;

public abstract class AbstractListSection extends AbstractSection implements IConstraintListener {
    
	protected TableViewer nodesViewer;
	private Button		addButton;
	private Button		removeButton;
	private Button		upButton;
	private Button		downButton;
	
	public Node[] listObjects = new Node[0];
	private SectionMessageAreaComposite mcomp;

	public AbstractListSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void commit() {
		super.commit();
	}

	protected void createClient(Section section, FormToolkit toolkit) {

		section.setText(getListTitle()); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription(getListDescription()); 
		section.setExpanded(isSectionExpanded());

		Composite container = toolkit.createComposite(section);

		TableWrapLayout tl = new TableWrapLayout();
		tl.leftMargin = tl.rightMargin = toolkit.getBorderStyle() != SWT.NULL ? 0 : 2;
		tl.numColumns = 2;
		container.setLayout(tl);
		
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		container.setLayoutData(td);

		// table viewer 

		nodesViewer = new TableViewer(container, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | toolkit.getBorderStyle());
		nodesViewer.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent e) {
				nodeSelected((IStructuredSelection)e.getSelection());
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

		addButton = toolkit.createButton(buttonContainer, "Add", SWT.PUSH);
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

		removeButton = toolkit.createButton(buttonContainer, "Remove", SWT.PUSH);
		removeButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}
			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}
			private void exec() {
				removeNode();
			}
			
		});
		removeButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		upButton = toolkit.createButton(buttonContainer, "Up", SWT.PUSH);
		upButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec();
			}
			public void widgetSelected(SelectionEvent arg0) {
				exec();
			}
			private void exec() {
				moveUp();
			}
			
		});
		upButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		downButton = toolkit.createButton(buttonContainer, "Down", SWT.PUSH);
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

		nodesViewer.setContentProvider(new NodesContentProvider());
		nodesViewer.setLabelProvider(new NodesLabelProvider());
		toolkit.paintBordersFor(container);
		section.setClient(container);
		
		nodesViewer.setInput(getModel());
        updateButtons();

		getPage().getMpeEditor().getConstraintManager().addConstraintListener(this);

	}
	
	public int getTableHightHint() {
		return 120;
	}

	protected abstract String getListDescription();

	protected abstract String getListTitle();
	
	protected abstract String getItemName(Object element);
	
	protected abstract String getExtractorExpression();
	
	protected abstract String getTargetName();
	
	protected abstract void notifyDependents(Node node);
	
	protected abstract void clearDependents();

	protected void updateButtons() {
		Table table = nodesViewer.getTable();
		TableItem[] selection = table.getSelection();
		boolean hasSelection = selection.length > 0;
		boolean canMove = hasSelection && (table.getItemCount() > 1) && (selection.length == 1);
        addButton.setEnabled(true);
        removeButton.setEnabled(hasSelection);
        upButton.setEnabled(canMove && (table.getSelectionIndex() > 0));
        downButton.setEnabled(canMove && (table.getSelectionIndex() < table.getItemCount() - 1));
	}

	protected void nodeSelected(IStructuredSelection selection) {
		updateButtons();
		if (selection.size() == 1) {
			notifyDependents((Node)selection.getFirstElement());
		} else {
			clearDependents();
		}
	}

	protected abstract void addNode();
	
	protected void removeNode() {
		int sel[] = nodesViewer.getTable().getSelectionIndices();
		for (int i = 0; i < sel.length; i++) {
			Node n = listObjects[sel[i]];
			n.getParentNode().removeChild(n);
		}
		refreshList();
		clearDependents();
		updateButtons();
	}


	protected void moveUp() {
		int sel[] = nodesViewer.getTable().getSelectionIndices();
		if ((sel.length == 0) || (sel[0] == 0)) { return; }
		Node n = listObjects[sel[0]];
		Node psib = listObjects[sel[0]-1];
		Node parentNode = n.getParentNode();
		
		parentNode.removeChild(n);
		parentNode.insertBefore(n,psib);
		
		refreshList();
		updateButtons();
		nodesViewer.setSelection(new StructuredSelection(n), true);
	}
	
	protected void moveDown() {
		int sel[] = nodesViewer.getTable().getSelectionIndices();
		if ((sel.length == 0) || (sel[0] > (listObjects.length-2))) { return; }
		Node n = listObjects[sel[0]];
		Node nsib = listObjects[sel[0]+1];
		Node parentNode = n.getParentNode();
		
		parentNode.removeChild(nsib);
		parentNode.insertBefore(nsib,n);
		
		refreshList();
		updateButtons();
		nodesViewer.setSelection(new StructuredSelection(n), true);
	}
	
	protected void refreshList() {
		refreshList(ModelAccess.getNodes(getSourceNode(),getExtractorExpression()));		
	}
	
	protected void refreshList(Node newList[]) {
		listObjects = newList;		
		nodesViewer.setInput(getRoot());
		clearDependents();
	}
	
	public void clear() {
		loadFrom(null);
		clearDependents();
		enableSection(false);
	}

	public void loadFrom(Node source) {
		setSourceNode(source);
		refreshList();
		enableSection(source!=null);
	}

	public void enableSection(boolean enable) {
		nodesViewer.getTable().setEnabled(enable);
		addButton.setEnabled(enable);
		removeButton.setEnabled(enable);
		upButton.setEnabled(enable);
		downButton.setEnabled(enable);
	}
	
	public Node[] getListObjects() {
		if (listObjects == null) { listObjects = new Node[0]; }
		return listObjects;
	}

	public void select(Node node) {
		nodesViewer.setSelection(new StructuredSelection(node), true);
		notifyDependents(node);
	}
	
	public void reselect() {
		if (nodesViewer.getTable().getSelectionCount() != 1) { return; }
		Node n = listObjects[nodesViewer.getTable().getSelectionIndex()];
		nodesViewer.refresh(n,true);
		notifyDependents(n);
	}

	public class NodesContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object parent) {
			return getListObjects();
		}
		public void dispose() {}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
	}


	public class NodesLabelProvider implements ILabelProvider {
		public Image getImage(Object element) {
			return null;
		}
		public String getText(Object element) {
			return getItemName(element);
		}
		public void dispose() {}
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}
		public void addListener(ILabelProviderListener listener) {}
		public void removeListener(ILabelProviderListener listener) {}
	}

	protected Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public String getSourceExpression() {
		return null;
	}

	public void refreshSelected() {
		if (nodesViewer.getTable().getSelectionCount() != 1) { return; }
		Node n = listObjects[nodesViewer.getTable().getSelectionIndex()];
		nodesViewer.refresh(n,true);
	}

	public void setSelection(Node node) {
		Node candidate = node;
		while (candidate != null) {
			if (inListObjects(candidate)) {
				select(candidate);
				setDependentSelection(node);
				candidate = null;
			} else {
				candidate = candidate.getParentNode();
			}
		}
	}

	private boolean inListObjects(Node node) {
		for (int i = 0; i < listObjects.length; i++) {
			if (listObjects[i] == node) {return true; }
		}
		return false;
	}
	
	public abstract void setDependentSelection(Node node);
	
	protected abstract boolean isSectionExpanded();
	
	public void constraintsChecked(ArrayList<ConstraintFailure> problems) {
	
	}
	
	public boolean navigates() { return true; }
		
}
