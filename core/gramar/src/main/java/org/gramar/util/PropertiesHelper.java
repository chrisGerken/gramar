package org.gramar.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.gramar.exception.GramarException;

public class PropertiesHelper {
	
	public static final String PROPERTY_PROPERTY_FILE 	= "properties";
	public static final String PROPERTY_GRAMAR_ID 		= "gramar";
	public static final String PROPERTY_MODEL 			= "model";
	public static final String PROPERTY_LIST_SAMPLES	= "list-samples";
	public static final String PROPERTY_SAMPLE_NAME 	= "sample-name";
	
	private Properties props;
	
	public PropertiesHelper(String[] args) throws GramarException {
		load(args);
	}

	private void load(String[] args) throws GramarException {

		try {
			props = new Properties();
			
			for (String arg: args) {
				int index = arg.indexOf('=');
				String property = null;
				String value = null;
				if (index > -1) {
					property = arg.substring(0,index);
					value = arg.substring(index+1);
				} else {
					property = arg;
					value = "";
				}
				props.put(property, value);
			}
			
			if (props.contains(PROPERTY_PROPERTY_FILE)) {
				String value = (String) props.get(PROPERTY_PROPERTY_FILE);
				props.load(new FileReader(value));
			}
		} catch (FileNotFoundException e) {
			throw new GramarException("Specified property file not found", e);
		} catch (IOException e) {
			throw new GramarException("Problem reading specified property file", e);
		}
		
	}

	/**
	 * Returns the value of the given property name as specified in either the
	 * command line arguments or the specified property file.  If the property
	 * is not present then throw an exception.
	 * 
	 * Note this class's static constants for common property names for convenience.
	 * 
	 * @param propertyName
	 * @return
	 * @throws GramarException 
	 */
	public String getString(String propertyName) throws GramarException {
		String value = (String) props.getProperty(propertyName);
		if (value == null) {
			throw new GramarException("Missing property: "+propertyName);
		}
		return value;
	}

	/**
	 * Answers whether the given property name was provided in the command line
	 * arguments or in the property file
	 * 
	 * Note this class's static constants for common property names for convenience.
	 * 
	 * @param propertyName
	 * @return true/false depending on whether a value exists for the given property name
	 * @throws GramarException 
	 */
	public boolean hasValue(String propertyName) {
		String value = (String) props.getProperty(propertyName);
		return (value != null);
	}

	/**
	 * Returns the value of the given property name as specified in either the
	 * command line arguments or the specified property file.  If the property
	 * is not present then return the given defaultValue.
	 * 
	 * Note this class's static constants for common property names for convenience.
	 * 
	 * @param propertyName
	 * @return
	 */
	public String getString(String propertyName, String defaultValue) {
		return props.getProperty(propertyName, defaultValue);
	}

	/**
	 * Returns all values whose corresponding property name starts with the
	 * given prefix.  For example, a propertyNamePrefix of "a.b." would return
	 * values for properties "a.b.1", "a.b.2" and "a.b.c".
	 *  
	 * @param properties
	 * @param propertyNamePrefix
	 * @return
	 */
	public static List<String> getIndexedValues(Properties properties, String propertyNamePrefix) {
		ArrayList<String> values = new ArrayList<String>();
		for (Object key: properties.keySet()) {
			if (key instanceof String) {
				String property = (String) key;
				if (property.startsWith(propertyNamePrefix)) {
					values.add((String) properties.getProperty(property));
				}
			}
		}
		return values;
	}

	public Properties getProperties() {
		return props;
	}

	/**
	 * Set the value of a property.  If the property is already set and override
	 * is false, do not override the existing value.
	 * @param key
	 * @param value
	 */
	public void setProperty(String key, String value, boolean override) {
		if (!props.containsKey(key) || override) {
			props.setProperty(key, value);
		}
	}
	
}
