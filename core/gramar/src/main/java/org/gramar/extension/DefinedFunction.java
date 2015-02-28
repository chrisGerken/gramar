package org.gramar.extension;

import javax.xml.xpath.XPathFunction;

public class DefinedFunction {

	private String name;
	private int parms;
	private boolean variableNumberOfParms;
	private XPathFunction function;
	
	public DefinedFunction(String name, int parms, boolean variableNumberOfParms, XPathFunction function) {
		super();
		this.name = name;
		this.parms = parms;
		this.variableNumberOfParms = variableNumberOfParms;
		this.function = function;
	}

	public String getName() {
		return name;
	}

	public int getParms() {
		return parms;
	}

	public XPathFunction getFunction() {
		return function;
	}

	public boolean matches(String aName, int numberOfArgs) {

		if (!name.equals(aName)) {
			return false;
		}
		
		if (variableNumberOfParms) {
			return parms <= numberOfArgs;
		}
		
		return parms == numberOfArgs;

	}

}
