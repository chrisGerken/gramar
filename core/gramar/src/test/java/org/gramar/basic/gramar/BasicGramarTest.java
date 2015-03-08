package org.gramar.basic.gramar;

import static org.junit.Assert.fail;

import org.gramar.IFileStore;
import org.gramar.IGramarPlatform;
import org.gramar.IModel;
import org.gramar.exception.GramarException;
import org.gramar.filestore.ConsoleFileStore;
import org.gramar.model.DocumentHelper;
import org.gramar.platform.SimpleGramarPlatform;
import org.junit.Test;


public class BasicGramarTest {

	@Test
	public void test01() throws Exception {
		
		try {
			
			IGramarPlatform platform = new SimpleGramarPlatform();
			IFileStore store = new ConsoleFileStore();
			IModel model = DocumentHelper.modelFromResource("/models/gramar01.xml");
			
			platform.apply(model, "org.gramar.basic.gramar", store);
			
		} catch (GramarException e) {
			fail(e.toString());
		}
		
	}

}
