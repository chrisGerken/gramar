package org.gramar.base.function;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List; 

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.gramar.function.GramarFunction;
import org.gramar.function.IGramarFunction;
import org.gramar.model.NodeArray;
import org.w3c.dom.Node;

public class UniqueFunction extends GramarFunction implements IGramarFunction, XPathFunction {

	public UniqueFunction() {
		super();
	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		
		try {

			Node node[] = asNodesArg(args.get(0));
			
			String keyExpression = asStringArg(args.get(1));
			
			HashSet<Object> keys = new HashSet<Object>();
			ArrayList<Node> nodes = new ArrayList<Node>();
			
			for (Node n: node) {
				Object key = context.resolveToObject(keyExpression, n);
				if (!keys.contains(key)) {
					keys.add(key);
					nodes.add(n);
				}
			}
			
			return new NodeArray(nodes); 
 
		} catch (XPathExpressionException e) {
			context.error(e);
		}
		return false;
		
	}

}
