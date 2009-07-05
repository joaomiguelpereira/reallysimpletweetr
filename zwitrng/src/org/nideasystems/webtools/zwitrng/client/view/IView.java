package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.shared.model.IDTO;



import com.google.gwt.user.client.ui.Widget;


public interface IView<C extends IController> {
	
	public void setController(C controller);
	public C getController();
	public void setName(String name);
	
	public String getName();
	public Widget getAsWidget();
	public void init();
	public void isUpdating(boolean isUpdating);
	
	
	

}
