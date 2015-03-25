package org.gramar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.gramar.exception.GramarException;
import org.gramar.model.XmlModel;
import org.gramar.platform.SimpleGramarPlatform;
import org.gramar.util.PropertiesHelper;

/**
 * Command line tool to apply a gramar to a model.
 * 
 * @author chrisgerken
 *
 */
public class Apply {

	public Apply() {

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		PropertiesHelper pm = null;
		String gramarId;
		String modelFile;
		SimpleGramarPlatform platform;
		
		try {
			
			pm = new PropertiesHelper(args);

			gramarId = pm.getString(PropertiesHelper.PROPERTY_GRAMAR_ID);
			modelFile = pm.getString(PropertiesHelper.PROPERTY_MODEL);
			IModel model = new XmlModel(new FileInputStream(modelFile));

			platform = new SimpleGramarPlatform(pm.getProperties());
			IGramarApplicationStatus result = platform.apply(model, gramarId);
			String msg = "Apply complete with status "+result.getStatus()+".  Model accessed "+result.getModelAccesses()+" times";  
			if (result.hadErrors()) {
				System.err.println(msg);
			} else {
				System.out.println(msg);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return;
		}
		
	}

}
