package org.gramar.base.function.util;

import org.w3c.dom.Node;

/**
 * An object that represents a Node in a set of Nodes to be sorted. The key can be a
 * String or a Double and the type is determined on the fly. The direction of the
 * sort can be ascending or descending.
 * 
 * @author chrisgerken
 *
 */
public class SortableNode implements Comparable<SortableNode> {

	private Object key;
	private Node node;
	private int asc;
	
	private boolean keyIsString;
	
	public SortableNode(Object key, Node node, boolean asc) {
		this.key = key;
		this.node = node;
		this.asc = asc ? 1 : -1;
		keyIsString = key instanceof String;
	}

	public Node getNode() {
		return node;
	}
	
	@Override
	public int compareTo(SortableNode o) {
		
		if (keyIsString == o.keyIsString) {
			if (keyIsString) {
				String k = (String) key;
				return asc * k.compareTo((String)o.key);
			} else {
				Double k = (Double) key;
				return asc * k.compareTo((Double)o.key);
			}
		}
		
		// If we get this far then the caller is doing something screwy, so we just wash
		// our hands of this mess and return 0
		return 0;
	}

}
