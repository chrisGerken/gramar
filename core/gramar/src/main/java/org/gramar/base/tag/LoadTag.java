package org.gramar.base.tag;

import java.io.InputStream;

import org.gramar.IGramarContext;
import org.gramar.IGramarPlatform;
import org.gramar.IModel;
import org.gramar.IModelAdaptor;
import org.gramar.ITagHandler;
import org.gramar.model.XmlModel;
import org.gramar.resource.MergeStream;
import org.gramar.tag.TagHandler;
import org.gramar.util.GramarHelper;
import org.w3c.dom.Document;

public class LoadTag extends TagHandler implements ITagHandler {

	public LoadTag() {

	}

	@Override
	public void mergeTo(MergeStream stream, IGramarContext context) {

		try {

			String url = getStringAttribute("url", context);
			String urlContext = getStringAttribute("urlContext", context, "gramar");  //  One of "gramar" (default), workspace and "transform"
			if (urlContext.equalsIgnoreCase("transform")) {
				urlContext = "gramar";
			}
			String name = getStringAttribute("var", context);
			String loader = getStringAttribute("loader", context, null);
			String type = getStringAttribute("type", context, null);

			String content = null;

			if (urlContext.equalsIgnoreCase("gramar")) {
				content = context.getGramar().getTemplateSource(url);
			} else if (urlContext.equalsIgnoreCase("workspace")) {
				InputStream is = context.getFileStore().getFileByteContent(url);
				content = GramarHelper.asString(is);
			} else {
				context.error("Invalid urlContext for load tag: "+urlContext);
				return;
			}
			
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
		return "load";
	}

}
