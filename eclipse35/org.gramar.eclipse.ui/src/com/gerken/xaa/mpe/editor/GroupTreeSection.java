package com.gerken.xaa.mpe.editor;

import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Node;

import com.gerken.xaa.mpe.core.AbstractFormPage;
import com.gerken.xaa.mpe.core.AbstractSection;

public class GroupTreeSection extends AbstractGroupTreeSection {

    public GroupTreeSection(AbstractFormPage page, Composite parent) {
        super(page, parent);
        // TODO Auto-generated constructor stub
    }

    public void notifyDependents(Node node, Node selected) {
        String name = node.getNodeName();
        AbstractSection section;
        if (name.equals("group")) {
//			((GroupPage)getPage()).getGroupDetailsSection().loadFrom(node);
            section = ((GroupPage) getPage()).getGroupDetailsSection();
            section.loadFrom(node);
            if (section.navigates()) {
                section.setSelection(selected);
            }

//			((GroupPage)getPage()).getGroupTextSection().loadFrom(node);
            section = ((GroupPage) getPage()).getGroupTextSection();
            section.loadFrom(node);
            if (section.navigates()) {
                section.setSelection(selected);
            }

        } else {
            if (getPage().isDirty()) {
                ((GroupPage) getPage()).getGroupDetailsSection().clear();
                ((GroupPage) getPage()).getGroupTextSection().clear();
            }
        }
    }

    public int getHeight() {
        return 300;
    }

    public void clearDependents() {
        // dependents for group
        if (getPage().isDirty()) {
            ((GroupPage) getPage()).getGroupDetailsSection().clear();
            ((GroupPage) getPage()).getGroupTextSection().clear();
        }
    }

    public void setDependentSelection(Node node, Node selected) {
        String name = node.getNodeName();
        AbstractSection section;
        if (name.equals("group")) {

            section = ((GroupPage) getPage()).getGroupDetailsSection();
            if (section.navigates()) {
                section.setSelection(selected);
            } else {
                section.setSelection(node);
            }

            section = ((GroupPage) getPage()).getGroupTextSection();
            if (section.navigates()) {
                section.setSelection(selected);
            } else {
                section.setSelection(node);
            }

        } else {
            if (getPage().isDirty()) {
                ((GroupPage) getPage()).getGroupDetailsSection().clear();
                ((GroupPage) getPage()).getGroupTextSection().clear();
            }
        }
    }

}
