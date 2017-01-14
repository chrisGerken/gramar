package com.gerken.xaa.mpe.constraint;

import java.util.StringTokenizer;

import org.w3c.dom.Node;

import com.gerken.xaa.mpe.core.ModelAccess;

public class Constraint {

	public static int CONSTRAINT_REQUIRED = 1;
	public static int CONSTRAINT_NUMERIC = 2;
	public static int CONSTRAINT_NAME = 3;
	public static int CONSTRAINT_UNIQUE = 4;
	public static int CONSTRAINT_INTEGER = 5;
	public static int CONSTRAINT_JAVA_PACKAGE = 6;
	
	public static int CONSTRAINT_REQUIRED_IF_DERIVED = 10;
	public static int CONSTRAINT_CONTAINS_XPATH = 11;
	public static int CONSTRAINT_REQUIRED_IF_PROJECT = 12;
	public static int CONSTRAINT_REQUIRED_IF_NOT_PROJECT = 13;
	public static int CONSTRAINT_REQUIRED_IF_FILE = 14;
	public static int CONSTRAINT_LEADING_SLASH_REQUIRED = 15;
	public static int CONSTRAINT_REQUIRED_IF_POLYMORPHIC_SINGLETON = 16;
	
	private String 	field = null;
	private int		type;
	
	private ConstraintSet owner;
	
	public Constraint(String field, int type) {
		this.field = field;
		this.type = type;
	}
	
	public Constraint(int type) {
		this.type = type;
	}
	
	public void check(Node target, ConstraintContext context) {
		try {
			if (type == CONSTRAINT_REQUIRED) {
				String value = getStringValue(target);
				if (value == null) { value = ""; }
				if (value.trim().length() == 0) {
					getOwner().addFailure(ConstraintFailure.error(target,"Field "+getField()+" must be entered."));
				}
				return;
			} 
			if (type == CONSTRAINT_NUMERIC) {
				String value = getStringValue(target);
				assertIsNumeric(target,value);
				return;
			} 
			if (type == CONSTRAINT_NAME) {
				String value = getStringValue(target);
				assertIsName(target,value);
				return;
			} 
			if (type == CONSTRAINT_UNIQUE) {
				String value = getStringValue(target);
				assertIsUnique(target,value);
			} 
			if (type == CONSTRAINT_INTEGER) {
				String value = getStringValue(target);
				assertIsNumeric(target,value);
				return;
			} 
			if (type == CONSTRAINT_JAVA_PACKAGE) {
				String value = getStringValue(target);
				if (value == null) { value = ""; }
				if (value.indexOf("..") > -1) {
					getOwner().addFailure(ConstraintFailure.error(target,"Value for "+getField()+" must be a valid Java package name"));
					return;
				}
				StringTokenizer st = new StringTokenizer(value,".");
				while (st.hasMoreTokens()) {
					String level = st.nextToken();
					for (int i=0; i < level.length(); i++) {
						char c = level.charAt(i);
						if (i == 0) {
							if (!Character.isJavaIdentifierStart(c)) {
								getOwner().addFailure(ConstraintFailure.error(target,"Value for "+getField()+" must be a valid Java package name"));
								return;
							}
						} else {
							if (!Character.isJavaIdentifierPart(c)) {
								getOwner().addFailure(ConstraintFailure.error(target,"Value for "+getField()+" must be a valid Java package name"));
								return;
							}
						}
					}
				}
			}
			if (type == CONSTRAINT_REQUIRED_IF_DERIVED) {
				String value = getStringValue(target);
				boolean derived = getBooleanValue(target, "derived");
				if (derived) {
					if (value.trim().length() == 0) {
						getOwner().addFailure(ConstraintFailure.error(target,"Formula is required for derived tokens"));
						return;
					}
				}
				return;
			} 
			if (type == CONSTRAINT_CONTAINS_XPATH) {
				String value = getStringValue(target);
				boolean lookingForRightBrace = true;
				
				StringTokenizer st = new StringTokenizer(value, "{}", true);
				while (st.hasMoreTokens()) {
					String token = st.nextToken();
					if (token.equals("{")) {
						if (lookingForRightBrace) {
							lookingForRightBrace = false;
						} else {
							getOwner().addFailure(ConstraintFailure.error(target,"Missing '{'"));
							return;
						}
					} else if (token.equals("}")) {
						if (!lookingForRightBrace) {
							lookingForRightBrace = true;
						} else {
							getOwner().addFailure(ConstraintFailure.error(target,"Missing '}'"));
							return;
						}
					}
				}
				return;
			} 
			if (type == CONSTRAINT_REQUIRED_IF_PROJECT) {
				String value = getStringValue(target);
				if (value == null) { value = ""; }
				if ((target.getNodeName().equals("createProject")) && (value.trim().length() == 0)) {
					getOwner().addFailure(ConstraintFailure.error(target,"Field "+getField()+" must be entered."));
				}
				return;
			} 
			if (type == CONSTRAINT_REQUIRED_IF_NOT_PROJECT) {
				String value = getStringValue(target);
				if (value == null) { value = ""; }
				if ((!target.getNodeName().equals("createProject")) && (value.trim().length() == 0)) {
					getOwner().addFailure(ConstraintFailure.error(target,"Field "+getField()+" must be entered."));
				}
				return;
			} 
			if (type == CONSTRAINT_REQUIRED_IF_FILE) {
				String value = getStringValue(target);
				if (value == null) { value = ""; }
				if ((target.getNodeName().equals("createFile")) && (value.trim().length() == 0)) {
					getOwner().addFailure(ConstraintFailure.error(target,"Field "+getField()+" must be entered."));
				}
				return;
			} 
			if (type == CONSTRAINT_LEADING_SLASH_REQUIRED) {
				String value = getStringValue(target);
				if (value == null) { value = ""; }
				if (value.length() > 1) {
					if (value.charAt(0) != '/') {
						getOwner().addFailure(ConstraintFailure.error(target,"Field "+getField()+" must start with a forward slash ('/')."));
					}
				}
				return;
			} 
			if (type == CONSTRAINT_REQUIRED_IF_POLYMORPHIC_SINGLETON) {
				String value = getStringValue(target);
				if (value == null) { value = ""; }
				if ("true".equalsIgnoreCase(ModelAccess.getAttribute(target, "@polymorphicSingleton")) && (value.trim().length() == 0)) {
					getOwner().addFailure(ConstraintFailure.error(target,"Field "+getField()+" must be entered. when 'Polymorphic Singleton' is checked"));
				}
				return;
			} 
			
		} catch (Throwable t) {
			getOwner().addFailure(ConstraintFailure.error(target,"Error occurred during check "+type+": "+t));
			return;
		} 
	}

