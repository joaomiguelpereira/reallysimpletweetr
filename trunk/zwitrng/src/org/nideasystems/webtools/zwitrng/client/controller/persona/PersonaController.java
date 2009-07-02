package org.nideasystems.webtools.zwitrng.client.controller.persona;

import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.controller.AbstractController;
import org.nideasystems.webtools.zwitrng.client.controller.AutoUpdatable;
import org.nideasystems.webtools.zwitrng.client.controller.twitteraccount.TwitterAccountController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.TwitterUpdatesListController;
import org.nideasystems.webtools.zwitrng.client.view.configuration.AbstractListConfigurationWidget;
import org.nideasystems.webtools.zwitrng.client.view.configuration.CampaignsConfigurationWidget;
import org.nideasystems.webtools.zwitrng.client.view.configuration.ConfigurationEditListener;
import org.nideasystems.webtools.zwitrng.client.view.configuration.SelectableItem;
import org.nideasystems.webtools.zwitrng.client.view.configuration.TemplateFragmentsConfigurationWidget;
import org.nideasystems.webtools.zwitrng.client.view.persona.PersonaView;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTO;
import org.nideasystems.webtools.zwitrng.shared.model.CampaignDTODTOList;
import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateFragmentDTOList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;

public class PersonaController extends
		AbstractController<PersonaDTO, PersonaView> implements AutoUpdatable {

	TwitterUpdatesListController twitterUpdatesListController = null;
	TwitterAccountController twitterAccountController = null;

	@Override
	public void init() {

		setView(new PersonaView());
		getView().setController(this);
		getView().init();
		// Add the Tools for the Persona

		// Create a controller for the TwitterAccount
		twitterAccountController = new TwitterAccountController();
		twitterAccountController.setModel(getModel().getTwitterAccount());
		twitterAccountController.setMainController(getMainController());
		twitterAccountController.setServiceManager(getServiceManager());

		twitterAccountController.setParentController(this);
		twitterAccountController.init();
		// Add the view of TwitterAccount to this persona
		getView().add(twitterAccountController.getView());
		// Add twitter updates list
		getView().add(new HTML("Update list"));

	}

	public TwitterUpdatesListController getTwitterUpdatesListController() {

		return this.twitterUpdatesListController;
	}

	@Override
	public void handleAction(String action, Object... args) {

	}

	public void delete() {
		assert (getParentController() instanceof PersonasListController);
		((PersonasListController) getParentController())
				.deletePersona(getModel());

	}

	@Override
	public void endProcessing() {
		getMainController().isProcessing(false);
		super.endProcessing();
	}

	@Override
	public void startProcessing() {
		getMainController().isProcessing(true);
		super.startProcessing();
	}

	@Override
	public void reload() {
		// Reload
		// initializeUpdatesController();

	}

	@Override
	public void pause() {

		if (this.twitterUpdatesListController != null) {
			this.twitterUpdatesListController.pause();
		}

	}

	@Override
	public void resume() {
		if (this.twitterUpdatesListController != null) {
			this.twitterUpdatesListController.resume();
		}

	}

	public boolean hasTwitterUpdatesListControllerInitialized() {
		return (this.twitterUpdatesListController != null);

	}

	public void initializeUpdatesListController() {
		GWT.log("Initializing UpdatesListController...", null);
		// initialize only if it's authenticated
		if (getModel().getTwitterAccount().getIsOAuthenticated()) {
			this.twitterUpdatesListController = new TwitterUpdatesListController();
			this.twitterUpdatesListController
					.setMainController(getMainController());
			this.twitterUpdatesListController.setModel(getModel()
					.getTwitterAccount());
			this.twitterUpdatesListController.setParentController(this);
			this.twitterUpdatesListController
					.setServiceManager(getServiceManager());
			this.twitterUpdatesListController.init();
			getView().add(this.twitterUpdatesListController.getView());
		}

	}

	public TwitterAccountController getTwitterAccountController() {
		return this.twitterAccountController;

	}

	public void loadTemplates(final AbstractListConfigurationWidget<TemplateDTO, TemplateDTOList> templatesConfigurationWidget) {

		try {
			getServiceManager().getRPCService().getTemplatesList(
					this.getModel().getName(),
					new AsyncCallback<TemplateDTOList>() {

						@Override
						public void onFailure(Throwable caught) {
							getMainController().addException(caught);
							templatesConfigurationWidget.onFailedLoadObjects(caught);

						}

						@Override
						public void onSuccess(TemplateDTOList result) {
							templatesConfigurationWidget.onSuccessLoadObjects(result);

						}

					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getMainController().addException(e);
			templatesConfigurationWidget.onFailedLoadObjects(e);
		}

		// TemplateDTOList list = null;

	}

	public void createTemplate(String templateText, String tags,
			final ConfigurationEditListener<TemplateDTO> callback) {

		TemplateDTO template = new TemplateDTO();
		template.setTemplateText(templateText);
		// tags

		String[] tagsArray = StringUtils.splitText(tags);
		// end tags
		for (String tag : tagsArray) {
			template.addTags(tag);
		}

		try {
			getServiceManager().getRPCService().createTemplate(this.getModel(),
					template, new AsyncCallback<TemplateDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onError(caught);

						}

						@Override
						public void onSuccess(TemplateDTO result) {
							callback.onObjectCreated(result);

						}

					});
		} catch (Exception e) {
			callback.onError(e);
			e.printStackTrace();
		}

	}

	public void removeTemplate(TemplateDTO template, final ConfigurationEditListener<TemplateDTO> callBack) {
		// callback.onSuccessDeleteTemplates(template);

		try {
			getServiceManager().getRPCService().deleteTemplate(this.getModel(),
					template, new AsyncCallback<TemplateDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							callBack.onError(caught);

						}

						@Override
						public void onSuccess(TemplateDTO result) {
							//callback.onSuccessDeleteObject(result);
							callBack.onObjectRemoved(result);

						}

					});
		} catch (Exception e) {
			callBack.onError(e);
			e.printStackTrace();
		}

	}

	public void saveTemplate(TemplateDTO template,
			final ConfigurationEditListener<TemplateDTO> callBack) {
		
		//ConfigurationEditListener
		
		try {
			getServiceManager().getRPCService().saveTemplate(getModel(),template, new AsyncCallback<TemplateDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					callBack.onError(caught);
					
				}

				@Override
				public void onSuccess(TemplateDTO result) {
					callBack.onObjectSaved(result);
					
				}
				
			});
		} catch (Exception e) {
			callBack.onError(e);		}
		
	}

	public void getTemplateFragments(
			final TemplateFragmentsConfigurationWidget templateFragmentsConfigurationWidget) {
		try {
			getServiceManager().getRPCService().getTemplateFragmentList(
					this.getModel(),
					new AsyncCallback<TemplateFragmentDTOList>() {

						@Override
						public void onFailure(Throwable caught) {
							getMainController().addException(caught);
							templateFragmentsConfigurationWidget.onError(caught);

						}

						@Override
						public void onSuccess(TemplateFragmentDTOList result) {
							templateFragmentsConfigurationWidget.onSuccessLoadObjects(result);
							
							

						}

					});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			getMainController().addException(e);
			templateFragmentsConfigurationWidget.onError(e);
		}

		
	}

	public void createTemplateFragment(
			TemplateFragmentDTO object,
			final TemplateFragmentsConfigurationWidget callback) {
		try {
			getServiceManager().getRPCService().createTemplateFragment(this.getModel(), object, new AsyncCallback<TemplateFragmentDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onError(caught);
					
				}

				@Override
				public void onSuccess(TemplateFragmentDTO result) {
					callback.onObjectCreated(result);
					
				}
				
			});
		} catch (Exception e) {
			
			callback.onError(e);
			e.printStackTrace();
		}
		
	}

	public void saveTemplateFragment(
			TemplateFragmentDTO object,
			final ConfigurationEditListener<TemplateFragmentDTO> callback) {
		try {
			getServiceManager().getRPCService().saveTemplateFragment(this.getModel(), object, new AsyncCallback<TemplateFragmentDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onError(caught);
					
				}

				@Override
				public void onSuccess(TemplateFragmentDTO result) {
					callback.onObjectSaved(result);
					
				}
				
			});
		} catch (Exception e) {
			
			callback.onError(e);
			e.printStackTrace();
		}
		
	}

	public void removeTemplateFragment(TemplateFragmentDTO dataObject,
			final SelectableItem<TemplateFragmentDTO, TemplateFragmentDTOList> callbak) {
		try {
			getServiceManager().getRPCService().deleteTemplateFragment(this.getModel(),
					dataObject, new AsyncCallback<TemplateFragmentDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							callbak.onError(caught);

						}

						@Override
						public void onSuccess(TemplateFragmentDTO result) {
							//callback.onSuccessDeleteObject(result);
							callbak.onObjectRemoved(result);

						}

					});
		} catch (Exception e) {
			callbak.onError(e);
			e.printStackTrace();
		}

		
		
	}

	public void loadTemplateFragmentsLists(List<String> lists,
			AsyncCallback<Map<String, String>> asyncCallback)  throws Exception {
		getServiceManager().getRPCService().getTemplateFragmentsLists(getModel(),lists,
				asyncCallback);
		
	}

	public void loadCampaigns(
			final AbstractListConfigurationWidget<CampaignDTO,CampaignDTODTOList> callback) {
		try {
			getServiceManager().getRPCService().getCampaigns(this.getModel(), new AsyncCallback<CampaignDTODTOList>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onError(caught);

						}

						@Override
						public void onSuccess(CampaignDTODTOList result) {
							//callback.onSuccessDeleteObject(result);
							callback.onSuccessLoadObjects(result);

						}

					});
		} catch (Exception e) {
			callback.onError(e);
			e.printStackTrace();
		}

		
	}

	public void createCampaign(CampaignDTO object,
			final AbstractListConfigurationWidget<CampaignDTO,CampaignDTODTOList> callback) {
		try {
			getServiceManager().getRPCService().createCampaign(this.getModel(), object, new AsyncCallback<CampaignDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onError(caught);
					
				}

				@Override
				public void onSuccess(CampaignDTO result) {
					callback.onObjectCreated(result);
					
				}
				
			});
		} catch (Exception e) {
			
			callback.onError(e);
			e.printStackTrace();
		}
		
		
	}

	public void saveCampaign(CampaignDTO object,
			final ConfigurationEditListener<CampaignDTO> callback) {
		try {
			getServiceManager().getRPCService().saveCampaign(this.getModel(), object, new AsyncCallback<CampaignDTO>() {

				@Override
				public void onFailure(Throwable caught) {
					callback.onError(caught);
					
				}

				@Override
				public void onSuccess(CampaignDTO result) {
					callback.onObjectSaved(result);
					
				}
				
			});
		} catch (Exception e) {
			
			callback.onError(e);
			e.printStackTrace();
		}
		
		
	}

	public void removeCampaign(CampaignDTO dataObject,
			final SelectableItem<CampaignDTO, CampaignDTODTOList> callback) {
		try {
			getServiceManager().getRPCService().deleteCampaign(this.getModel(),
					dataObject, new AsyncCallback<CampaignDTO>() {

						@Override
						public void onFailure(Throwable caught) {
							callback.onError(caught);

						}

						@Override
						public void onSuccess(CampaignDTO result) {
							//callback.onSuccessDeleteObject(result);
							callback.onObjectRemoved(result);

						}

					});
		} catch (Exception e) {
			callback.onError(e);
			e.printStackTrace();
		}

		
	}

}
