package com.gerken.xaa.mpe.domain;

import java.util.ArrayList;

import org.w3c.dom.Node;

import com.gerken.xaa.mpe.editor.XaaEditor;

public abstract class AbstractEnumeration {

	private boolean volatileDomain = true;
	private XaaEditor	 editor;
	protected String  key[] 	 = null;
	protected String  text[] 	 = null;
	private	Node 	target   = null;

	public AbstractEnumeration(XaaEditor editor) {
		this.editor = editor;
	}

	protected XaaEditor getEditor() {
		return editor;
	}
	
	public void buildDomain() {
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		addConstants(keys,values);
		key = keys.toArray(new String[0]);
		text = values.toArray(new String[0]);
	}
	
	public abstract void addConstants(ArrayList<String> keys, ArrayList<String> values);
	
	protected abstract String keyExpression();
	
	protected abstract String valueExpression();

	public void setTarget(Node target) {
		this.target = target;
		key = null;
		text = null;
	}

	public Node getTarget() {
		return target;
	}
	
	public String[] getKeys() {
		if (volatileDomain || (key == null)) { buildDomain(); }
		return key;
	}

	public String[] getTexts() {
		if (volatileDomain || (text == null)) { buildDomain(); }
		return text;
	}
	
	public String getKey(int index) {
		return getKeys()[index];
	}
	
	public String getText(int index) {
		return getTexts()[index];
	}
	
	public int getKeyIndex(String _key) {
		getKeys();
		for (int i = 0; i < key.length; i++) {
			if (key[i].equals(_key)) { return i; }
		}
		return -1;
	}
	
	public String textFor(String _key) {
		int sel = getKeyIndex(_key);
		if (sel < 0) { return ""; }
		return getText(sel);
	}
	
	public int size() {
		return getKeys().length;
	}

}
