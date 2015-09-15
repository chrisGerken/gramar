package org.gramar.filestore;

import java.io.IOException;

import org.gramar.IGramarContext;
import org.gramar.context.GramarContext;
import org.gramar.exception.GramarException;
import org.gramar.resource.MergeStream;
import org.gramar.resource.UpdateFile;
import org.junit.Test;

public class FileStoreTest {

	@Test
	public void testSameBytes() throws GramarException, IOException {
		MemoryFileStore store = new MemoryFileStore();
		String path = "prj/folder/file.txt";
		String textContent1 = "abc\ndef\nghi";
		String textContent2 = "abc\ndef\nghij";
		
		IGramarContext context = GramarContext.dummy();
		
		MergeStream stream = new MergeStream();
		stream.append(textContent1);
		UpdateFile uf = new UpdateFile(path, stream, true);		
		store.addUpdate(uf);
		
		stream = new MergeStream();
		stream.append(textContent2);
		uf = new UpdateFile(path, stream, true);		
		store.addUpdate(uf);
		
		store.commit("first", context);
		
	}

}
