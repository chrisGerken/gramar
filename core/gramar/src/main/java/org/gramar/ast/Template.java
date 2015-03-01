package org.gramar.ast;

import java.io.IOException;

import org.gramar.IGramarContext;
import org.gramar.ITemplate;
import org.gramar.filestore.MergeStream;
import org.gramar.tag.TagDocument;


public class Template implements ITemplate {

	private TagDocument document;
	
	public Template(TagDocument document) {
		this.document = document;
	}

	@Override
	public TagDocument getDocument() {
		return document;
	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) throws IOException {
		document.mergeTo(stream, context);
	}

}
