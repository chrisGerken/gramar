package com.gerken.xaa.sme;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.w3c.dom.Node;

import com.gerken.xaa.model.xform.CreateFile;
import com.gerken.xaa.model.xform.CreateProject;
import com.gerken.xaa.model.xform.Group;
import com.gerken.xaa.model.xform.JavaPkg;
import com.gerken.xaa.model.xform.Replacement;
import com.gerken.xaa.model.xform.Token;
import com.gerken.xaa.model.xform.Xform;

public class JavaMentor extends AbstractMentor implements IXaaMentor {

	private ArrayList<String> javaProjectIds = null;
	
	public JavaMentor() {
	}

	public boolean applicable(Xform xform) {
		javaProjectIds = new ArrayList<String>();
		Iterator<CreateProject> projects = xform.projectsWithNature("org.eclipse.jdt.core.javanature").iterator();
		while (projects.hasNext()) {
			javaProjectIds.add(projects.next().getId());
		}
		return !javaProjectIds.isEmpty();
	}

	public void tweak(Xform xform) {
		
		Group group = xform.getGroupArray()[0];
		
		Iterator<String> ids = javaProjectIds.iterator();
		while (ids.hasNext()) {
			
			String prjId = ids.next();
			
			ArrayList<CreateFile> cf = xform.getFilesEndingWith(prjId,".class");
			Iterator<CreateFile> iter = cf.iterator();
			while (iter.hasNext()) {
				CreateFile file = iter.next();
				file.remove();
//				xform.getRootGroup().addCreateFile(file);
			}
			
			CreateProject cp = xform.getProject(prjId);
			Node classpath = xform.getFileRootElement(prjId,".classpath");
			
			String binDir = retrieveString(classpath,"/classpath/classpathentry[@kind='output']/@path");
			cf = xform.getFilesStartingWith(cp.getId(),binDir);
			iter = cf.iterator();
			while (iter.hasNext()) {
				CreateFile file = iter.next();
				file.remove();
//				xform.getRootGroup().addCreateFile(file);
			}

			Node src[] = retrieveNodes(classpath,"/classpath/classpathentry[@kind='src']");
			for (int s = 0; s < src.length; s++) {
				Hashtable<String,ArrayList<CreateFile>> ht = new Hashtable<String, ArrayList<CreateFile>>();
				ArrayList<CreateFile> files;
				String srcFolder = retrieveString(src[s],"@path");
				cf = xform.getFilesStartingWith(cp.getId(),srcFolder);
				iter = cf.iterator();
				while (iter.hasNext()) {
					CreateFile file = iter.next();
					String dirOffset = file.getOPath().substring(srcFolder.length()+1);
					int offset = dirOffset.lastIndexOf('/');
					String pkg = "";
					if (offset > 0) {
						dirOffset = dirOffset.substring(0,offset);
						pkg = dirOffset.replace('/','.');
						pkg = pkg.replace('\\','.');
					}
					files = ht.get(pkg);
					if (files == null) {
						files = new ArrayList<CreateFile>();
						ht.put(pkg,files);
					}
					files.add(file);
				}
				String pkg[] = new String[ht.size()];
				String name[] = new String[ht.size()];
				String orig[] = new String[ht.size()];
				int p = 0;
				for (Enumeration<String> mune = ht.keys(); mune.hasMoreElements(); ) {
					String buffer = mune.nextElement();
					orig[p] = buffer;
					int index = buffer.lastIndexOf('.');
					if (index == -1) {
						pkg[p] = "";
						name[p] = buffer;
					} else {
						pkg[p] = buffer.substring(0,index);
						name[p] = buffer.substring(index+1);
					}
					p++;
				}
				boolean matches = true;
				while (matches) {
					matches = false;
					for (p = 0; p < pkg.length; p++) {
						for (int c = 0; c < pkg.length; c++) {
							if ((p != c) && (name[p].equals(name[c]))) {
								matches = true;
								change(pkg,name,p);
								change(pkg,name,c);
							}
						}
					}
				}
				for (p = 0; p < pkg.length; p++) {
					String buffer = name[p] + "." + srcFolder + "." + cp.getPurpose();
					buffer = buffer.replace('/','.');
					buffer = buffer.replace('\\','.');
					String varroot = camelizePackage(buffer); 
										
					ArrayList<CreateFile> cfs = ht.get(orig[p]);
					Iterator<CreateFile> cfIter = cfs.iterator();
					while (cfIter.hasNext()) {
						CreateFile file = cfIter.next();
						JavaPkg jp = new JavaPkg();
						jp.setName(orig[p]);
						jp.setPurpose(varroot);
						file.addJavaPkg(jp);
					}
					
				}
				
			}

		}
		
	}

	private void change(String[] pkg, String[] name, int index) {
		String buffer = pkg[index];
		int offset = buffer.lastIndexOf('.');
		if (offset == -1) {
			if (buffer.length() > 0) {
				pkg[index] = "";
				name[index] = buffer + "." + name[index];
			}
		} else {
			pkg[index] = buffer.substring(0,offset);
			name[index] = buffer.substring(offset+1) + "." + name[index];
		}
	}

	public String getName() {
		return "Java";
	}

	public boolean hasWizardPage() {
		return false;
	}

	public IXaaMentorWizardPage getWizardPage() {
		return null;
	}

}
