package org.gramar.context;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;

import org.gramar.IGramarContext;
import org.gramar.exception.GramarException;


public class XPathResolver implements XPathFunctionResolver, XPathVariableResolver {

	private IGramarContext context;
	
	public XPathResolver(IGramarContext context) {
		this.context = context;
	}

	@Override
	public Object resolveVariable(QName variableName) {
		return context.getVariable(variableName.getLocalPart());
	}

	@Override
	public XPathFunction resolveFunction(QName functionName, int arity) {
		XPathFunction function = null;
		try {
			function = context.getXPathFunction(functionName.getLocalPart(), arity);
		} catch (GramarException e) {

		}
		return function;
	}

}
