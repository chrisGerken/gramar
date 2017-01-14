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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.core.ModelAccess;
	
	public class TokenFromReplacementDialog extends Dialog {
	
		private TreeViewer 	viewer;
		private Node		selectedGroup = null;
		private Text		textToken;
		private String		tokenName;
		private String 		groupName;
		private String		groupId;
		private Node		xform;
		
		/**
		 * @param parentShell
		 */
		public TokenFromReplacementDialog(Shell parentShell, Node xform) {
			super(parentShell);
			this.xform = xform;
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
			
			Composite treeFrame = new Composite(frame, SWT.NONE);
			gl = new GridLayout();
			gl.numColumns = 1;
			gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL);
			gd.horizontalSpan = 2;
			treeFrame.setLayout(gl);
			treeFrame.setLayoutData(gd);

			viewer = new TreeViewer(treeFrame, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
			viewer.addSelectionChangedListener(new ISelectionChangedListener () {
				public void selectionChanged(SelectionChangedEvent e) {
					IStructuredSelection selection = (IStructuredSelection)e.getSelection();
					if (selection.size() == 1) {
						Node group = (Node)selection.getFirstElement();
						enableOK(validate());
						selectedGroup = group;
						groupName = ModelAccess.getAttribute(selectedGroup,"@name");
						groupId = ModelAccess.getAttribute(selectedGroup,"@id");
					}
				}
			});
			
			label = new Label(frame,SWT.SHADOW_NONE);
			label.setText("Token Name");
			gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING); 
			label.setLayoutData(gd);

			textToken = new Text(frame,SWT.SINGLE | SWT.SIMPLE | SWT.BORDER);
			gd = new GridData(GridData.FILL_HORIZONTAL); 
			textToken.setLayoutData(gd);
			textToken.setText("");
			textToken.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent arg0) {
					tokenName = textToken.getText();
					enableOK(validate());
				}
			});
			
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.heightHint = 120;
			gd.widthHint  = 120;
			viewer.getTree().setLayoutData(gd);

			viewer.setContentProvider(new GroupsContentProvider());
			viewer.setLabelProvider(new GroupsLabelProvider());
			viewer.setInput(xform);
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
	
		private boolean validate() {
			return ((selectedGroup != null) && (tokenName != null) && (tokenName.trim().length() > 0));
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
				return ModelAccess.getNodes(xform,"group[@root=\"true\"]");		
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

		public String getTokenName() {
			return tokenName;
		}

		public String getGroupName() {
			return groupName;
		}

		public String getGroupId() {
			return groupId;
		}
		
	}
