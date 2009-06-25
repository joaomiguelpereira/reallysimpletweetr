package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.HashMap;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.view.persona.TemplateList;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
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
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TemplatesConfigurationWidget extends ConfigurationWidget implements
		TemplateList {
	private InlineHTML searchTextLabel;
	private TextBox searchValue;
	private VerticalPanel contentPanel;
	private Timer timer = null;
	private EditableTemplate selectedTemplate;

	private Map<Long, EditableTemplate> templates;
	@Override
	public void init() {
		super.init();
		templates = new HashMap<Long, EditableTemplate>();
		HorizontalPanel searchPanel = new HorizontalPanel();
		searchTextLabel = new InlineHTML("Search: ");
		searchValue = new TextBox();
		searchPanel.add(searchTextLabel);
		searchPanel.add(searchValue);
		this.add(searchPanel);
		contentPanel = new VerticalPanel();
		ScrollPanel scrollPanel = new ScrollPanel(contentPanel);
		scrollPanel.setHeight(Constants.CONFIGURATION_PANEL_MAX_HEIGH);
		this.add(scrollPanel);
		
		// loadTemplates();

		// load callbacks
		searchValue.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				startTimer();

			}

		});

	}

	public EditableTemplate getSelectedTemplate() {
		return selectedTemplate;
	}

	public void onSelect(EditableTemplate selectedTemplate) {

		// check if is the same

		if (this.selectedTemplate != null) {
			if (this.selectedTemplate != selectedTemplate) {

				this.selectedTemplate.onUnSelected();
				this.selectedTemplate = selectedTemplate;
				this.selectedTemplate.onSelectd();
			}

		} else {
			this.selectedTemplate = selectedTemplate;
			this.selectedTemplate.onSelectd();
		}

		// remove the styke from current
		if (this.selectedTemplate != null
				&& this.selectedTemplate != selectedTemplate) {
			this.selectedTemplate.onUnSelected();
		}

	}

	private void startTimer() {
		if (timer == null) {
			timer = new Timer() {
				@Override
				public void run() {
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
			EditableTemplate template = (EditableTemplate) contentPanel
					.getWidget(i);

			String lookupValue = searchValue.getValue().toLowerCase();
			String tagText = template.getTemplate().getTagsAsText()
					.toLowerCase();
			if (!template.getTemplate().getTemplateText().toLowerCase()
					.contains(lookupValue)
					&& !tagText.contains(lookupValue)) {
				visibleIndexes[i] = 0; // dont show this
			} else {
				visibleIndexes[i] = 1;// show this
			}
		}

		// remove them
		for (int i = 0; i < widgetCount; i++) {
			EditableTemplate template = (EditableTemplate) contentPanel
					.getWidget(i);
			template.setVisible(visibleIndexes[i] == 1);
		}
	}

	private void loadTemplates() {
		isProcessing(true);
		contentPanel.clear();
		getController().getTemplates(this);

	}

	@Override
	public void onFailedLoadTemplates(Throwable tr) {
		Window.alert("Error: " + tr.getMessage());
		isProcessing(false);

	}

	@Override
	public void onNewTemplate(TemplateDTO tmplate) {
		Window.alert("on New Template");
		isProcessing(false);

	}

	@Override
	public void onSuccessLoadTemplates(TemplateDTOList result) {

		isProcessing(false);
		for (TemplateDTO template : result.getTemplates()) {
			EditableTemplate ePanel = new EditableTemplate(template, this); 
			//contentPanel.insert(ePanel, 0);
			contentPanel.add(ePanel);
			
			templates.put(template.getId(),ePanel);
		}
		searchValue.setFocus(true);

	}

	public class EditableTemplate extends VerticalPanel implements
			HasMouseOutHandlers, HasMouseOverHandlers, HasDoubleClickHandlers,
			HasClickHandlers {

		private TemplateDTO template = null;
		private EditableTemplate instance = null;
		private TemplatesConfigurationWidget parent = null;
		HorizontalPanel toolBar = null;

		public EditableTemplate(TemplateDTO theTemplate,
				TemplatesConfigurationWidget theParent) {
			instance = this;
			parent = theParent;

			this.setTemplate(theTemplate);
			this.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
			this.setHeight(Constants.EDITABLE_TEMPLATE_MIN_HEIGHT);
			this.addStyleName("list_item");
			HorizontalPanel textPanel = new HorizontalPanel();
			textPanel.add(new HTML(HTMLHelper.get().getParsedUpdateHtml(
					theTemplate.getTemplateText())));
			HorizontalPanel tags = new HorizontalPanel();
			StringBuffer sb = new StringBuffer();
			sb.append("Tags: ");
			for (String tag : theTemplate.getTags()) {
				sb.append(tag);
				sb.append(" ");
			}

			tags.add(new HTML(sb.toString()));
			tags.addStyleName("tags");

			this.add(textPanel);
			this.add(tags);

			toolBar = createToolbar();
			this.add(toolBar);
			
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

			this.addDoubleClickHandler(new DoubleClickHandler() {

				@Override
				public void onDoubleClick(DoubleClickEvent event) {
					Window.alert("DoubleClick");

				}

			});

			this.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					select(instance);

					// addStyleName("list_item_selected");

				}

			});

		}

		private HorizontalPanel createToolbar() {
			
			HorizontalPanel panel = new HorizontalPanel();
			
			
			FlexTable table = new FlexTable();
			
			panel.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
			
			panel.setSpacing(5);
			//Image edit 
			Image edit = new Image(Constants.EDIT_ICON);
			edit.setTitle("Edit this template");
			edit.setWidth(Constants.MINI_TOOLBAR_ICON_WIDTH);
			edit.setHeight(Constants.MINI_TOOLBAR_ICON_HEIGHT);
			table.setWidget(0, 0, edit);
			//panel.add(edit);
			Image remove = new Image(Constants.REMOVE_ICON);
			remove.setTitle("Delete this template");
			remove.setWidth(Constants.MINI_TOOLBAR_ICON_WIDTH);
			remove.setHeight(Constants.MINI_TOOLBAR_ICON_HEIGHT);
			table.setWidget(0, 1, remove);
			
			panel.add(table);
			
			panel.setVisible(false);
			
			//Add handler
			remove.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if ( Window.confirm("Are you sure you want to delete the template:\n"+template.getTemplateText()) ) {
						isProcessing(true);
						parent.removeTemplate(instance);
					}
				
				}
				
			});
			return panel;
		}

		public void onSelectd() {
			addStyleName("list_item_selected");
			// add toolbar
			this.toolBar.setVisible(true);
		}

		public void onUnSelected() {
			removeStyleName("list_item_selected");
			this.toolBar.setVisible(false);

		}

		protected void select(EditableTemplate templwidget) {
			// Chck the selected
			// if (parent )

			// }

			parent.onSelect(this);

		}

		@Override
		public HandlerRegistration addMouseOutHandler(MouseOutHandler handler) {
			return addDomHandler(handler, MouseOutEvent.getType());
		}

		@Override
		public HandlerRegistration addMouseOverHandler(MouseOverHandler handler) {
			return addDomHandler(handler, MouseOverEvent.getType());

		}

		public void setTemplate(TemplateDTO template) {
			this.template = template;
		}

		public TemplateDTO getTemplate() {
			return template;
		}

		@Override
		public HandlerRegistration addDoubleClickHandler(
				DoubleClickHandler handler) {
			return addDomHandler(handler, DoubleClickEvent.getType());
		}

		@Override
		public HandlerRegistration addClickHandler(ClickHandler handler) {

			return addDomHandler(handler, ClickEvent.getType());
		}

	}

	@Override
	public void onSelect() {
		loadTemplates();

	}

	protected void removeTemplate(EditableTemplate instance) {
		getController().removeTemplate(instance.getTemplate(),this);
		
	}

	@Override
	public void onFailedDeleteTemplate(Throwable tr) {
		Window.alert("Error deleting");
		isProcessing(false);
		
	}

	@Override
	public void onSuccessDeleteTemplates(TemplateDTO result) {
		isProcessing(false);
		//
		EditableTemplate deletedTemplate = templates.get(result.getId());
		if ( deletedTemplate != null ) {
			contentPanel.remove(deletedTemplate);
			templates.remove(result.getId());
		}
	}

}
