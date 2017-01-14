package org.gramar.eclipse.ui.editors;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.gramar.eclipse.ui.Activator;

public class ProdDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		try {
			IDocument document = super.createDocument(element);
			if (document != null) {
				IFileEditorInput input = (IFileEditorInput) element;
				IProject project = input.getFile().getProject();
				IDocumentPartitioner partitioner =
					new ProdDocumentPartitioner(project);
				partitioner.connect(document);
				document.setDocumentPartitioner(partitioner);
			}
			return document;
		} catch (CoreException e) {
			Activator.getDefault().logError(e);
			throw e;
		}
	}
}