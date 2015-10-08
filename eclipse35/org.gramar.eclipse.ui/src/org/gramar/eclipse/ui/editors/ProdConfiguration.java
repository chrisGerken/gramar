package org.gramar.eclipse.ui.editors;

import org.eclipse.jface.text.DefaultTextDoubleClickStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class ProdConfiguration extends SourceViewerConfiguration {
	private DefaultTextDoubleClickStrategy doubleClickStrategy;
	private ColorManager colorManager;

	public ProdConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			ProdDocumentPartitioner.REGION_COMMENT,
			ProdDocumentPartitioner.REGION_DIRECTIVE,
			ProdDocumentPartitioner.REGION_TAG,
			ProdDocumentPartitioner.REGION_CONTROL,
			ProdDocumentPartitioner.REGION_TEXT	};
	}
		
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new DefaultTextDoubleClickStrategy();
		return doubleClickStrategy;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		
		PresentationReconciler reconciler = new PresentationReconciler();
		
		ProdPresentationDamager pdd = new ProdPresentationDamager(IProdColorConstants.TEXT, IProdColorConstants.BACKGROUND);
		reconciler.setDamager(pdd, ProdDocumentPartitioner.REGION_TEXT);
		reconciler.setRepairer(pdd, ProdDocumentPartitioner.REGION_TEXT);
		
		pdd = new ProdPresentationDamager(IProdColorConstants.TAG, IProdColorConstants.BACKGROUND);
		reconciler.setDamager(pdd, ProdDocumentPartitioner.REGION_TAG);
		reconciler.setRepairer(pdd, ProdDocumentPartitioner.REGION_TAG);
		
		pdd = new ProdPresentationDamager(IProdColorConstants.COMMENT, IProdColorConstants.BACKGROUND);
		reconciler.setDamager(pdd, ProdDocumentPartitioner.REGION_COMMENT);
		reconciler.setRepairer(pdd, ProdDocumentPartitioner.REGION_COMMENT);
		
		pdd = new ProdPresentationDamager(IProdColorConstants.DIRECTIVE, IProdColorConstants.BACKGROUND);
		reconciler.setDamager(pdd, ProdDocumentPartitioner.REGION_DIRECTIVE);
		reconciler.setRepairer(pdd, ProdDocumentPartitioner.REGION_DIRECTIVE);
		
		pdd = new ProdPresentationDamager(IProdColorConstants.CONTROL, IProdColorConstants.BACKGROUND);
		reconciler.setDamager(pdd, ProdDocumentPartitioner.REGION_CONTROL);
		reconciler.setRepairer(pdd, ProdDocumentPartitioner.REGION_CONTROL);
		
		pdd = new ProdPresentationDamager(IProdColorConstants.TAG_ERROR, IProdColorConstants.BACKGROUND);
		reconciler.setDamager(pdd, ProdDocumentPartitioner.REGION_ERROR);
		reconciler.setRepairer(pdd, ProdDocumentPartitioner.REGION_ERROR);
		
		return reconciler;
	}

}