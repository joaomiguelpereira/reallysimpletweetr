package org.nideasystems.webtools.zwitrng.client;

import org.nideasystems.webtools.zwitrng.client.controller.MainController;


import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Zwitrng implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		//Controller creates the tabbedPannel
		//PersonasCompositeController.getInstance().init();
		
		MainController.getInstance().init();
		
		//WindowManager.getInstance().createTabbedPannel();
		//Get from the server all personas registered for the logged user
		//PersonaService.getInstance().loadPersonas();
		
		
		
		
	}
}
