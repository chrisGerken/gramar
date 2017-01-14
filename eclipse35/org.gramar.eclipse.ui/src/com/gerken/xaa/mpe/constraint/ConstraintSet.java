package com.gerken.xaa.mpe.constraint;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Node;

public class ConstraintSet {

	private int		owningSection;
	private String[]	nodeName = null;
	private ArrayList<String> nodeNames = new ArrayList<String>();
	private String  pageName;
	private String  pageID;
	
	private ArrayList<Constraint> constraints = new ArrayList<Constraint>();
	private ArrayList<ConstraintFailure> problems;
	
	public ConstraintSet(int owningSection) {
		this.owningSection = owningSection;
	}

	public void addConstraint(Constraint constraint) {
		constraints.add(constraint);
		constraint.setOwner(this);
	}
	
	public ArrayList<ConstraintFailure> validate(Node target, ConstraintContext context) {
		Iterator<Constraint> iter = constraints.iterator();
		problems = new ArrayList<ConstraintFailure>();
		
		while (iter.hasNext()) {
			iter.next().check(target,context);
		}
		
		return problems;
	}
	
	public ConstraintFailure addFailure(ConstraintFailure failure) {
		problems.add(failure);
		failure.setReportingConstraintSet(getOwningSection());
		failure.setPageID(getPageID());
		failure.setPageName(getPageName());
		return failure;
	}

	public int getOwningSection() {
		return owningSection;
	}

	public void setOwningSection(int owningSection) {
		this.owningSection = owningSection;
	}

	public String[] getNodeName() {
		if (nodeName == null) {
			nodeName = new String[nodeNames.size()];
			nodeNames.toArray(nodeName);
		}
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		addNodeName(nodeName);
	}
	
	public boolean appliesTo(Node node) {
		return appliesTo(node.getNodeName());
	}
	
	public boolean appliesTo(String name) {
		getNodeName();
		for (int i = 0; i < getNodeName().length; i++) {
			if (name.equals(getNodeName()[i])) {
				return true;
			}
		}
		return false;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageID() {
		return pageID;
	}

	public void setPageID(String pageID) {
		this.pageID = pageID;
	}

	public String toString() {
		return getNodeName()+"("+getOwningSection()+") on "+getPageName();
	}	
	
	public boolean isEmpty() {
		return constraints.isEmpty(); 
	}

	public void addNodeName(String name) {
		nodeName = null;
		nodeNames.add(name);
	}
}
