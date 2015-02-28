package org.gramar;

import java.util.HashMap;
import java.util.List;

import org.gramar.ast.SourceRegion;
import org.gramar.exception.GramarException;
import org.gramar.filestore.MergeStream;


public interface ICustomTagHandler {

	public void mergeTo(MergeStream stream, IGramarContext context);
	
	public ICustomTagHandler getParent();
	
	public List<ICustomTagHandler> getChildren();

	public ICustomTagHandler glomUsing(SourceRegion region, IGramarContext context) throws GramarException;

	public void setParent(ICustomTagHandler parent);

	public void setAttributes(HashMap<String, String> attributes);

}
