package org.gramar.base;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

public class UppercaseFirstFunction implements XPathFunction {

	public UppercaseFirstFunction() {

	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		String original = (String) args.get(0);
		return original.substring(0, 1).toUpperCase() + original.substring(1);
	}

}
