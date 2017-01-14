package com.gerken.xaa.mpe.editor;


import java.util.Hashtable;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.AbstractToolSection;
import com.gerken.xaa.mpe.core.ModelAccess;

public class PackageToolsSection extends AbstractToolSection implements IHyperlinkListener {


	public PackageToolsSection(AbstractFormPage page, Composite parent) {
		super(page, parent);
	}

	public String getTextContent() {

		Node group[] = targetGroups();
		
		String content = "<form>";
		for (int i = 0; i < group.length; i++) {
			String name = ModelAccess.getAttribute(group[i], "@name");
			String id = ModelAccess.getAttribute(group[i], "@id");
			content = content + "<li style=\"text\" bindent=\"5\">Create token in group <a href=\""+id+"\">"+name+"</a>.</li>";
		}
		content = content + "</form>";

		return content;
	}

	public void linkActivated(HyperlinkEvent e) {
		String href = e.getHref().toString();


		if (getPage().getMpeEditor().getConstraintManager().getCurrentProblems().size() > 0) {
			boolean goon = MessageDialog.openConfirm(new Shell(),"Errors Exist in Model","Errors in the model may cause this action to fail.  Do you wish to continue?");
			if (!goon) { return; }
		}
		try {
			boolean goon = MessageDialog.openConfirm(new Shell(),"Begin Action","Create tokens?");
			if (!goon) { return; }
			
			Node group = ModelAccess.getNodes(getModel(), "/xform/group[@id=\""+href+"\"]")[0];
			String groupName = ModelAccess.getAttribute(group, "@name");
			String groupId = ModelAccess.getAttribute(group, "@id");
			
			Node javaPkg = getSourceNode();
			String varroot = ModelAccess.getAttribute(javaPkg, "@purpose");
			String pkgName = ModelAccess.getAttribute(javaPkg, "@name");

			String pkgvar = varroot + "Pkg";
			String dirvar = varroot + "Dir";

			ModelAccess.createToken(group.getOwnerDocument(), pkgvar, groupId, true, pkgName);

			String formula = "{translate($"+groupName+"/@"+pkgvar+",'.','/')}";
			ModelAccess.createToken(group.getOwnerDocument(), dirvar, groupId, true, formula);
			
			String oldString = pkgName;
			String newString = "{$"+groupName+"/@"+pkgvar+"}";
			ModelAccess.createReplacement(group.getOwnerDocument(), oldString, newString);
			
			oldString = pkgName.replace('.','/');
			newString = "{$"+groupName+"/@"+dirvar+"}";
			ModelAccess.createReplacement(group.getOwnerDocument(), oldString, newString);
			
			
		} catch (Throwable t) {
			
		}

	}
	
	public Node[] targetGroups() {
		
		if (getSourceNode() == null) {
			return new Node[0];
		}

		int size = ModelAccess.getNodes(getModel(), "/xform/group[@name!=\"ignored\"]").length;
		Node flat[] = new Node[size];
		flat[0] = ModelAccess.getNodes(getModel(), "/xform/group[@root=\"true\"]")[0];
		int index = 1;
		int current = 0;
		int parent[] = new int[size];
		parent[0] = -1;
		boolean show[] = new boolean[size];
		show[0] = false;
		while (index < flat.length) {
			String id = ModelAccess.getAttribute(flat[current],"@id");
			Node kid[] = ModelAccess.getNodes(getModel(), "/xform/group[@parentId=\""+id+"\"]");
			for (int i = 0; i < kid.length; i++) {
				flat[index] = kid[i];
				parent[index] = current;
				index++;
			}
			current++;
		}
		
		String pkg = ModelAccess.getAttribute(getSourceNode(), "@name");
		String xpath = "/xform/group[@name!=\"ignored\"]/createFile/javaPkg[@name=\""+pkg+"\"]";
		Node[] samepkg = ModelAccess.getNodes(getModel(), xpath);
		
		Hashtable<String, Node> ht = new Hashtable<String, Node>();
		for (int i = 0; i < samepkg.length; i++) {
			Node group = ModelAccess.getNodes(samepkg[i], "../..")[0];
			String id = ModelAccess.getAttribute(group, "@id");
			ht.put(id,group);
		}
		
		Node group[] = new Node[ht.size()];
		ht.values().toArray(group);
		for (int i = 0; i < group.length; i++) {
			for (int j = 1; j < size; j++) {
				if (flat[j] == group[i]) {
					index = j;
					while (index > 0) {
						show[index] = true;
						index = parent[index];
					}
				}
			}
		}
		
		size = 0;
		for (int i = 0; i < show.length; i++) {
			if (show[i]) { size++; }
		}
		
		Node result[] = new Node[size];
		index = 0;
		for (int i = 0; i < show.length; i++) {
			if (show[i]) { 
				result[index] = flat[i];
				index++;
			}
		}
		
		return result;
	}

	public void linkEntered(HyperlinkEvent e) {}

	public void linkExited(HyperlinkEvent e) {}

	public boolean isPrimary() {
		return true;
	}

	public String getText() {
		return "Package Actions";
	}
	
	public String getDescription() {
		return "Click to perform an action";
	}

	public boolean isSectionExpanded() {
		return true;
	}

}
