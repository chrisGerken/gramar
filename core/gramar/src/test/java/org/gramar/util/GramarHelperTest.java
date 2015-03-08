package org.gramar.util;

import static org.junit.Assert.*;

import org.gramar.util.GramarHelper;
import org.junit.Test;

public class GramarHelperTest {

	@Test
	public void testPathSegments() {
		String segment[] = GramarHelper.pathSegments("/project/dir1/dir2/file.txt");
		if (segment.length != 4) {
			fail("Wrong number of segments");
		}
	}

}
