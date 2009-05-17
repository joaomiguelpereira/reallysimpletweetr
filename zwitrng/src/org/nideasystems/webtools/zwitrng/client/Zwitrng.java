package org.nideasystems.webtools.zwitrng.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Zwitrng implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		
		WindowManager.getInstance().createTabbedPannel();
		//Get from the server all personas registered for the logged user
		PersonaService.getInstance().loadPersonas();
		
		
		
	}
}
