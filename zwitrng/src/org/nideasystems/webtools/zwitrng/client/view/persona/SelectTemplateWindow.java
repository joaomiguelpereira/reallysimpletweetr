package org.nideasystems.webtools.zwitrng.client.view.persona;

import java.util.Date;
import java.util.List;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.persona.PersonaController;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SelectTemplateWindow extends PopupPanel implements TemplateList {

	private Image waitingImage = new Image(Constants.WAITING_IMAGE);
	private PersonaController controller = null;
	private List<TemplateDTO> templates;
	private VerticalPanel contentPanel = null;
	private SendUpdateWidget updateWidget;
	private SelectableTemplate activeSelectableTemplateWidget;
	private TemplateList templateList = null;
	private Timer timer = null;
	private TextBox searchValue = null;

	long lastUpTime = 0;

	public SelectableTemplate getActiveTemplate() {
		return activeSelectableTemplateWidget;
	}

	public SelectTemplateWindow(PersonaController pController) {
		this.setController(pController);
	}

	public void setController(PersonaController controller) {
		this.controller = controller;
	}

	public PersonaController getController() {
		return controller;
	}

	private void startTimer() {
		if (timer == null) {
			timer = new Timer() {

				@Override
				public void run() {
					// Window.alert("ok");
					filter();
				}

			};
		}
		timer.cancel();
		timer.schedule(500);

	}

	private void filter() {
		// Get all widgets
		int widgetCount = contentPanel.getWidgetCount();
		int[] visibleIndexes = new int[widgetCount];

		for (int i = 0; i < widgetCount; i++) {
			SelectableTemplate template = (SelectableTemplate) contentPanel
					.getWidget(i);
			String lookupValue = searchValue.getValue().toLowerCase();
			String tagText = template.getTemplate().getTagsAsText().toLowerCase(); 
			if (!template.getTemplate().getTemplateText().toLowerCase().contains(lookupValue)
					&& !tagText.contains(lookupValue)) {
				visibleIndexes[i] = 0; // dont show this
			} else {
				visibleIndexes[i] = 1;// show this
			}
		}

		// remove them	
		for (int i = 0; i < widgetCount; i++) {
			SelectableTemplate template = (SelectableTemplate) contentPanel.getWidget(i);
			template.setVisible(visibleIndexes[i]==1);
		}

	}

	public void init() {
		this.setAnimationEnabled(true);
		this.setAutoHideEnabled(true);
		templateList = this;
		VerticalPanel mainPanel = new VerticalPanel();

		HorizontalPanel searchPanel = new HorizontalPanel();
		searchPanel.add(new InlineHTML("Search: "));
		searchValue = new TextBox();
		searchPanel.add(searchValue);

		searchValue.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				lastUpTime = new Date().getTime();
				startTimer();

			}

		});

		mainPanel.add(searchPanel);
		mainPanel.add(new HTML("Select one template:"));

		contentPanel = new VerticalPanel();
		ScrollPanel scrollPannel = new ScrollPanel(contentPanel);
		scrollPannel.setHeight("200px");
		mainPanel.add(scrollPannel);

		waitingImage.setVisible(false);
		mainPanel.add(waitingImage);

		mainPanel.setWidth("510px");

		HorizontalPanel toolsPanel = new HorizontalPanel();
		toolsPanel.setSpacing(5);

		HTML closeLink = new InlineHTML("Close");
		closeLink.addStyleName("link");

		closeLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide(true);

			}

		});
		final HTML newLink = new InlineHTML("New");
		newLink.addStyleName("link");
		newLink.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				MainController.getInstance().getPopupManager()
						.showCreateTemplateWindow(controller, templateList,
								newLink.getAbsoluteLeft(),
								newLink.getAbsoluteTop() - 200);

			}

		});
		toolsPanel.add(closeLink);
		toolsPanel.add(newLink);

		mainPanel.add(toolsPanel);

		this.add(mainPanel);

	}

	public void setProcessing(boolean b) {
		this.waitingImage.setVisible(b);
	}

	public void loadTemplates() {
		this.setProcessing(true);
		controller.loadTemplates(this);
	}

	


	

	

	private class SelectableTemplate extends VerticalPanel implements
			HasMouseOutHandlers, HasMouseOverHandlers, HasClickHandlers {

		private TemplateDTO template;
		private SelectTemplateWindow parentWnd;
		private SelectableTemplate instance;

		public SelectableTemplate(TemplateDTO aTemplate,
				SelectTemplateWindow aParentWnd) {
			this.ensureDebugId("selectable_template_" + aTemplate.getId());
			this.setWidth("550px");
			this.setHeight("50px");
			this.addStyleName("list_item");
			parentWnd = aParentWnd;
			instance = this;
			this.setTemplate(aTemplate);
			HorizontalPanel textPanel = new HorizontalPanel();

			textPanel.add(new HTML(HTMLHelper.get().getParsedUpdateHtml(
					aTemplate.getTemplateText())));

			HorizontalPanel tags = new HorizontalPanel();
			StringBuffer sb = new StringBuffer();
			sb.append("Tags: ");
			for (String tag : aTemplate.getTags()) {
				sb.append(tag);
				sb.append(" ");
			}

			tags.add(new HTML(sb.toString()));
			tags.addStyleName("tags");

			this.add(textPanel);
			this.add(tags);
			this.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(MouseOverEvent event) {
					addStyleName("list_item_over");
				}

			});
			this.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(MouseOutEvent event) {
					removeStyleName("list_item_over");

				}

			});

			this.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
					String[] userNames = StringUtils.getUserNames(updateWidget.getText());
					String newTemplateText = template.getTemplateText();
					
					
					if (userNames.length>0) {
						for ( int i=0; i<userNames.length ;i++ ) {
							newTemplateText = newTemplateText.replaceAll("\\Q{username_"+i+"}\\E", userNames[i]);
						}
						
					}
					
					
					updateWidget.setTemplateText(newTemplateText);
					
					if (parentWnd.getActiveTemplate() != null
							&& parentWnd.getActiveTemplate().getTemplate()
									.getId() != template.getId()) {
						parentWnd.getActiveTemplate().removeStyleName(
								"list_item_selected");
						instance.addStyleName("list_item_selected");
						parentWnd.setActiveSelectableTemplate(instance);

					} else if (parentWnd.getActiveTemplate() == null) {
						instance.addStyleName("list_item_selected");
						parentWnd.setActiveSelectableTemplate(instance);
						parentWnd.hide(true);
					}

				}

			});
		}

		public void setTemplate(TemplateDTO template) {
			this.template = template;
		}

		public TemplateDTO getTemplate() {
			return template;
		}

		@Override
		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());
		}

		@Override
		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());

		}

		@Override
		public HandlerRegistration addClickHandler(ClickHandler handler) {
			return addDomHandler(handler, ClickEvent.getType());
		}
	}

	public void setSendTwitterUpdateWidget(SendUpdateWidget aWidget) {
		this.updateWidget = aWidget;

	}

	protected void setActiveSelectableTemplate(SelectableTemplate selTemplate) {
		this.activeSelectableTemplateWidget = selTemplate;

	}

	@Override
	public void onNewTemplate(TemplateDTO theTemplate) {
		Panel templatePanel = new SelectableTemplate(theTemplate, this);
		contentPanel.insert(templatePanel, 0);
		// contentPanel.add();

	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		
	}

	@Override
	public void onFailedLoadTemplates(Throwable tr) {
		Window.alert("Error " + tr.getMessage());

		
	}

	@Override
	public void onSuccessLoadTemplates(TemplateDTOList result) {
		setProcessing(false);

		this.templates = result.getTemplates();

		for (TemplateDTO template : this.templates) {
			Panel templatePanel = new SelectableTemplate(template, this);
			contentPanel.insert(templatePanel, 0);

		}
		searchValue.setFocus(true);

		
	}
	
	

}
