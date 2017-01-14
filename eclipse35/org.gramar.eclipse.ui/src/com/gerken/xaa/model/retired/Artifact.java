package com.gerken.xaa.model.retired;

import org.w3c.dom.Node;

public abstract class Artifact extends ModelElement {
	
	private String		purpose;
	private boolean		unique;
	private String		group;
	private boolean		used;
	private Generation	generation;

	public Artifact(Generation generation) {
		this.generation = generation;
	}

	public Artifact(Node node) {
		this.purpose = getAttr(node,"purpose");
		this.unique = getBooleanAttr(node,"unique");
		this.group = getAttr(node,"group");
		this.used = getBooleanAttr(node,"used");
	}

	public void writeArtifactAttributes(StringBuffer sb) {
		writeAttr(sb,"purpose",purpose);
		writeAttr(sb,"unique",String.valueOf(unique));
		writeAttr(sb,"group",group);
		writeAttr(sb,"used",String.valueOf(used));
	}
	
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public Generation getGeneration() {
		return generation;
	}

	public void setGeneration(Generation generation) {
		this.generation = generation;
	}

}
