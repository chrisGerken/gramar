package com.gerken.xaa.adaptor;

import java.io.InputStream;

import org.eclipse.core.runtime.CoreException;

import com.gerken.xaa.model.refimpl.ExemplarProject;

public interface IExemplarAdaptor {

	String name(Object object);

	String location(Object object);

	Object[] filesIn(Object object) throws CoreException;

	Object[] foldersIn(Object object) throws CoreException;

	InputStream inputStream(Object object) throws CoreException;

	String relativePath(ExemplarProject exemplarProject, Object source);

	void close();
}
