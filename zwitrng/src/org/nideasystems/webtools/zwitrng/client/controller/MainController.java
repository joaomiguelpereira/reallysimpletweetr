package org.nideasystems.webtools.zwitrng.client.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonasListController;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.services.BasicServiceManager;
import org.nideasystems.webtools.zwitrng.client.services.IServiceManager;
import org.nideasystems.webtools.zwitrng.client.view.persona.DefaultHomeView;
import org.nideasystems.webtools.zwitrng.client.view.twitteraccount.SelectSendingAccountWindow;

import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;

public class MainController implements IMainController {
	private Panel mainPanel = RootPanel.get("main");
	private static MainController instance = null;
	final private Image waitingImg = new Image(Constants.WAITING_IMAGE);
	private IServiceManager serviceManager = new BasicServiceManager();
	private Map<String, PopupManager> popupManagers = new HashMap<String, PopupManager>();
	PersonasListController personasListController = null;
	private DefaultHomeView homeView;
	private Map<String, HorizontalPanel> personaTabTitles = new HashMap<String, HorizontalPanel>();

	private MainController() {

	}

	static {
		publishJsMethods();
	}
	

	public static MainController getInstance() {
		if (instance == null) {
			instance = new MainController();
		}
		return instance;

	}

	public PersonaController getCurrentPersonaController() {
		return personasListController.getCurrentPersonaController();
	}

	public PopupManager getPopupManager() {

		PopupManager popup = popupManagers.get(personasListController
				.getCurrentPersonaController().getModel().getName());
		if (popup == null) {
			String currentPersonaController = personasListController
					.getCurrentPersonaController().getModel().getName();
			popup = new PopupManager(personasListController
					.getCurrentPersonaController());
			popupManagers.put(currentPersonaController, popup);

		}
		return popup;
	}

	private void loadPersonas() throws Exception {
		isProcessing(true);
		serviceManager.getRPCService().loadPersonas(
				new AsyncCallback<PersonaDTOList>() {

					@Override
					public void onFailure(Throwable caught) {
						isProcessing(false);
						addException(caught);

					}

					@Override
					public void onSuccess(PersonaDTOList result) {
						isProcessing(false);
						if (personasListController == null) {

							// personasListController = new
							// PersonasListController();

							personasListController = new PersonasListController();
							personasListController
									.setMainController(MainController
											.getInstance());
							personasListController.setModel(result);
							personasListController
									.setServiceManager(serviceManager);
							personasListController.init();
							mainPanel.add(personasListController.getView());

						}

					}

				});

	}

	public void init() {
		// This will be the default error handle for this controller
		// Load personas from Service;
		// Add processing image
		waitingImg.setVisible(false);
		mainPanel.add(waitingImg);
		try {

			loadPersonas();
		} catch (Exception e) {
			addException(e);
			isProcessing(false);
		}

		

	}
	public native static void publishJsMethods() /*-{
	var nextTweetId = 0;
	String.prototype.parseURL = function() {
		
		return this.replace(/[A-Za-z]+:\/\/[A-Za-z0-9-_]+\.[A-Za-z0-9-_:%&\?\/.=]+/g, function(url) {
			newText = "<a href=\""+ url + "\" target=\"_blank\">" + url + "</a>";
			return newText;
			
		});
	};

	String.prototype.parseUsername = function() {
		return this.replace(/[@]+[A-Za-z0-9-_]+/g, function(u) {
			var username = u.replace("@","")
			
			tweetId = "tweet_id_"+(++nextTweetId);
			
			newtext = "<a id=\"" + tweetId
						+ "\" href=\"javascript:showUserPanel('"
						+ username + "','" + tweetId + "')\">@"
						+ username + "</a>";
			
			return newtext;
		});
	};

	String.prototype.parseHashtag = function() {
		return this.replace(/[#]+[A-Za-z0-9-_]+/g, function(t) {
			var tag = t.replace("#","");
			newText = "<a href=\"javascript:processHashTag('#" + tag + "')\">#"+ tag + "</a>";
			return newText;
		});
	};
}-*/;

public native static String jsParseText(String text) /*-{
	return text.parseURL().parseHashtag().parseUsername();
}-*/;

	@Override
	public void addError(String errorMsg) {
		Window.alert(errorMsg);

	}

	@Override
	public void addException(Throwable tr) {
		Window.alert(tr.getLocalizedMessage());

	}
	
	@Override
	public void addInfoMessage(String string) {
		Window.alert(string);
		
	}


	@Override
	public void isProcessing(boolean isProcessing) {
		waitingImg.setVisible(isProcessing);
	}

	@Override
	public List<TwitterAccountDTO> getAllTwitterAccounts(
			SelectSendingAccountWindow selectSendingAccountWindow) {
		// get all personas
		List<TwitterAccountDTO> retList = new ArrayList<TwitterAccountDTO>();
		for (PersonaDTO persona : personasListController.getModel()
				.getPersonaList()) {
			retList.add(persona.getTwitterAccount());
		}
		return retList;

	}

	@Override
	public TwitterAccountController getTwitterAccountController(
			String userScreenName) {

		return this.personasListController
				.getTwitterAccountController(userScreenName);

	}

	public void setHomeView(DefaultHomeView homeView) {
		this.homeView = homeView;
		
	}

	public DefaultHomeView getHomeView() {
		return this.homeView;
	}

	public void addPersonaTabTitle(String name, HorizontalPanel tabTitle) {

		personaTabTitles.put(name, tabTitle);
		
		
	}

	public void updateTabTitle(PersonaDTO personaDto) {
		HorizontalPanel panel = personaTabTitles.get(personaDto.getName());
		
		if ( panel!= null) {
			panel.clear();
			Image img = new Image(personaDto.getTwitterAccount().getTwitterImageUrl());
			img.setWidth("24px");
			img.setHeight("24px");
			img.setTitle(personaDto.getTwitterAccount().getTwitterName());
			panel.add(img);
		}
		
	}


	/*
	 * @Override public void reload() { // TODO Auto-generated method stub
	 * 
	 * }
	 */

}
