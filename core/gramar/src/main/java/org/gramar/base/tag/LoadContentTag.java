package org.gramar.base.tag;

import org.gramar.IGramarContext;
import org.gramar.IGramarPlatform;
import org.gramar.IModelAdaptor;
import org.gramar.ITagHandler;
import org.gramar.model.XmlModel;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.w3c.dom.Document;

public class LoadContentTag extends TagHandler implements ITagHandler {

	public LoadContentTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {
			String name = getStringAttribute("var", context);
			String loader = getStringAttribute("loader", context, null);
			String type = getStringAttribute("type", context, null);

			MergeStream newStream = processChildren(context);
			String content = newStream.toString();
			
			IModelAdaptor adaptor = null;
			IGramarPlatform platform = context.getPlatform();
			if (loader != null) {
				try { adaptor = platform.getModelAdaptor(loader); } catch (Throwable t) { }
			}
			if ((adaptor == null) && (type != null)) {
				try { adaptor = platform.getModelAdaptorFor(type); } catch (Throwable t) { }
			}
			
			Document doc = null;
			if (adaptor != null) {
				doc = adaptor.asDocument(content);
			} else {
				doc = new XmlModel(content).asDOM();
			}
			
			context.setVariable(name, doc);
			
		} catch (Exception e) {
			context.error(e);
		}

	}

	@Override
	public String getTagName() {
		return "loadContent";
	}

}
