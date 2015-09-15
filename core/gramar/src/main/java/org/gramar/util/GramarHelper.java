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
import java.util.StringTokenizer;

public class GramarHelper {
	
	public static void copy(InputStream is, OutputStream os) throws IOException {
		byte b[] = new byte[32000];
		int len = is.read(b);
		while (len > -1) {
			os.write(b, 0, len);
			len = is.read(b);
		}
	}
	
	public static InputStream asStream(Reader reader) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(4000);
		copy(reader, baos);
		return new ByteArrayInputStream(baos.toByteArray());
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
	
	public static String[] pathSegments(String path) {
		StringTokenizer st = new StringTokenizer(path, "/\\");
		String segment[] = new String[st.countTokens()];
		for (int s = 0; s < segment.length; s++) {
			segment[s] = st.nextToken();
		}
		return segment;
	}

	/**
	 * Return the contents of an InputStream as a byte arrary
	 * @param stream an InputStream
	 * @return the content from InputStream stream as a byte array
	 * @throws IOException 
	 */
	public static byte[] getBytes(InputStream stream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(stream,baos);
		return baos.toByteArray();
	}

}
