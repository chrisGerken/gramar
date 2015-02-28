package org.gramar.context;

import static org.junit.Assert.*;

import javax.xml.xpath.XPathConstants;

import org.gramar.IGramarContext;
import org.gramar.IModel;
import org.gramar.context.GramarContext;
import org.gramar.model.DocumentHelper;
import org.gramar.model.ModelAccess;
import org.gramar.platform.SimpleGramarPlatform;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public class ModelAccessTest {

	@Test
	public void testGetNodes() throws Exception {
		Node n[] = nodes("/models/simple01.xml", "/root/fred");
		if (n.length != 4) {
			fail("Wrong number of nodes returned");
		}
	}

	@Test
	public void testGetNodes01() throws Exception {
		GramarContext context = context("/models/simple01.xml");
		context.setVariable("color", "blue");
		Node node[] = ModelAccess.getDefault().getNodes(context.getPrimaryModel(), "/root/fred[@color=$color]", true, context);
		if (node.length != 2) {
			fail("Wrong number of nodes returned");
		}
	}

	@Test
	public void testGetString01() throws Exception {
		GramarContext context = context("/models/simple01.xml");
		String value = (String) ModelAccess.getDefault().resolve(context.getPrimaryModel(), "/root/fred[@id='b']/@color", context, XPathConstants.STRING);
		if (!value.equals("yellow")) {
			fail("Wrong color");
		}
	}

	@Test
	public void testGetString02() throws Exception {
		GramarContext context = context("/models/simple01.xml");
		context.setVariable("id", "b");
		String value = (String) ModelAccess.getDefault().resolve(context.getPrimaryModel(), "/root/fred[@id=$id]/@color", context, XPathConstants.STRING);
		if (!value.equals("yellow")) {
			fail("Wrong color");
		}
	}

	@Test
	public void testGetString03() throws Exception {
		GramarContext context = context("/models/simple01.xml");
		Document other = DocumentHelper.loadDocument("/models/simple02.xml");
		context.addModel("look", other);
//		Node node[] = ModelAccess.getDefault().getNodes(context.getPrimaryModel(), "$look/lookup/pair[@key='y']", true, context);
		String value = (String) ModelAccess.getDefault().resolve(context.getPrimaryModel(), "$look/lookup/pair[@key='y']/@value", context, XPathConstants.STRING);
		if (!value.equals("why")) {
			fail("Wrong value");
		}
	}

	@Test
	public void testGetBoolean02() throws Exception {
		GramarContext context = context("/models/simple01.xml");
		Boolean test = (Boolean) ModelAccess.getDefault().resolve(context.getPrimaryModel(), "/root/mike", context, XPathConstants.BOOLEAN);
		if (test) {
			fail("Wrong boolean");
		}
	}

	@Test
	public void testGetBoolean03() throws Exception {
		GramarContext context = context("/models/simple01.xml");
		Boolean test = (Boolean) ModelAccess.getDefault().resolve(context.getPrimaryModel(), "/root/fred[@id='a']", context, XPathConstants.BOOLEAN);
		if (!test) {
			fail("Wrong boolean");
		}
	}

	@Test
	public void testGetBoolean04() throws Exception {
		GramarContext context = context("/models/simple01.xml");
		context.setVariable("id", "a");
		Boolean test = (Boolean) ModelAccess.getDefault().resolve(context.getPrimaryModel(), "/root/fred[@id=$id]", context, XPathConstants.BOOLEAN);
		if (!test) {
			fail("Wrong boolean");
		}
	}

	@Test
	public void testGetBoolean05() throws Exception {
		GramarContext context = context("/models/simple01.xml");
		context.setVariable("id", "x");
		Boolean test = (Boolean) ModelAccess.getDefault().resolve(context.getPrimaryModel(), "/root/fred[@id=$id]", context, XPathConstants.BOOLEAN);
		if (test) {
			fail("Wrong boolean");
		}
	}

	@Test
	public void testSetAttribute() {
//		fail("Not yet implemented");
	}

	@Test
	public void testNewNode() {
//		fail("Not yet implemented");
	}
	
	
	
	private GramarContext context(String resource) throws Exception {
		Document model = DocumentHelper.modelFromResource(resource).asDOM();
		GramarContext context = new GramarContext(new SimpleGramarPlatform(),model);
		return context;
	}
	
	private Node[] nodes(String resource, String expr) throws Exception {
		GramarContext context = context(resource);
		return ModelAccess.getDefault().getNodes(context.getPrimaryModel(), expr, true, context);
	}

}
