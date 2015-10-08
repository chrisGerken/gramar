package org.gramar;

import org.gramar.ast.SourceRegion;
import org.gramar.tag.TagHandler;

/**
 * Describes all relevent information about an event in the application of a Gramar production to a model
 * 
 * @author chrisgerken
 *
 */
public interface IProductionEvent {

	public static final int EVENT_START_TAG = 1;
	public static final int EVENT_END_TAG = 2;
	public static final int EVENT_RESOLVE_ATTR = 3;
	public static final int EVENT_BEFORE_CONTENT = 4;
	public static final int EVENT_AFTER_CONTENT = 5;
	
	/**
	 * Answer the gramar-relative path of the current template
	 */
	public String getProductionID();
	
	/**
	 * Answer the source region of the production that's being processed
	 * @return
	 */
	public SourceRegion getCurrentSourceRegion();
	
	/**
	 * Answer the custom tag handler responsible for processing the current source region
	 */
	public TagHandler getTagHandler();
	
	/**
	 * Answers one of the event types
	 * @return
	 */
	public int getEventType();
}
