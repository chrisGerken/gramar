package org.gramar.platform;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Util {

	public static String asString(InputStream is) throws IOException {
		byte[] b = new byte[4000];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		copy(is,baos);
		is.close();
		return baos.toString();
	}

	public static void copy(InputStream is, OutputStream os) throws IOException {
		byte[] b = new byte[4000];
		int len = is.read(b);
		while (len > -1) {
			os.write(b);
			len = is.read(b);
		}
	}

	public static String fromResource(String path) throws IOException {
		return asString(Util.class.getResourceAsStream(path));
	}
}
