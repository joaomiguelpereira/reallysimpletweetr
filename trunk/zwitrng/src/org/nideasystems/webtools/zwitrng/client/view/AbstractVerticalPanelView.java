package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.client.controller.IController;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractVerticalPanelView extends VerticalPanel implements IView{
	private IController controller;
	private String name;
	
	
	@Override
	public IController getController() {
		// TODO Auto-generated method stub
		return this.controller;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public void setController(IController controller) {
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
