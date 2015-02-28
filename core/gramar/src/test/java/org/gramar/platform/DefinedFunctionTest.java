package org.gramar.platform;

import static org.junit.Assert.*;

import org.gramar.extension.DefinedFunction;
import org.junit.Test;


public class DefinedFunctionTest {

	@Test
	public void testMatches01() {
		DefinedFunction df = new DefinedFunction("fred",2,false,null);
		if (df.matches("fred", 1)) {
			fail("Incorrect match");
		}
	}

	@Test
	public void testMatches02() {
		DefinedFunction df = new DefinedFunction("fred",2,false,null);
		if (!df.matches("fred", 2)) {
			fail("Incorrect match");
		}
	}

	@Test
	public void testMatches03() {
		DefinedFunction df = new DefinedFunction("fred",2,false,null);
		if (df.matches("fred", 3)) {
			fail("Incorrect match");
		}
	}

	@Test
	public void testMatches04() {
		DefinedFunction df = new DefinedFunction("fred",2,false,null);
		if (df.matches("biff", 2)) {
			fail("Incorrect match");
		}
	}

	@Test
	public void testMatches05() {
		DefinedFunction df = new DefinedFunction("fred",2,true,null);
		if (df.matches("fred", 1)) {
			fail("Incorrect match");
		}
	}

	@Test
	public void testMatches06() {
		DefinedFunction df = new DefinedFunction("fred",2,true,null);
		if (!df.matches("fred", 2)) {
			fail("Incorrect match");
		}
	}

	@Test
	public void testMatches07() {
		DefinedFunction df = new DefinedFunction("fred",2,true,null);
		if (!df.matches("fred", 3)) {
			fail("Incorrect match");
		}
	}

	@Test
	public void testMatches08() {
		DefinedFunction df = new DefinedFunction("fred",2,true,null);
		if (df.matches("biff", 2)) {
			fail("Incorrect match");
		}
	}

}
