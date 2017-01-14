package com.gerken.xaa.adaptor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;

import com.gerken.xaa.model.refimpl.ExemplarProject;

public class FileSystemAdaptor implements IExemplarAdaptor {

	public FileSystemAdaptor() {
	}

	public Object[] filesIn(Object object) throws CoreException {
		return entriesIn(object,false);
	}

	public Object[] foldersIn(Object object) throws CoreException {
		return entriesIn(object,true);
	}

	private Object[] entriesIn(Object object, boolean isDirectory) {
		ArrayList<File> entries = new ArrayList<File>();
		if (object instanceof File) {
			File cur = (File) object;
			String entry[] = cur.list();
			if (entry == null) { entry = new String[0]; }
			for (int index = 0; index < entry.length; index++) {
				File candidate = new File(cur,entry[index]);
				if (candidate.isDirectory() == isDirectory) {
					entries.add(candidate);
				}
			}
		}
		Object result[] = new Object[entries.size()];
		entries.toArray(result);
		return result;
	}

	public InputStream inputStream(Object object) throws CoreException {
		try {
			File file = (File) object;
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return new ByteArrayInputStream(new byte[0]);
		}
	}

	public String name(Object object) {
		File file = (File)object;
		String sourcePath = file.getAbsolutePath();
		sourcePath = sourcePath.replace('\\','/');
		int index = sourcePath.lastIndexOf('/');
		if (index < 0) { return sourcePath; }
		return sourcePath.substring(index+1);
	}

	public String relativePath(ExemplarProject exemplarProject, Object source) {
		File root = (File)(exemplarProject.getSource());
		String rootPath = root.getAbsolutePath();
		File file = (File)source;
		String sourcePath = file.getAbsolutePath();
		return sourcePath.substring(rootPath.length()+1);
	}
	
	public File fileAt(String absolutePath) {
		return new File(absolutePath);
	}

	public String location(Object object) {
		File source = (File)object;
		return source.getAbsolutePath();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
