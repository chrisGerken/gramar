package org.gramar.base.function;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.gramar.function.GramarFunction;
import org.gramar.function.IGramarFunction;

public class LessThanFunction extends GramarFunction implements XPathFunction, IGramarFunction {
	
	public LessThanFunction() {
		super();
	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		Double op1 = asNumberArg(args.get(0));
		Double op2 = asNumberArg(args.get(1));
		
		return op1 < op2;
	}

}
