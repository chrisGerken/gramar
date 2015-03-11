package org.gramar.base.tag;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.gramar.IGramarContext;
import org.gramar.ITagHandler;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Node;

public class DumpTag extends TagHandler implements ITagHandler {

	public DumpTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			Node node = getNodeAttribute("select", context);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			DOMSource source = new DOMSource(node);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
			stream.append(writer.toString());
		} catch (Exception e) {
			context.error(e);
		}
		
	}

	@Override
	public String getTagName() {
		return "dump";
	}

}
