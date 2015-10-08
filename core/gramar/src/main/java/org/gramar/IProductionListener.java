package org.gramar;

/**
 * An object that gets notified of events relating to the application of a Gramar production to a model.
 *  
 * @author chrisgerken
 *
 */
public interface IProductionListener {
	
	public void beginTag(IProductionEvent event);
	
	public void endTag(IProductionEvent event);

}
