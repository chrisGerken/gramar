package org.gramar.filestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.gramar.util.GramarHelper;


public class MergeStream {

	private ByteArrayOutputStream stream = new ByteArrayOutputStream(2000);
	
	public MergeStream() {

	}
	
	public void append(String content) throws IOException {
		stream.write(content.getBytes());
	}

	public InputStream asInputStream() {
		return new ByteArrayInputStream(stream.toByteArray());
	}
	
	public String toString() {
		try {
			return GramarHelper.asString(asInputStream());
		} catch (IOException e) {
			return e.toString();
		}
	}
}
