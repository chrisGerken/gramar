package com.gerken.xaa.mpe.constraint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gerken.xaa.mpe.editor.FileDetailsSection;
import com.gerken.xaa.mpe.editor.GroupDetailsSection;
import com.gerken.xaa.mpe.editor.NamingDetailsSection;
import com.gerken.xaa.mpe.editor.OriginDetailsSection;
import com.gerken.xaa.mpe.editor.ReplacementDetailsSection;
import com.gerken.xaa.mpe.editor.TokenDetailsSection;
import com.gerken.xaa.mpe.editor.XformDetailsSection;

public class ConstraintManager {

	private ConstraintSet cset[];
	private ArrayList<IConstraintListener> listeners = new ArrayList<IConstraintListener>();
	
	private ArrayList<ConstraintFailure> problems;
	
	public ConstraintManager() {
		super();
		loadConstraintSets();
	}

	public void propertyChanged(Node target, String field, Node model) {
		validateNode(target);
	}

	public void elementAdded(Node target) {
		validateNode(target);
	}

	public void modelChanged(Document doc) {
		validateEntireModel(doc);
	}
	
	private void loadConstraintSets() {

		Vector<ConstraintSet> v = new Vector<ConstraintSet>();
		ConstraintSet cs;
		
		cs = XformDetailsSection.getConstraintSet();
		if (!cs.isEmpty()) {
			cs.setNodeName("xform");
			cs.setPageName("Overview");
        	cs.setPageID("OVERVIEW");
			v.addElement(cs);
		}

		cs = NamingDetailsSection.getConstraintSet();
		if (!cs.isEmpty()) {
			cs.addNodeName("createFile");
			cs.addNodeName("createFolder");
			cs.addNodeName("createProject");
			cs.setPageName("Artifact");
			cs.setPageID("ARTIFACT");
			v.addElement(cs);
		}

		cs = FileDetailsSection.getConstraintSet();
		if (!cs.isEmpty()) {
			cs.addNodeName("createFile");
			cs.addNodeName("createFolder");
			cs.addNodeName("createProject");
			cs.setPageName("Artifact");
			cs.setPageID("ARTIFACT");
			v.addElement(cs);
		}

		cs = OriginDetailsSection.getConstraintSet();
		if (!cs.isEmpty()) {
			cs.addNodeName("createFile");
			cs.addNodeName("createFolder");
			cs.addNodeName("createProject");
			cs.setPageName("Artifact");
			cs.setPageID("ARTIFACT");
			v.addElement(cs);
		}

		cs = ReplacementDetailsSection.getConstraintSet();
		if (!cs.isEmpty()) {
			cs.setNodeName("replacement");
			cs.setPageName("Replacement");
			cs.setPageID("REPLACEMENT");
			v.addElement(cs);
		}

		cs = TokenDetailsSection.getConstraintSet();
		if (!cs.isEmpty()) {
			cs.setNodeName("token");
			cs.setPageName("Token");
			cs.setPageID("TOKEN");
			v.addElement(cs);
		}

		cs = GroupDetailsSection.getConstraintSet();
		if (!cs.isEmpty()) {
			cs.setNodeName("group");
			cs.setPageName("Group");
			cs.setPageID("GROUP");
			v.addElement(cs);
		}

		cset = new ConstraintSet[v.size()];
		v.copyInto(cset);

	}
	
	public void addConstraintListener(IConstraintListener listener) {
		listeners.add(listener);
	}
	
	public void validateNode(Node node) {
		problems = new ArrayList<ConstraintFailure>();
		validateNode(node,problems,new ConstraintContext(),false);
		Iterator<IConstraintListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			iter.next().constraintsChecked(problems);
		}
	}
	
	public void validateEntireModel(Node node) {
		problems = new ArrayList<ConstraintFailure>();
		Node doc = null;
		if (node.getNodeType() == Node.DOCUMENT_NODE) { doc = node; }
		else { doc = node.getOwnerDocument(); }
		validateNode(doc,problems,new ConstraintContext(),true);
		Iterator<IConstraintListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			iter.next().constraintsChecked(problems);
		}
	}
	
	public void validateNode(Node node, ArrayList<ConstraintFailure> problems, ConstraintContext context, boolean recurse) {
		for (int i = 0; i < cset.length; i++) {
			if (cset[i].appliesTo(node)) {
				problems.addAll(cset[i].validate(node,context));
			}
		}
		if (!recurse) { return; }
		NodeList nlist = node.getChildNodes();
		for (int i = 0; i < nlist.getLength(); i++) {
			Node kid = nlist.item(i);
			if (!"ignored".equals(kid.getNodeName())) {
				validateNode(nlist.item(i),problems,context,recurse);
			}
		}
		
	}
	
	public ArrayList<ConstraintFailure> getCurrentProblems() {
		return problems;
	}

}
