package org.nideasystems.webtools.zwitrng.client.controller.search;


import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class SearchController extends AbstractController{

	//This search controller has a Model PersonaDTO
	private PersonaDTO persona = null;

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getDataLoadedHandler(Object result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getToolActionHandler(String string) {
		Window.alert("SearchController ActionEvent Handler"+string);
		
	}

	@Override
	public AsyncCallback<String> getDataRemovedCallBack() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPersona(PersonaDTO persona) {
		this.persona = persona;
	}

	public PersonaDTO getPersona() {
		return persona;
	}
	
	
	
	
}
