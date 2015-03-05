package org.gramar.platform;

import static org.junit.Assert.fail;

import org.gramar.IFileStore;
import org.gramar.IGramarPlatform;
import org.gramar.IModel;
import org.gramar.exception.GramarException;
import org.gramar.filestore.ConsoleFileStore;
import org.gramar.filestore.MemoryFileStore;
import org.gramar.filestore.ZipFileStore;
import org.gramar.model.DocumentHelper;
import org.junit.Test;


public class SimplePatternPlatformTest {

	@Test
	public void test01() throws Exception {
		
		try {
			
			IGramarPlatform platform = new SimpleGramarPlatform();
			IFileStore store = new ConsoleFileStore();
			IModel model = DocumentHelper.modelFromResource("/models/simple01.xml");
			
			platform.apply(model, "com.fredco.alpha.pattern", store);
			
		} catch (GramarException e) {
			fail(e.toString());
		}
		
	}

	@Test
	public void test02() throws Exception {
		
		try {
			
			IGramarPlatform platform = new SimpleGramarPlatform();
			MemoryFileStore store = new MemoryFileStore();
			IModel model = DocumentHelper.modelFromResource("/models/simple01.xml");
			
			platform.apply(model, "com.fredco.alpha.pattern", store);
			
		} catch (GramarException e) {
			fail(e.toString());
		}
		
	}

}
