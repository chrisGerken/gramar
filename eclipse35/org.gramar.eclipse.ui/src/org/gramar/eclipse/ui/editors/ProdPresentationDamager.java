package org.gramar.eclipse.ui.editors;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class ProdPresentationDamager implements IPresentationDamager, IPresentationRepairer {

	private Color foreground;
	private Color background;
	
	public ProdPresentationDamager(RGB fore, RGB back) {
		foreground = new Color(Display.getCurrent(),fore);
		background = new Color(Display.getCurrent(),back);
	}
	
	@Override
	public void setDocument(IDocument document) {

	}

	@Override
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent event,
			boolean documentPartitioningChanged) {

		return partition;
	}

	@Override
	public void createPresentation(TextPresentation presentation, ITypedRegion damage) {

		StyleRange range = new StyleRange(damage.getOffset(), damage.getLength(), foreground, null);
		presentation.addStyleRange(range);
	}

}
