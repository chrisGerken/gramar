package org.gramar.base.function;

import java.util.List;

import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionException;

import org.gramar.function.GramarFunction;

public class PackageNameFunction extends GramarFunction implements XPathFunction {

	public PackageNameFunction() {

	}

	@Override
	public Object evaluate(List args) throws XPathFunctionException {
		String fqn = asStringArg(args.get(0));
		int index = fqn.lastIndexOf('.');
		if (index > -1) {
			return fqn.substring(0,index);
		}
		return "";
	}

}
