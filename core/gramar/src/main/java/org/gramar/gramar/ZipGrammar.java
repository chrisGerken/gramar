package org.gramar.gramar;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.gramar.IGramar;
import org.gramar.exception.InvalidGramarException;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.util.GramarHelper;


public abstract class ZipGrammar extends Gramar implements IGramar {
	
	/*
	 * Construct a ZipPattern using the given InputStream as the source
	 * of the ZipFile containing the templates and other pattern resources.
	 */
	public ZipGrammar() throws InvalidGramarException {
	}

	protected void readAllToCache() throws InvalidGramarException {
		try {
			ZipInputStream zis = getZipInputStream();
			ZipEntry entry = zis.getNextEntry();
			while (entry != null) {
				String name = entry.getName();
				String source = GramarHelper.asString(zis);
				storeSource(name, source);
				entry = zis.getNextEntry();
			}
			try { zis.close(); } catch (Throwable t) {  }
		} catch (Exception e) {
			throw new InvalidGramarException(e);
		}
	}

	@Override
	public String readTemplateSource(String templateName) throws NoSuchResourceException {
		try {
			ZipInputStream zis = getZipInputStream();
			ZipEntry entry = zis.getNextEntry();
			while (entry != null) {
				if (entry.getName().equals(templateName)) {
					String source = GramarHelper.asString(zis);
					return source;
				}
				entry = zis.getNextEntry();
			}
			try { zis.close(); } catch (Throwable t) {  }
		} catch (Exception e) {
			throw new NoSuchResourceException(e);
		}

		throw new NoSuchResourceException();

	}

	protected abstract ZipInputStream getZipInputStream() throws Exception;

}
