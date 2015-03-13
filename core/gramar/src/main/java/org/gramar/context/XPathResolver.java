package org.gramar.context;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathFunction;
import javax.xml.xpath.XPathFunctionResolver;
import javax.xml.xpath.XPathVariableResolver;

import org.gramar.IGramarContext;
import org.gramar.exception.GramarException;
import org.gramar.function.IGramarFunction;


public class XPathResolver implements XPathFunctionResolver, XPathVariableResolver {

	private IGramarContext context;
	
	public XPathResolver(IGramarContext context) {
		this.context = context;
	}

	@Override
	public Object resolveVariable(QName variableName) {
		Object value = context.getVariable(variableName.getLocalPart());
		if (value == null) {
			context.error("Unresolved valriable: "+variableName.getLocalPart());
		}
		return context.getVariable(variableName.getLocalPart());
	}

	@Override
	public XPathFunction resolveFunction(QName functionName, int arity) {
		XPathFunction function = null;
		try {
			String namespace = functionName.getNamespaceURI();
			String name = functionName.getLocalPart();
			function = context.getXPathFunction(namespace, name, arity);
			if (function instanceof IGramarFunction) {
				((IGramarFunction)function).setContext(context);
			}
		} catch (GramarException e) {
			context.error(e);
		}
		return function;
	}

}
