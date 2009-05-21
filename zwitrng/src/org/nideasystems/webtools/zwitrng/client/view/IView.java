package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.client.controller.IController;



import com.google.gwt.user.client.ui.Widget;


public interface IView {
	
	public void setController(IController controller);
	public void setName(String name);
	public IController getController();
	public String getName();
	public Widget getAsWidget();
	public void init();
	
	
	

}
