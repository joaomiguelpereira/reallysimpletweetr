package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.client.controller.IController;


import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractVerticalPanelView<C extends IController> extends VerticalPanel implements IView<C>{
	private C controller;
	private String name;
	
	
	
	@Override
	public C getController() {
		// TODO Auto-generated method stub
		return this.controller;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public void setController(C controller) {
		this.controller = controller;
		
	}

	@Override
	public void setName(String name) {
		this.name = name;
		
	}

	@Override
	public Widget getAsWidget() {
		return this;
	}

}
