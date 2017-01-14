package com.gerken.xaa.mpe.editor;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.AbstractSection;
import com.gerken.xaa.mpe.core.AbstractTreeSection;
import com.gerken.xaa.mpe.core.ModelAccess;

public abstract class AbstractGroupTreeSection extends AbstractTreeSection {

	public AbstractGroupTreeSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	public String getTreeTitle() {
		return "Groups";
	}

	public String getTreeDescription() {
		return "Group Structure";
	}

	public String getSourceExpression() {
		return ".";
	}

	public String getExtractorExpression() {
		return "group[@root=\"true\"]";
	}
	
	public int getHeight() {
		return 150;
	}

	@Override
	protected void buildMenu(Tree tree) {
		Menu menu = tree.getMenu();
		
		if (menu == null) {
			menu = new Menu(tree);
			tree.setMenu(menu);
		}
		
		MenuItem item = new MenuItem(menu, SWT.NONE);
		item.setText("New SubGroup");
		item.setEnabled(true);
		item.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				exec(arg0);
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec(arg0);
			}
			private void exec(SelectionEvent arg0) {
				TreeSelection ts = (TreeSelection) nodesViewer.getSelection();
				Node  node = (Node) ts.getFirstElement();
				if (node == null) { return; }
				NewGroupDialog ngd = new NewGroupDialog(new Shell());
				ngd.setBlockOnOpen(true);
				if (ngd.open() == ngd.OK) {
					Element kid = node.getOwnerDocument().createElement("group");
					node.getParentNode().appendChild(kid);
					kid.setAttribute("name",ngd.getName());
					String newID = getPage().getMpeEditor().getNextID();
					String parentID = ModelAccess.getAttribute(node, "@id");
					kid.setAttribute("id", newID);
					kid.setAttribute("parentId", parentID);
					getPage().getMpeEditor().elementAdded(kid);
					refreshTree();
					select(node);
				}
				
			}
		});
		
		item = new MenuItem(menu, SWT.NONE);
		item.setText("Delete");
		item.setEnabled(true);
		item.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent arg0) {
				exec(arg0);
			}
			public void widgetDefaultSelected(SelectionEvent arg0) {
				exec(arg0);
			}
			private void exec(SelectionEvent arg0) {
				TreeSelection ts = (TreeSelection) nodesViewer.getSelection();
				Node  node = (Node) ts.getFirstElement();
				if (node == null) { return; }
				node.getParentNode().removeChild(node);
				refreshTree();
			}
		});
	}

	public Node[] getChildrenFor(Node parent) {
			// Begin getChild() custom content logic (1309892008792)
		String id = ModelAccess.getAttribute(parent,"@id");
		String xpathExpr = "group[@parentId= \""+id+"\"]";
		Node[] kid = ModelAccess.getNodes(getSourceNode(),xpathExpr);
		return kid;
			// End getChild() custom content logic (1309892008792)
	}
		
	protected String getItemName(Object obj) {
			// Begin getItemName() custom content logic (1309892008792)
		Node element = (Node) obj;
		String name = element.getNodeName();
		if (name.equals("group")) {
				// Begin label calculation for group
			return bind(element,"{@name}");
				// End label calculation for group
		}
		return "--- ? ---";
			// End getItemName() custom content logic (1309892008792)
	}

	public boolean isSectionExpanded() {
		return true;
	}

	public abstract void notifyDependents(Node node, Node selected);
	
	public abstract void clearDependents();

	protected boolean isPrimary() {
		return true;
	}

	public abstract void setDependentSelection(Node node, Node selected);
	
	public boolean ofInterest(Node node) {
		String name = node.getNodeName();
		if (name.equals("group")) { return true; }
		return false;
	}

}
