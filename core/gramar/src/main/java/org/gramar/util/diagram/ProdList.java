package org.gramar.util.diagram;

import java.util.ArrayList;

import org.gramar.base.function.EscapeJsonFunction;

public class ProdList implements Prod {

	private String boxLabel;
	private String lineLabel;
	private String label;
	private String label_i;
	private boolean singles;
	private ProdList parent;
	private boolean terminal = false;
	public boolean hasNonTerminal = false;
	
	private ArrayList<Prod> items = new ArrayList<Prod>();
	
	private static long nextList = 0L;
	
	public ProdList(ProdList parent, String boxLabel, String lineLabel) {
		this.boxLabel = boxLabel;
		this.lineLabel = lineLabel;
		this.parent = parent;
		label = "List"+getNextList();
		label_i = label + "i";
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
				ProdList pl = (ProdList) p;
				pl.prepareNode();
			} else {
				resources.add(p);
			}
		}
		
		hasNonTerminal = !loops.isEmpty();
		
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
				ProdList rlist = new ProdList(this, ".","1 : 1");
				rlist.items = resources;
				rlist.singles = true;
				rlist.terminal = true;
				items.add(rlist);
			}
		}
	}
	
	public void writeNode(StringBuffer sb) {

		if (isSingles()) {

			sb.append(label+" [ label=\" ");
			String delim = "";
			int index = 0;
			for (Prod p: items) {
				String field = "<I"+index+">";
				sb.append(delim+field + p.getBoxLabel() );
				delim = "|";
				index++;
			}

			sb.append("\"; fillcolor=lightsteelblue; style=filled  ];  \n");
			
		} else {

			sb.append(label_i+" [ shape=circle; width=0.15; height=0.15; label=\"+\" ] \n");

			for (Prod p: items) {
				ProdList pl = (ProdList) p;
				sb.append(pl.label+"v [ label=\" "+pl.getBoxLabel() + "\"; fillcolor=orchid1; style=filled  ];  \n" );
			}
			
		}
		
		for (Prod p: items) {
			if (p.isList()) {
				((ProdList)p).writeNode(sb);
			}
		}
	}
	
	public void writeEdges(StringBuffer sb) {

		if (!isSingles()) {
			sb.append("\""+label+"v\"  ->   \""+label_i+"\"  [ label=\"*\"; color=darkslategrey; penwidth=3 ] ; \n");
		}

		int index = 0;
		for (Prod p: items) {
			if (p.isList()) {
				ProdList ilist = (ProdList) p;
				if (ilist.terminal) {
					sb.append("\""+label_i+"\"  ->   \""+ilist.getLabel()+"\"  [ label=\""+ProdResource.escape(ilist.getLineLabel())+"\"; color=darkslategrey; penwidth=3 ] ; \n");
				} else {
					sb.append("\""+label_i+"\"  ->   \""+ilist.getLabel()+"v\"  [ label=\""+ProdResource.escape(ilist.getLineLabel())+"\"; color=darkslategrey; penwidth=3 ] ; \n");
					if (hasNonTerminal) {
						sb.append("\""+ilist.getLabel()+"v\"  ->   \""+ilist.getLabel()+"\"  [ label=\""+ProdResource.escape(ilist.getLineLabel())+"\"; color=darkslategrey; penwidth=3 ] ; \n");
					}
				}
				
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

	public ProdList getParent() {
		return parent;
	}

	public void setParent(ProdList parent) {
		this.parent = parent;
	}

}
