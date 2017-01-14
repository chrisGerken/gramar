package com.gerken.xaa.mpe.editor;

import com.gerken.xaa.mpe.core.AbstractSection;
import com.gerken.xaa.mpe.core.AbstractTreeSection;
import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.ModelAccess;

import java.util.Vector;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class GroupTreeTokenSection extends AbstractGroupTreeSection {

    public GroupTreeTokenSection(AbstractFormPage page, Composite parent) {
        super(page, parent);
    }

    public void notifyDependents(Node node, Node selected) {
        String name = node.getNodeName();
        AbstractSection section;
        if (name.equals("group")) {
//			((TokenPage)getPage()).getTokenListSection().loadFrom(node);
            section = ((TokenPage) getPage()).getTokenListSection();
            section.loadFrom(node);
            if (section.navigates()) {
                section.setSelection(selected);
            }

        } else {
            if (getPage().isDirty()) {
            	((TokenPage) getPage()).getTokenListSection().clear();
            }
        }
    }

    public void clearDependents() {
        // dependents for group
        if (getPage().isDirty()) {
            ((TokenPage) getPage()).getTokenListSection().clear();
        }
    }

    public void setDependentSelection(Node node, Node selected) {
        String name = node.getNodeName();
        AbstractSection section;
        if (name.equals("group")) {

            section = ((TokenPage) getPage()).getTokenListSection();
            if (section.navigates()) {
                section.setSelection(selected);
            } else {
                section.setSelection(node);
            }

        } else {
            if (getPage().isDirty()) {
                ((TokenPage) getPage()).getTokenListSection().clear();
            }
        }
    }

}
