package org.gramar.filestore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.gramar.IFileStore;
import org.gramar.exception.NoSuchResourceException;
import org.gramar.util.GramarHelper;

/**
 * An IFileStore implementation in which all committed resources as stored in a single zip file
 * 
 * @author chrisgerken
 *
 */
public class ZipFileStore extends FileStore implements IFileStore {

	private ByteArrayOutputStream baos;
	private ZipOutputStream zos;
	
	public ZipFileStore() {

	}

	@Override
	public Reader getFileContent(String path) throws NoSuchResourceException {
		return null;
	}

	@Override
	public void setFileContent(String path, Reader reader) throws NoSuchResourceException, IOException {
		zos.putNextEntry(new ZipEntry(path));
		GramarHelper.copy(reader, zos);
		zos.closeEntry();
	}

	@Override
	public boolean resourceExists(String path) {
		return false;
	}

	@Override
	public void createProject(String projectName, String altPath) throws IOException {
		zos.putNextEntry(new ZipEntry(projectName+"/"));
		zos.closeEntry();
	}

	@Override
	public void createFolder(String pathName) throws NoSuchResourceException, IOException {
		String name = pathName;
		if (!name.endsWith("/")) {
			name = name + "/";
		}
		zos.putNextEntry(new ZipEntry(name));
		zos.closeEntry();
	}

	@Override
	public void reset() {
		baos = new ByteArrayOutputStream();
		zos = new ZipOutputStream(baos);
	}
	
	public boolean writeZipFile(String absolutePath) {
		if (zos == null) { return false; }
		try {
			FileOutputStream fos = new FileOutputStream(absolutePath);
			zos.close();
			GramarHelper.copy(new ByteArrayInputStream(baos.toByteArray()), fos);
			fos.close();
			return true;
		} catch (Exception e) {

		}
		return false;
	}

}
