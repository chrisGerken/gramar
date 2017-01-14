package com.gerken.xaa.mpe.core;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.w3c.dom.Node;

public abstract class AbstractTreeSection extends AbstractSection {
    
	protected TreeViewer nodesViewer;
	
	private Node[] treeObjects = new Node[0];

	public AbstractTreeSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	protected void commit() {
		super.commit();
	}

	protected void createClient(Section section, FormToolkit toolkit) {

		section.setText(getTreeTitle()); 
		section.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
		section.setDescription(getTreeDescription()); 
		section.setExpanded(isSectionExpanded());

		Composite container = toolkit.createComposite(section);

		TableWrapLayout tl = new TableWrapLayout();
		tl.leftMargin = tl.rightMargin = toolkit.getBorderStyle() != SWT.NULL ? 0 : 2;
		tl.numColumns = 2;
		container.setLayout(tl);
		
		TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		container.setLayoutData(td);

		// table viewer 

		nodesViewer = new TreeViewer(container, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | toolkit.getBorderStyle());
		nodesViewer.addSelectionChangedListener(new ISelectionChangedListener () {
			public void selectionChanged(SelectionChangedEvent e) {
				nodeSelected((IStructuredSelection)e.getSelection());
			}
		});
		
		td = new TableWrapData(TableWrapData.FILL_GRAB);
		td.heightHint = getHeight();
		nodesViewer.getTree().setLayoutData(td);

		nodesViewer.setContentProvider(new NodesContentProvider());
		nodesViewer.setLabelProvider(new NodesLabelProvider());
		toolkit.paintBordersFor(container);
		section.setClient(container);
		
		nodesViewer.setInput(getModel());

		buildMenu(nodesViewer.getTree());
	}
	
	protected abstract void buildMenu(Tree tree);
	
	protected abstract String getTreeDescription();

	protected abstract String getTreeTitle();
	
	protected abstract String getItemName(Object element);
	
	protected abstract String getExtractorExpression();
	
//	protected abstract String getTargetName();
	
	protected void notifyDependents(Node node) {
		notifyDependents(node,node);		
	}
	
	protected abstract void notifyDependents(Node node, Node selected);
	
	
	protected abstract void clearDependents();
	
	protected abstract Node[] getChildrenFor(Node node);

	protected void nodeSelected(IStructuredSelection selection) {
		if (selection.size() == 1) {
			notifyDependents((Node)selection.getFirstElement());
		}
	}
	
	protected void refreshTree() {
		treeObjects = ModelAccess.getNodes(getSourceNode(),getExtractorExpression());		
		nodesViewer.setInput(getRoot());
		clearDependents();
		nodesViewer.expandAll();
	}
	
	public void clear() {
		loadFrom(null);
		clearDependents();
		enableSection(false);
	}

	public void loadFrom(Node source) {
		setSourceNode(source);
		refreshTree();
		enableSection(true);
	}

	private void enableSection(boolean enable) {
		nodesViewer.getTree().setEnabled(enable);
	}
	
	public Node[] getTreeObjects() {
		if (treeObjects == null) { treeObjects = new Node[0]; }
		return treeObjects;
	}

	public void select(Node node) {
		nodesViewer.setSelection(new StructuredSelection(node), true);
		notifyDependents(node);
	}

	class NodesContentProvider implements ITreeContentProvider {
		public Object[] getElements(Object parent) {
			return getTreeObjects();
		}

		public void dispose() {}
		
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

		public Object[] getChildren(Object parentElement) {
			return getChildrenFor((Node)parentElement);
		}

		public Object getParent(Object element) {
			return ((Node)element).getParentNode();
		}

		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	class NodesLabelProvider implements ILabelProvider {
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

	public void reselect() {
		if (nodesViewer.getTree().getSelectionCount() != 1) { return; }
		TreeSelection sel = (TreeSelection) nodesViewer.getSelection();
		Node n = (Node) sel.getFirstElement();
		nodesViewer.refresh(n,true);
		notifyDependents(n);
	}

	public void refreshSelected() {
		if (nodesViewer.getTree().getSelectionCount() != 1) { return; }
		TreeSelection sel = (TreeSelection) nodesViewer.getSelection();
		Node n = (Node) sel.getFirstElement();
		nodesViewer.refresh(n,true);
	}

	public void setSelection(Node node) {
		Node candidate = node;
		
		while ((candidate != null) && (!ofInterest(candidate))) {
			candidate = candidate.getParentNode();
		}		
		
		if (candidate != null) { 
			select(candidate);
			setDependentSelection(candidate,node);
		} else {
			clearDependents();
		}
		
		expandAll();
	}
	
	public abstract boolean ofInterest(Node node);
	
	public abstract void setDependentSelection(Node node, Node selected);
	
	public abstract boolean isSectionExpanded();
	
	public int getHeight() {
		return 120;
	}
	
	public boolean navigates() { return true; }
	
	public void expandAll() {

		nodesViewer.expandAll();

	}
	
}
