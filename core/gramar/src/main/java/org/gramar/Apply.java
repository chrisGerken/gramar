package org.gramar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.gramar.exception.GramarException;
import org.gramar.model.XmlModel;
import org.gramar.platform.SimpleGramarPlatform;
import org.gramar.util.PropertiesHelper;

/**
 * Command line tool to apply a gramar to a model.  Can be subclassed to 
 * provide tools that assume specific defaults in support of specific patterns
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
		
		try {
			new Apply().apply(args);
		} catch (GramarException e) {
			System.err.println(e.toString());
		}

	}

	/**
	 * @param args
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public void apply(String[] args) throws GramarException {

		PropertiesHelper pm = new PropertiesHelper(args);
		
		defaultValues(pm);

		String gramarId;
		String modelFile;
		SimpleGramarPlatform platform;
		
		gramarId = pm.getString(PropertiesHelper.PROPERTY_GRAMAR_ID);

		platform = new SimpleGramarPlatform(pm.getProperties());
		
		if (pm.hasValue(PropertiesHelper.PROPERTY_LIST_SAMPLES)) {
			IGramar gramar = platform.getGramar(gramarId);
			List<ISampleModel> samples = gramar.getSamples();
			if (samples.isEmpty()) {
				System.out.println("Gramar "+gramarId+" has no sample models.");
				return;
			}
			System.out.println("Available sample models for "+gramarId+":");
			for (ISampleModel sample: samples) {
				System.out.println("\t["+sample.getName()+"] "+sample.getDescription());
			}
			return;
		}
		
		if (pm.hasValue(PropertiesHelper.PROPERTY_SAMPLE_NAME)) {
			IGramar gramar = platform.getGramar(gramarId);
			List<ISampleModel> samples = gramar.getSamples();
			String name = pm.getString(PropertiesHelper.PROPERTY_SAMPLE_NAME);
			for (ISampleModel sample: samples) {
				if (name.equalsIgnoreCase(sample.getName())) {
					String sampleContent = gramar.getTemplateSource(sample.getPath());
					System.out.println("\n\n"+sampleContent+"\n\n");
					return;
				}
			}
			System.out.println("Gramar "+gramarId+" has no sample model named "+name);
			return;
		}

		modelFile = pm.getString(PropertiesHelper.PROPERTY_MODEL);
		IModel model;
		try {
			model = new XmlModel(new FileInputStream(modelFile));
		} catch (FileNotFoundException e) {
			System.out.println("Model file not found: "+modelFile);
			return;
		}
		
		IGramarApplicationStatus result = platform.apply(model, gramarId);
		String msg = "Apply complete with status "+result.getStatus()+".  Model accessed "+result.getModelAccesses()+" times";  
		if (result.hadErrors()) {
			System.err.println(msg);
		} else {
			System.out.println(msg);
		}
		
	}

	/**
	 * Interogate the given properties, from both command line arguments and the 
	 * property file, and set any desired default values.  This is primarily intended
	 * to be provided by a subclass that might, for example, assume a specific gramar
	 * or even model file.
	 * 
	 * @param pm
	 */
	public void defaultValues(PropertiesHelper pm) {

		// Do nothing for this class.  Perhaps subclasses
		// might have defaults they want to set.
		
	}

}
