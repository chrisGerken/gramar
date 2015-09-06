package org.gramar.base.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List; 

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.gramar.base.function.util.SortableNode;
import org.gramar.function.GramarFunction;
import org.gramar.function.IGramarFunction;
import org.gramar.model.NodeArray;
import org.w3c.dom.Node;

public class SortFunction extends GramarFunction implements IGramarFunction, XPathFunction {

	public SortFunction() {
		super();
	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		
		try {

			Node node[] = asNodesArg(args.get(0));
			
			String keyExpression = asStringArg(args.get(1));
			
			String sortDetails = "string asc";
			if (args.size() > 2) {
				sortDetails = asStringArg(args.get(2)).toLowerCase();
			}
			
			boolean stringSort = true;
			boolean ascSort = true;
			
			if (sortDetails.indexOf("number") > -1) {
				stringSort = false;
			}
			
			if (sortDetails.indexOf("desc") > -1) {
				ascSort = false;
			}
			
			ArrayList<SortableNode> sortables = new ArrayList<SortableNode>();
						
			for (Node n: node) {
			
				Object key = null;
				if (stringSort) {
					key = context.resolveToString(keyExpression, n);
				} else {
					key = context.resolveToNumber(keyExpression, n);
				}
				
				sortables.add(new SortableNode(key, n, ascSort));
				
			}

			SortableNode sorted[] = new SortableNode[sortables.size()];
			sortables.toArray(sorted);
			Arrays.sort(sorted);
			
			node = new Node[sorted.length];
			for (int i = 0; i < node.length; i++) {
				node[i] = sorted[i].getNode();
			}
			
			return new NodeArray(node); 
 
		} catch (XPathExpressionException e) {
			context.error(e);
		}
		return false;
		
	}
	
	

}
