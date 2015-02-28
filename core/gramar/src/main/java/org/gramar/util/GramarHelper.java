package org.gramar.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

}
