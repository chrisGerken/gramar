package org.gramar;

import java.io.IOException;

import org.gramar.filestore.MergeStream;
import org.gramar.tag.TagDocument;



public interface ITemplate {

	public TagDocument getDocument();

	public void mergeTo(MergeStream stream, IGramarContext context) throws IOException;
	
}
