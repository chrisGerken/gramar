package org.gramar.model;

import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class NodeArray implements NodeList {

	private Node nodes[];
	
	public NodeArray(Node[] nodes) {
		this.nodes = nodes;
	}
	
	public NodeArray(List<Node> nodes) {
		this.nodes = new Node[nodes.size()];
		nodes.toArray(this.nodes);
	}

	@Override
	public Node item(int index) {
		return nodes[index];
	}

	@Override
	public int getLength() {
		return nodes.length;
	}

}
