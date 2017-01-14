package com.gerken.xaa.mpe.editor;
	

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.core.ModelAccess;
	
	public class DestinationGroupDialog extends Dialog {
	
		private TreeViewer 	viewer;
		private Node		currentGroup;
		private Node		selectedGroup = null;
		
		/**
		 * @param parentShell
		 */
		public DestinationGroupDialog(Shell parentShell, Node currentGroup) {
			super(parentShell);
			this.currentGroup = currentGroup;
		}

		protected Control createDialogArea(Composite parent) {
	
			Composite frame = new Composite(parent, SWT.NONE);
			GridLayout gl = new GridLayout();
			gl.numColumns = 2;
			GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
			gd.horizontalSpan = 2;
			frame.setLayout(gl);
			frame.setLayoutData(gd);

			Label label = new Label(frame,SWT.NONE);
			label.setText("Select the destination group");
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 2;
			label.setLayoutData(gd);

			viewer = new TreeViewer(frame, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
			viewer.addSelectionChangedListener(new ISelectionChangedListener () {
				public void selectionChanged(SelectionChangedEvent e) {
					IStructuredSelection selection = (IStructuredSelection)e.getSelection();
					if (selection.size() == 1) {
						Node group = (Node)selection.getFirstElement();
						if (group == currentGroup) {
							enableOK(false);
							selectedGroup = null;
						} else {
							enableOK(true);
							selectedGroup = group;
						}
					}
				}
			});
			
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.heightHint = 120;
			gd.widthHint  = 120;
			viewer.getTree().setLayoutData(gd);

			viewer.setContentProvider(new GroupsContentProvider());
			viewer.setLabelProvider(new GroupsLabelProvider());
			viewer.setInput(currentGroup.getParentNode());
			viewer.expandAll();
			
			return frame;
		}
		
		/* (non-Javadoc)
		 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 	*/
		protected void createButtonsForButtonBar(Composite parent) {
			super.createButtonsForButtonBar(parent);
			enableOK(false);
		}
	
		private void enableOK(boolean pageIsValid) {
			Button ok = getButton(IDialogConstants.OK_ID);
			if (ok == null) { return; }
			ok.setEnabled(pageIsValid);
		}
	
		public Node getDestinationGroup() {
			return selectedGroup;
		}


		class GroupsContentProvider implements ITreeContentProvider {
			public Object[] getElements(Object parent) {
				return ModelAccess.getNodes(currentGroup.getParentNode(),"group[@root=\"true\"]");		
			}

			public void dispose() {}
			
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}

			public Object[] getChildren(Object parentElement) {
				Node parent = (Node) parentElement;
				String id = ModelAccess.getAttribute(parent,"@id");
				String xpathExpr = "group[@parentId= \""+id+"\"]";
				Node[] kid = ModelAccess.getNodes(parent.getParentNode(),xpathExpr);
				return kid;
			}

			public Object getParent(Object element) {
				Node group = (Node) element;
				String id = ModelAccess.getAttribute(group,"@parentId");
				String xpathExpr = "group[@id= \""+id+"\"]";
				Node[] kid = ModelAccess.getNodes(group.getParentNode(),xpathExpr);
				if (kid.length == 0) {
					return null;
				}
				return kid[0];
			}

			public boolean hasChildren(Object element) {
				return getChildren(element).length > 0;
			}
		}

		class GroupsLabelProvider implements ILabelProvider {
			public Image getImage(Object element) {
				return null;
			}
			public String getText(Object element) {
				Node group = (Node) element;
				String name = group.getNodeName();
				if (name.equals("group")) {
					return ModelAccess.getAttribute(group,"@name");
				}
				return "--- ? ---";
			}
			public void dispose() {}
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			public void addListener(ILabelProviderListener listener) {}
			public void removeListener(ILabelProviderListener listener) {}
		}
		
	}
