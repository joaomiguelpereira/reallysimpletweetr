package org.nideasystems.webtools.zwitrng.client.controller;

import org.nideasystems.webtools.zwitrng.client.view.IView;
import org.nideasystems.webtools.zwitrng.shared.model.IModel;

public interface HasParent<C extends IController<?, ?>> {

	public void setParentController(C parentController);
	public C getParent();
}
