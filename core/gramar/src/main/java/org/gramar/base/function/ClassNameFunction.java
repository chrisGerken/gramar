package org.gramar.base.function;

import java.util.List;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.gramar.function.GramarFunction;
import org.w3c.dom.NodeList;

public class ClassNameFunction extends GramarFunction implements XPathFunction {

	public ClassNameFunction() {

	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		String fqn = asStringArg(args.get(0));
		int index = fqn.lastIndexOf('.');
		if (index > -1) {
			return fqn.substring(index+1);
		}
		return fqn;
	}

}