	private void assertIsNumeric(Node target, String value) {
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (!Character.isDigit(c)) {
				getOwner().addFailure(ConstraintFailure.error(target,"Field "+getField()+" not numeric (char "+(i+1)+" '"+c+"'"));
				return;
			}
		}
	}

	private void assertIsUnique(Node target, String value) {
		String nodeName = target.getNodeName();
		if (value == null) { value = ""; }
		String expr = "../"+nodeName+"[@"+field+"='"+value+"']";
		Node sib[] = ModelAccess.getNodes(target,expr);
		if (sib.length > 1) {
			getOwner().addFailure(ConstraintFailure.error(target,"Value for "+getField()+" is not unique"));
			return;
		}
	}

	private void assertIsName(Node target,String value) {
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (Character.isWhitespace(c)) {
				getOwner().addFailure(ConstraintFailure.error(target,"Value for "+getField()+" can not contain whitespace"));
				return;
			}
		}
	}

	private String getStringValue(Node target) {
		return ModelAccess.getAttribute(target,"@"+getField());
	}

	private String getStringValue(Node target, String field) {
		return ModelAccess.getAttribute(target,"@"+field);
	}

	private boolean getBooleanValue(Node target, String field) {
		return "true".equalsIgnoreCase(ModelAccess.getAttribute(target,"@"+field));
	}

	public ConstraintSet getOwner() {
		return owner;
	}

	public void setOwner(ConstraintSet owner) {
		this.owner = owner;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

		// Begin custom methods

		// End custom methods

}
