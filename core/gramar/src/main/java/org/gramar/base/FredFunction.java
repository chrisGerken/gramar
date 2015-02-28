package org.gramar.base;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

public class FredFunction implements XPathFunction {

	public FredFunction() {

	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		return "Waka Waka!!";
	}

}
