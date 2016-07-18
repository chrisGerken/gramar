package org.gramar.util.diagram;

import java.util.ArrayList;

import org.gramar.base.function.EscapeJsonFunction;

public class ProdList implements Prod {

	private String boxLabel;
	private String lineLabel;
	private String label;
	private boolean singles;
	
	private ArrayList<Prod> items = new ArrayList<Prod>();
	
	private static long nextList = 0L;
	
	public ProdList(String boxLabel, String lineLabel) {
		this.boxLabel = boxLabel;
		this.lineLabel = lineLabel;
		label = "List"+getNextList();
	}

	public void add(Prod item) {
		items.add(item);
	}
	
	public void prepareNode() {
		ArrayList<Prod> resources = new ArrayList<Prod>();
		ArrayList<Prod> loops = new ArrayList<Prod>();
		
		for (Prod p: items) {
			if (p.isList()) {
				loops.add(p);
				((ProdList)p).prepareNode();
			} else {
				resources.add(p);
			}
		}
		
		if (resources.isEmpty()) {
			if (loops.isEmpty()) {
				// do nothing;
			} else {
				singles = false;
			}
		} else {
			if (loops.isEmpty()) {
				singles = true;
			} else {
				singles = false;
				items = loops;
				ProdList rlist = new ProdList(".","1 : 1");
				rlist.items = resources;
				rlist.singles = true;
				items.add(rlist);
			}
		}
	}
	
	public void writeNode(StringBuffer sb) {

		sb.append(label+" [ label=\" ");
		String delim = "";
		int index = 0;
		for (Prod p: items) {
			String field = "<I"+index+">";
			sb.append(delim+field + p.getBoxLabel() );
			delim = "|";
			index++;
		}
		String color = "orchid1";
		if (isSingles()) {
			color = "lightsteelblue";
		}
		sb.append("\"; fillcolor="+color+"; style=filled  ];  \n");

		for (Prod p: items) {
			if (p.isList()) {
				((ProdList)p).writeNode(sb);
			}
		}
	}
	
	public void writeEdges(StringBuffer sb) {

		int index = 0;
		for (Prod p: items) {
			if (p.isList()) {
				ProdList ilist = (ProdList) p;
				String field = "<I"+index+">";
				sb.append("\""+label+"\":"+field+"  ->   \""+ilist.getLabel()+"\":<I0>  [ label=\""+ProdResource.escape(ilist.getLineLabel())+"\"; color=darkslategrey; penwidth=3 ] ; \n");
			}
			index++;
		}
		
		for (Prod p: items) {
			if (p.isList()) {
				ProdList ilist = (ProdList) p;
				ilist.writeEdges(sb);
			}
		}
	}

	@Override
	public String getBoxLabel() {
		return boxLabel;
	}
	
	public String getLabel() {
		return label;
	}
	
	private static long getNextList() {
		long result = nextList;
		nextList++;
		return result;
	}

	@Override
	public boolean isList() {
		return true;
	}

	public int size() {
		return items.size();
	}

	public boolean isSingles() {
		return singles;
	}

	public void setSingles(boolean singles) {
		this.singles = singles;
	}

	public String getLineLabel() {
		return lineLabel;
	}

}
