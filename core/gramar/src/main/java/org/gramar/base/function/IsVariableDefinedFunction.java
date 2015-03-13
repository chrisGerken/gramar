package org.gramar.base.function;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.gramar.function.GramarFunction;
import org.gramar.function.IGramarFunction;

public class IsVariableDefinedFunction extends GramarFunction implements XPathFunction, IGramarFunction {
	
	public IsVariableDefinedFunction() {
		super();
	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		String variableName = asStringArg(args.get(0));
		
		return context.getVariable(variableName) != null;
	}

}
