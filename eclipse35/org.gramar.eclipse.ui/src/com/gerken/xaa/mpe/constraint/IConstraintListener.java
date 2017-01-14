package com.gerken.xaa.mpe.constraint;

import java.util.ArrayList;

public interface IConstraintListener {

	public void constraintsChecked(ArrayList<ConstraintFailure> problems);
	
}
