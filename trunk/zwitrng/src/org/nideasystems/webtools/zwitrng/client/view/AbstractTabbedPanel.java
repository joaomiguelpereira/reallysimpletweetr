package org.nideasystems.webtools.zwitrng.client.view;

import org.nideasystems.webtools.zwitrng.client.controller.IController;

import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractTabbedPanel<C extends IController<?, ?>> extends DecoratedTabPanel implements
		IView<C> {
	
	
	private C controller;
	private String name;

	@Override
	public C getController() {
		return this.controller;
	}

	@Override
	public String getName() {
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
