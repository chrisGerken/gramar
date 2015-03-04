package org.gramar;

import java.util.HashMap;
import java.util.List;

import org.gramar.ast.SourceRegion;
import org.gramar.exception.GramarException;
import org.gramar.filestore.MergeStream;


public interface ITagHandler {

	public void mergeTo(MergeStream stream, IGramarContext context);
	
	public ITagHandler getParent();
	
	public List<ITagHandler> getChildren();

	public ITagHandler glomUsing(SourceRegion region, IGramarContext context) throws GramarException;

	public void setParent(ITagHandler parent);

	public void setAttributes(HashMap<String, String> attributes);

	public ITagHandler parentNamed(String name);

}
