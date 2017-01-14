package com.gerken.xaa.sme;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.w3c.dom.Node;

import com.gerken.xaa.model.xform.CreateFile;
import com.gerken.xaa.model.xform.CreateProject;
import com.gerken.xaa.model.xform.Group;
import com.gerken.xaa.model.xform.Replacement;
import com.gerken.xaa.model.xform.Token;
import com.gerken.xaa.model.xform.Xform;

public class Ios4Mentor extends AbstractMentor implements IXaaMentor {

	public Ios4Mentor() {
	}

	public boolean applicable(Xform xform) {
		return false;
	}

	public String getName() {
		return "iOS4 (iPhone/iPad App)";
	}

	public IXaaMentorWizardPage getWizardPage() {
		return null;
	}

	public boolean hasWizardPage() {
		return false;
	}

	public void tweak(Xform xform) {
		
		Group group = xform.getGroupArray()[0];
		
		CreateProject cp = group.getCreateProjectArray()[0];
		String prjId = cp.getId();
			
		xform.removeFilesStartingWith(prjId,"build");

		Hashtable<String,CreateFile> ht = new Hashtable<String, CreateFile>();
		
		ArrayList<CreateFile> cfs = xform.getFilesEndingWith(prjId, ".m");
		Iterator<CreateFile> iter = cfs.iterator();
		while (iter.hasNext()) {
			CreateFile file = iter.next();
			String buffer = file.getNameExpr();
			buffer = buffer.substring(0,buffer.length()-2);
			ht.put(buffer,file);
		}
				
		cfs = xform.getFilesEndingWith(prjId, ".h");
		iter = cfs.iterator();
		while (iter.hasNext()) {
			CreateFile hfile = iter.next();
			String buffer = hfile.getNameExpr();
			buffer = buffer.substring(0,buffer.length()-2);
			CreateFile mfile = ht.get(buffer);
			if (mfile != null) {
					// we have a matching .h and .m file
				String nameExpr = derivedToken(xform,group,buffer,mfile.getPurpose());
				hfile.setNameExpr(nameExpr+"_h");
				hfile.setPurposeAsTokenName(false);
				mfile.setNameExpr(nameExpr+"_m");
				mfile.setPurposeAsTokenName(true);
			} else {
				// we have an unmatched header
				String nameExpr = derivedToken(xform,group,buffer,hfile.getPurpose());
				hfile.setNameExpr(nameExpr+"_h");
				hfile.setPurposeAsTokenName(false);
			}
			
		}
	}

	private String derivedToken(Xform xform, Group group, String fileName, String purpose) {
		String fileTokenName = purpose;
		String shortString = "{$"+group.getName()+"/@"+fileTokenName+"}";
//		String newString = xform.toJetTags(shortString);
		String newString = shortString;
		
		Token token = new Token();
		token.setName(fileTokenName);
		token.setDerived(true);
		token.setDesc("Token representing the filename for "+purpose);
		token.setFormula(newString);
		group.addToken(token);
		
		Replacement replacement = new Replacement();
		replacement.setOldString(fileName);
		replacement.setNewString(newString);
		xform.addReplacement(replacement);

		return shortString;
	}

}
