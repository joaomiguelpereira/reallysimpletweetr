package org.nideasystems.webtools.zwitrng.client.controller.persona;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.IController;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;

import com.google.gwt.event.logical.shared.SelectionHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class PersonaController extends AbstractController {

	private PersonaView personaView = null;
	private PersonaDTO persona = null;
	
	@Override
	public void init() {
		//Let's create the persona View
		personaView = new PersonaView(persona);
		//The the controller for the view
		personaView.setController(this);
		//Initialize the view
		personaView.init();
		//Now, tell the controller what view to use
		this.view = personaView;
		
	}
	
	/**
	 * Set the Persona DTO
	 * @param persona
	 */
	public void setPersona(PersonaDTO persona) {
		this.persona = persona;
	}

	/**
	 * Get the Persona DTO
	 * @return
	 */
	public PersonaDTO getPersona() {
		return persona;
	}

	@Override
	public SelectionHandler<Integer> getSelectionHandler() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handleDataLoaded(Object result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleAction(String action, Object...args) {
		if (action.equals("DELETE")) {
			try {
				
				getParentController().handleAction(IController.IActions.DELETE, this.persona.getName());
				//getServiceManager().getRPCService().deletePersona(this.persona.getName(), getParentController().getDataRemovedCallBack());
			} catch (Exception e) {
				
				getErrorHandler().addException(e);
			}
		}
			
	}

	
	@Override
	public void endProcessing() {
		getErrorHandler().isProcessing(false);
		super.endProcessing();
	}

	@Override
	public void startProcessing() {
		getErrorHandler().isProcessing(true);
		super.startProcessing();
	}
	

	

}
