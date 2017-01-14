package com.gerken.xaa.mpe.core;

import java.util.Comparator;

public class KeySorter implements Comparator<String> {

	public int compare(String object1, String object2) {
		if (object1.length() < object2.length()) { return  1; }
		if (object1.length() > object2.length()) { return -1; }
		return 0;
	}

}
