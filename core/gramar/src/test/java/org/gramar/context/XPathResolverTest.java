package org.gramar.context;

import static org.junit.Assert.*;

import javax.xml.namespace.QName;

import org.gramar.context.GramarContext;
import org.gramar.context.XPathResolver;
import org.gramar.model.DocumentHelper;
import org.gramar.platform.SimpleGramarPlatform;
import org.junit.Test;
import org.w3c.dom.Document;


public class XPathResolverTest {

	@Test
	public void testResolveVariable() throws Exception {
		
		String name = "myvar";
		String originalValue = "waka waka";
		
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);
		context.setVariable(name, originalValue);
		XPathResolver resolver = new XPathResolver(context);
		
		String value = (String) resolver.resolveVariable(new QName(name));
		if (!value.equals(originalValue)) {
			fail("Incorrect value returned.");
		}
		
	}

	@Test
	public void testParentResolveVariable() throws Exception {
		
		String name = "myvar";
		String originalValue = "waka waka";
		
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);
		context.setVariable(name, originalValue);
		context = new GramarContext(context);
		XPathResolver resolver = new XPathResolver(context);
		
		String value = (String) resolver.resolveVariable(new QName(name));
		if (!value.equals(originalValue)) {
			fail("Incorrect value returned.");
		}
		
	}

	@Test
	public void testResolveFunction() {
//		fail("Not yet implemented");
	}

}
