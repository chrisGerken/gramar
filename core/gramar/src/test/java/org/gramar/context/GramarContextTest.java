package org.gramar.context;

import static org.junit.Assert.*;

import org.gramar.context.GramarContext;
import org.gramar.model.DocumentHelper;
import org.gramar.platform.SimpleGramarPlatform;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class GramarContextTest {

	@Test
	public void testResolveObjects01() throws Exception {
		String expression = " /root/fred ";
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);
		Object result = context.resolveToObject(expression);
		if (!(result instanceof Node)) {
			fail("Incorrect result type");
		}
	}

	@Test
	public void testResolveObjects02() throws Exception {
		String expression = " 3 + 4 ";
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);
		Object result = context.resolveToObject(expression);
		if (!(result instanceof Double)) {
			fail("Incorrect result type");
		}
	}

	@Test
	public void testResolveObjects03() throws Exception {
		String expression = " 'abc' ";
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);
		Object result = context.resolveToObject(expression);
		if (!(result instanceof String)) {
			fail("Incorrect result type");
		}
	}

	@Test
	public void testResolveObjects04() throws Exception {
		String expression = " /root/fredx ";
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);
		Object result = context.resolveToObject(expression);
		if (!result.equals("")) {
			fail("Incorrect result value");
		}
	}

	@Test
	public void testResolve01() throws Exception {
		String expression = "abc{/root/fred[@id='c']/@color}def";
		String correct = "abcbluedef";
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);
		String result = context.resolveExpressions(expression);
		if (!result.equals(correct)) {
			fail("Failed resolve expression: "+expression);
		}
	}

	@Test
	public void testGetPrimaryModel() throws Exception {
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);
		if (context.getPrimaryModel() != doc) {
			fail("Fumbled the primary model");
		}
	}

	@Test
	public void testGetVariable() throws Exception {
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);
		context.setVariable("color", "blue");
		if (!"blue".equals(context.getVariable("color"))) {
			fail("Fumbled a variable");
		}
	}

	@Test
	public void testSetAttribute() throws Exception {
		Document doc = DocumentHelper.loadDocument("/models/simple01.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);

		boolean test = context.resolveToBoolean("/root/biff/@newvar");
		if (test) {
			fail("Seeing things that are not there");
		}
		
		context.setAttribute("/root/biff", "newvar", "Howdy");

		test = context.resolveToBoolean("/root/biff/@newvar");
		if (!test) {
			fail("Not seeing things that are really there");
		}
		
		String value = context.resolveToString("/root/biff/@newvar");
		if (!"Howdy".equals(value)) {
			fail("Not getting the correct value back");
		}
		
	}

	@Test
	public void testResolveToString() throws Exception {
		Document doc = DocumentHelper.loadDocument("/models/simple02.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);

		String value = context.resolveToString("/lookup/pair[@key='z']/@value");
		if (!"zee".equals(value)) {
			fail("Did not get the correct value");
		}
	}

	@Test
	public void testResolveToNode() throws Exception {
		Document doc = DocumentHelper.loadDocument("/models/simple02.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);

		Node node = context.resolveToNode("/lookup/pair[@key='y']");
		context.setVariable("pair", node);

		String value = context.resolveToString("$pair/@value");
		if (!"why".equals(value)) {
			fail("Did not get the correct node");
		}
	}

	@Test
	public void testResolveToNodes() throws Exception {
		Document doc = DocumentHelper.loadDocument("/models/simple02.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);

		Node node[] = context.resolveToNodes("/lookup/pair");
		if (node.length != 3) {
			fail("Did not get the correct number of nodes");
		}
	}

	@Test
	public void testResolveToBoolean() throws Exception {
		Document doc = DocumentHelper.loadDocument("/models/simple02.xml");
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),doc);

		Boolean test = context.resolveToBoolean("/lookup/pair[@key='a']");
		if (test) {
			fail("Failed to fail test");
		}

		test = context.resolveToBoolean("/lookup/pair[@key='x']");
		if (!test) {
			fail("Failed to test");
		}
	}

}
