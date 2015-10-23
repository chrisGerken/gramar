package org.gramar.base.function;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.gramar.function.GramarFunction;
import org.gramar.function.IGramarFunction;

public class ModuloCounterFunction extends GramarFunction implements XPathFunction, IGramarFunction {
	
	public ModuloCounterFunction() {
		super();
	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		String variableName = asStringArg(args.get(0));
		int modulo = asNumberArg(args.get(1)).intValue();
		
		Integer counter = (Integer) context.getVariable(variableName);
		if (counter == null) {
			counter = new Integer(-1);
		}
		
		counter++;
		counter = counter % modulo;
		context.setVariable(variableName, counter);
		
		return counter;
	}

}
