package com.gerken.xaa.mpe.domain;

	// Begin imports

import org.w3c.dom.Node;
import java.util.ArrayList;

import com.gerken.xaa.mpe.core.ModelAccess;
import com.gerken.xaa.mpe.editor.XaaEditor;

	// End imports

public class PolymorphicKeysEnumeration extends AbstractEnumeration {
	
	public PolymorphicKeysEnumeration(XaaEditor editor) {
		super(editor);
	} 
	
	public void addConstants(ArrayList<String> keys, ArrayList<String> values) {

		Node _node;
	// Begin build domain logic

		_node = getTarget();
		if (_node != null) {
			String id = ModelAccess.getAttribute(_node,"@parentId");
			Node node[] = ModelAccess.getNodes(_node,"../group[@id=\""+id+"\"]/token");
			for (int i = 0; i < node.length; i++) {
				keys.add(ModelAccess.getAttribute(node[i],"@name"));
				values.add(ModelAccess.getAttribute(node[i],"@name"));
			}
		}


	// End build domain logic
	}
	
	protected String keyExpression() {
		return null;
	}
	
	protected String valueExpression() {
		return null;
	}
	
// Begin custom logic


// End custom logic
	
}
