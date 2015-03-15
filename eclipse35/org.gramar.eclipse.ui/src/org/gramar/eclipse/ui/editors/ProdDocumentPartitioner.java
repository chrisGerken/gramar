package org.gramar.eclipse.ui.editors;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TypedRegion;
import org.gramar.IGramarContext;
import org.gramar.ast.Parser;
import org.gramar.ast.SourceRegion;
import org.gramar.context.GramarContext;
import org.gramar.eclipse.platform.EclipsePlatform;
import org.gramar.eclipse.platform.EclipseWorkspaceGramar;
import org.gramar.exception.GramarException;
import org.gramar.gramar.TagLibSpec;
import org.gramar.model.XmlModel;

public class ProdDocumentPartitioner implements IDocumentPartitioner {

	private IProject				project;
	private IDocument				document;
	
	private IGramarContext			context;
	private Parser					parser;
	private SourceRegion 			sourceRegion[];
	private ITypedRegion			typedRegion[];
	
	public static final String REGION_TEXT		= "TEXT";
	public static final String REGION_TAG		= "TAG";
	public static final String REGION_DIRECTIVE	= "DIRECTIVE";
	public static final String REGION_COMMENT	= "COMMENT";
	
	public ProdDocumentPartitioner(IProject project) {
		this.project = project;
	}

	@Override
	public void connect(IDocument document) {
		this.document = document;
		context = new GramarContext(new EclipsePlatform(), XmlModel.emptyModel().asDOM());
		try {
			EclipseWorkspaceGramar gramar = new EclipseWorkspaceGramar(project);
			ArrayList<TagLibSpec> taglibs = gramar.getTaglibs();
			for (TagLibSpec spec: taglibs) {
				context.declareTemplatingExtension(spec.getNamespace(), spec.getExtensionId());
			}
		} catch (GramarException e) {
			// Continue parsing with no extensions defined;
		}
		parser = new Parser(context);
	}

	@Override
	public void disconnect() {
		document = null;
		context = null;
		parser = null;
	}

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {

	}

	@Override
	public boolean documentChanged(DocumentEvent event) {
		sourceRegion = null;
		typedRegion = null;
		return true;
	}

	@Override
	public String[] getLegalContentTypes() {
		String legal[] = new String[4];
		legal[0] = REGION_TEXT;
		legal[1] = REGION_TAG;
		legal[2] = REGION_COMMENT;
		legal[3] = REGION_DIRECTIVE;
		return legal;
	}

	@Override
	public String getContentType(int offset) {
		return getPartition(offset).getType();
	}

	@Override
	public ITypedRegion[] computePartitioning(int offset, int length) {
		return getRegions();
	}

	@Override
	public ITypedRegion getPartition(int offset) {
		for (ITypedRegion region: getRegions()) {
			if ((region.getOffset()<=offset) && (region.getOffset()+region.getLength()>=offset)) {
				return region;
			}
		}
		return null;
	}
	
	private void parseDocument() {
		String source = document.get();
		sourceRegion = parser.parse(source);
	}
	
	private ITypedRegion[] getRegions() {
		try {
			if (sourceRegion == null) {
				parseDocument();
			}
			if (typedRegion == null) {
				typedRegion = new ITypedRegion[sourceRegion.length];
				for (int i = 0; i < sourceRegion.length; i++) {
					typedRegion[i] = typedRegionFrom(sourceRegion[i]);
				}
			}
		} catch (Exception e) {
			typedRegion = new ITypedRegion[1];
			typedRegion[0] = new TypedRegion(0, document.getLength(), REGION_TEXT);
		}
		return typedRegion;
	}

	private ITypedRegion typedRegionFrom(SourceRegion region) {
		String type = REGION_TEXT;
		if (region.isComment()) {
			type = REGION_COMMENT;
		}
		if (region.isTag()) {
			type = REGION_TAG;
		}
		if (region.isEndTag()) {
			type = REGION_TAG;
		}
		if (region.isEmptyTag()) {
			type = REGION_TAG;
		}
		if (region.isDirective()) {
			type = REGION_DIRECTIVE;
		}
		
		TypedRegion result = new TypedRegion(region.getStart(), region.getEnd()-region.getStart()+1, type);
		return result;
	}

}
