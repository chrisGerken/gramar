package org.gramar.model;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.gramar.context.ModelAccessTest;
import org.gramar.exception.GramarException;
import org.junit.Test;
import org.w3c.dom.Document;

public class DocumentHelperTest {

	@Test
	public void testFromTsv() throws Exception {
		InputStream is = DocumentHelper.load("/files/simple.tsv");
		Document doc = DocumentHelper.fromTsv(is);
		int rows = ModelAccess.getDefault().getRawResult(doc, "//row",	null).length;
		if (rows != 3) {
			fail("Wrong number of rows");
		}
	}

}
