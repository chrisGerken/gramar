package com.gerken.xaa.refactor.popup.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.MultiStateTextFileChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextEditChangeGroup;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;

public class XaaTemplateRefactoring extends Refactoring {

	private IFile[] file;
	private Hashtable<String,String> replacements;
	private String[] sortedKey;
	
	public XaaTemplateRefactoring(IFile file, Hashtable<String, String> replacements) {
		this.file = new IFile[1];
		this.file[0] = file;
		this.replacements = replacements;
		sort();
	}
	
	public XaaTemplateRefactoring(IFile[] file, Hashtable<String, String> replacements) {
		this.file = file;
		this.replacements = replacements;
		sort();
	}

	public RefactoringStatus checkFinalConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		return new RefactoringStatus();
	}

	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
			throws CoreException, OperationCanceledException {
		return new RefactoringStatus();
	}

	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		CompositeChange change = new CompositeChange("Replace Tokens");
		TextFileChange textFileChange;
		MultiStateTextFileChange mstfc;
		TextEdit multiTextEdit;
		
		int editnum = 0;
		
		for (int f = 0; f < file.length; f++) {

			String content = getContent(file[f]);
			ArrayList<ReplaceEdit> edits = sliceAndDice(content,0);
			ArrayList<String> names = new ArrayList<String>();
			Iterator<ReplaceEdit> iter = edits.iterator();
			while (iter.hasNext()) {
				ReplaceEdit replaceEdit = iter.next();
				String key = content.substring(replaceEdit.getOffset(),replaceEdit.getOffset()+replaceEdit.getLength());
				String name = "Replace '"+key+"' ("+editnum+")";
				names.add(name);
				editnum++;
			}
			
			if (!edits.isEmpty()) {
				textFileChange = new TextFileChange("Replace tokens in "+file[f].getName(),file[f]);
				multiTextEdit = new MultiTextEdit();
				textFileChange.setEdit(multiTextEdit);
				iter = edits.iterator();
				Iterator<String> desc = names.iterator();

				while (iter.hasNext()) {
					TextEdit textEdit = iter.next();
					multiTextEdit.addChild(textEdit);
					TextEditGroup textEditGroup = new TextEditGroup(desc.next());
					textEditGroup.addTextEdit(textEdit);
					TextEditChangeGroup group = new TextEditChangeGroup(textFileChange,textEditGroup);
					textFileChange.addChangeGroup(group);
				}
				change.add(textFileChange);
			}
		}

		return change;
	}

	private ArrayList<ReplaceEdit> sliceAndDice(String content, int offset) {
		ArrayList<ReplaceEdit> result = new ArrayList<ReplaceEdit>();
		for (int s = 0; s < sortedKey.length; s++) {
			int index = content.indexOf(sortedKey[s]);
			if (index > -1) {
				String key = sortedKey[s];
				String value = replacements.get(key);
				ReplaceEdit replaceEdit = new ReplaceEdit(index+offset,key.length(),value);
				if (index > 0) {
					String leftSide = content.substring(0,index);
					result.addAll(sliceAndDice(leftSide,offset));
				}
				result.add(replaceEdit);
				if ((index+key.length()) < content.length()) {
					String rightSide = content.substring(index+key.length());
					result.addAll(sliceAndDice(rightSide,offset+index+key.length()));
				}
				return result;
			}
		}
		return result;
	}

	private String getContent(IFile aFile) throws CoreException {
		ITextFileBufferManager manager= FileBuffers.getTextFileBufferManager();
		IPath path= aFile.getFullPath();
		manager.connect(path, LocationKind.IFILE, null);
		ITextFileBuffer fBuffer= manager.getTextFileBuffer(path, LocationKind.IFILE);
		IDocument doc= fBuffer.getDocument();
		String content = doc.get();
		manager.disconnect(aFile.getFullPath(), LocationKind.IFILE, null);
		return content;
	}

	public String getName() {
		return "Token replacement";
	}

	private void sort() {
		sortedKey = new String[replacements.size()];
		int s = 0;
		for (Enumeration<String> mune = replacements.keys(); mune.hasMoreElements(); ) {
			sortedKey[s] = mune.nextElement();
			s++;
		}
		Arrays.sort(sortedKey,new KeySorter());
	}
	
	private class KeySorter implements Comparator<String> {
		public int compare(String object1, String object2) {
			if (object1.length() < object2.length()) { return  1; }
			if (object1.length() > object2.length()) { return -1; }
			return 0;
		}
	}
}
