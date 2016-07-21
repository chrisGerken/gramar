package org.gramar.util.diagram;

import java.util.StringTokenizer;

import org.gramar.IGramar;
import org.gramar.ITagHandler;
import org.gramar.ITemplate;
import org.gramar.context.GramarContext;
import org.gramar.exception.GramarException;
import org.gramar.tag.TagDocument;
import org.gramar.tag.TagHandler;

public class Diagrammer {

	private IGramar gramar;
	private StringBuffer sb = new StringBuffer();
	
	private Diagrammer(IGramar gramar) {
		this.gramar = gramar;
	}

	public static String diagramFor(IGramar gramar) throws GramarException {
		
		Diagrammer d = new Diagrammer(gramar);
		d.createDiagram();
		return d.getDiagram();
		
	}
	
	private String getDiagram() {
		return sb.toString();
	}

	private void createDiagram() throws GramarException {

		String main = gramar.getMainProductionId();
		ITemplate template = gramar.getTemplate(main, GramarContext.dummy());
		TagDocument doc = template.getDocument();

		ProdList gramar = new ProdList(null, "gramar", "1 : 1");

		ProdList list = new ProdList(gramar, lastLevel(main),"1 : *");
		gatherItems(doc,list);
		
		gramar.add(list);
		gramar.prepareNode();
		
		sb.append("digraph G {\n");
		
		sb.append("node [shape=record]; \n");
		sb.append("graph [rankdir=LR; splines=line]; \n");

		sb.append(" \n");

		gramar.writeNode(sb);
		gramar.writeEdges(sb);
		
		sb.append("}\n");

	}

	private void gatherItems(ITagHandler root, ProdList list) {
		
		for (ITagHandler child: root.getChildren()) {
			String name = child.getTagName();
			if (name.equals("file")) {
				String path = child.getRawAttribute("path","");
				String template = child.getRawAttribute("template","");
				path = lastLevel(path);
				template = lastLevel(template);
				list.add(new ProdResource("File",path));  // +" from "+template
			} else if (name.equals("folder")) {
				String path = child.getRawAttribute("path","");
				list.add(new ProdResource("Folder",path));
			} else if (name.equals("project")) {
				String path = child.getRawAttribute("name","");
				list.add(new ProdResource("Project",path));
			} else if (name.equals("iterate")) {
				String var = child.getRawAttribute("var","?");
				String select = child.getRawAttribute("select",".");
				ProdList ilist = new ProdList(list, var, "0 : *");
				gatherItems(child,ilist);
//				list.hasNonTerminal = true;
				if (ilist.size() > 0) {
					list.add(ilist);
				}
			} else {
				gatherItems(child, list);
			}
		}
		
	}

	private String lastLevel(String buf) {
		int offset = -1;
		boolean inXpath = false;
		for (int i = 0; i < buf.length(); i++) {
			char c = buf.charAt(i);
			if (c=='{') {
				inXpath = true;
			} else if (c=='}') {
				inXpath = false;
			} else if (!inXpath&&(c=='/')) {
				offset = i;
			}
		}
		return buf.substring(offset+1);
	}

}
