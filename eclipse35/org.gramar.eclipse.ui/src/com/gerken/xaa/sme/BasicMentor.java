package com.gerken.xaa.sme;

import java.util.Hashtable;

import com.gerken.xaa.model.xform.CreateFile;
import com.gerken.xaa.model.xform.CreateFolder;
import com.gerken.xaa.model.xform.CreateProject;
import com.gerken.xaa.model.xform.Group;
import com.gerken.xaa.model.xform.Replacement;
import com.gerken.xaa.model.xform.Token;
import com.gerken.xaa.model.xform.Xform;

public class BasicMentor extends AbstractMentor implements IXaaMentor {

	private BasicMentorWizardPage wizardPage;
	
	public BasicMentor() {
	}

	public boolean applicable(Xform xform) {
		return true;
	}

	public void tweak(Xform xform) {
		Hashtable<String,String> ht = new Hashtable<String, String>();
		
		String projectName[] = wizardPage.getProjectName();
		String purpose[] = wizardPage.getPurpose();
		String groupName = wizardPage.getGroupName();
		String projectVarName[] = new String[projectName.length];
		
		Group group  = xform.getGroupArray()[0];
		group.setName(groupName);
		
		Token token = new Token();
		token.setName("name");
		token.setDerived(false);
		token.setDesc("Name of the artifact collection to be generated");
		token.setFormula("");
		xform.getGroupArray()[0].addToken(token);
		
		for (int p = 0; p < purpose.length; p++) {
			projectVarName[p] = purpose[p] + "Project";
			
			token = new Token();
			token.setName(projectVarName[p]);
			token.setDerived(true);
			token.setDesc("Token representing the new generated name for project "+projectName[p]);
//			token.setFormula(xform.toJetTags("{$"+groupName+"/@name}"+purpose[p]));
			token.setFormula("{$"+groupName+"/@name}"+purpose[p]);
			xform.getGroupArray()[0].addToken(token);
			
			String oldString = projectName[p];
			String shortString = "{$"+groupName+"/@"+projectVarName[p]+"}";
//			String newString = xform.toJetTags(shortString);
			String newString = shortString;
			Replacement replacement = new Replacement();
			replacement.setOldString(oldString);
			replacement.setNewString(newString);
			xform.addReplacement(replacement);
			
			ht.put(oldString,shortString);
			
		}
		
		CreateProject[] cp = group.getCreateProjectArray();
		for (int i = 0; i < cp.length; i++) {
			String expr = cp[i].getProjectExpr();
			expr = ht.get(expr);
			cp[i].setProjectExpr(expr);
			cp[i].setPurpose(purpose[i]);
			if (cp[i].getOLocation().trim().length() > 0) {

				String loc = cp[i].getOLocation();
				String name = cp[i].getOPath();
				if (loc.endsWith(name)) {
					String pname = cp[i].getOPath();
					String locBase = loc.substring(0,loc.length()-pname.length());

					String locVar = cp[i].getPurpose() + "ProjectLocation";
					token = new Token();
					token.setName(locVar);
					token.setDerived(true);
					token.setDesc("Token representing the location of project "+projectName);
//					token.setFormula(xform.toJetTags(locBase+"{$"+groupName+"/@"+projectVarName[i]+"}"));
					token.setFormula(locBase+"{$"+groupName+"/@"+projectVarName[i]+"}");
					xform.getGroupArray()[0].addToken(token);
					
					String oldString = loc;
					String shortString = "{$"+groupName+"/@"+locVar+"}";
//					String newString = xform.toJetTags(shortString);
					String newString = shortString;
					Replacement replacement = new Replacement();
					replacement.setOldString(oldString);
					replacement.setNewString(newString);
					xform.addReplacement(replacement);
					
					cp[i].setLocationExpr(locBase + shortString);
				}
				
			}
		}
		
		CreateFile[] cf = group.getCreateFileArray();
		for (int i = 0; i < cf.length; i++) {
			String expr = cf[i].getProjectExpr();
			expr = ht.get(expr);
			cf[i].setProjectExpr(expr);
			if (isBinary(cf[i].getNameExpr())) {
				cf[i].setBinary(true);
			}
			if (cf[i].getNameExpr().startsWith(".")) {
				cf[i].setChangeableName(false);
			}
			cf[i].setPurpose(camelizePackage(cf[i].getNameExpr()));
		}
		
		CreateFolder[] cfo = group.getCreateFolderArray();
		for (int i = 0; i < cfo.length; i++) {
			String expr = cfo[i].getProjectExpr();
			expr = ht.get(expr);
			cfo[i].setProjectExpr(expr);
			cfo[i].setPurpose(cfo[i].getOPath());
		}
	}

	private boolean isBinary(String nameExpr) {
		if (nameExpr.endsWith(".jar")) { return true; }
		if (nameExpr.endsWith(".ico")) { return true; }
		if (nameExpr.endsWith(".jpg")) { return true; }
		if (nameExpr.endsWith(".jpeg")) { return true; }
		if (nameExpr.endsWith(".gif")) { return true; }
		if (nameExpr.endsWith(".zip")) { return true; }
		if (nameExpr.endsWith(".pdf")) { return true; }
		return false;
	}

	public String getName() {
		return "Basic Eclipse";
	}

	public boolean hasWizardPage() {
		return true;
	}

	public IXaaMentorWizardPage getWizardPage() {
		if (wizardPage == null) {
			wizardPage = new BasicMentorWizardPage("BasicMentorPage",this);
		}
		return wizardPage;
	}

}
