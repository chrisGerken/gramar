package org.gramar.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public class GramarHelper {
	
	public static void copy(InputStream is, OutputStream os) throws IOException {
		byte b[] = new byte[32000];
		int len = is.read(b);
		while (len > -1) {
			os.write(b, 0, len);
			len = is.read(b);
		}
	}
	
	public static String asString(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4000);
		copy(is,baos);
		return baos.toString();
	}

	public static void copy(Reader reader, Writer writer) throws IOException {
		char buf[] = new char[4000];
		int len = reader.read(buf);
		while (len > -1) {
			writer.write(buf, 0, len);
			len = reader.read(buf);
		}
	}
	
	public static String asString(Reader reader) throws IOException {
		StringWriter writer = new StringWriter();
		copy(reader,writer);
		return writer.toString();
	}

	public static void copy(Reader reader, OutputStream os) throws IOException {
		StringWriter writer = new StringWriter();
		copy(reader,writer);
		os.write(writer.toString().getBytes());
	}

	public static Reader fromResource(String path) throws IOException {
		InputStream stream = GramarHelper.class.getResourceAsStream(path);
		if (stream == null) { 
			stream = new ByteArrayInputStream(new byte[0]); 
		}
		return new InputStreamReader(stream);
	}

}
