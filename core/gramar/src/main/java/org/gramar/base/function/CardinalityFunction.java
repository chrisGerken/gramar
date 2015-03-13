package org.gramar.base.function;

import java.util.List;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.gramar.function.GramarFunction;
import org.gramar.function.IGramarFunction;
import org.w3c.dom.Node;

public class CardinalityFunction extends GramarFunction implements IGramarFunction, XPathFunction {

	public CardinalityFunction() {
		super();
	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		
		try {
			String expression = asStringArg(args.get(0));
			Node node[] = context.resolveToNodes(expression);
			
			String cardinality = asStringArg(args.get(1));
			
			if (node.length == 0) {
				return (cardinality.indexOf('0') > -1);
			} else if (node.length == 1) {
				return (cardinality.indexOf('1') > -1);
			} else {
				return (cardinality.indexOf('M') > -1);
			} 
		} catch (XPathExpressionException e) {
			context.error(e);
		}
		return false;
		
	}

}
