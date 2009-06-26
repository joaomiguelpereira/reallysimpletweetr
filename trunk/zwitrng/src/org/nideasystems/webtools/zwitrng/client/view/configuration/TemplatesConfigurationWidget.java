package org.nideasystems.webtools.zwitrng.client.view.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.controller.MainController;
import org.nideasystems.webtools.zwitrng.client.controller.updates.ShortLinksListenerCallBack;
import org.nideasystems.webtools.zwitrng.client.view.persona.TemplateList;
import org.nideasystems.webtools.zwitrng.client.view.updates.SendUpdateWidget;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.StringUtils;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TemplatesConfigurationWidget extends ConfigurationWidget implements
		TemplateList {
	private InlineHTML searchTextLabel;
	private TextBox searchValue;
	private VerticalPanel contentPanel;
	private Timer timer = null;
	private SelectableTemplate selectedTemplate;

	private Map<Long, SelectableTemplate> templates;

	@Override
	public void init() {
		super.init();
		templates = new HashMap<Long, SelectableTemplate>();
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

	public SelectableTemplate getSelectedTemplate() {
		return selectedTemplate;
	}

	public void onSelect(SelectableTemplate selectedTemplate) {

		// check if is the same

		if (this.selectedTemplate != null) {
			if (this.selectedTemplate != selectedTemplate) {

				this.selectedTemplate.onUnSelected();
				this.selectedTemplate = selectedTemplate;
				this.selectedTemplate.onSelected();
			}

		} else {
			this.selectedTemplate = selectedTemplate;
			this.selectedTemplate.onSelected();
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
			SelectableTemplate template = (SelectableTemplate) contentPanel
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
			SelectableTemplate template = (SelectableTemplate) contentPanel
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
			SelectableTemplate ePanel = new SelectableTemplate(template, this);
			// contentPanel.insert(ePanel, 0);
			contentPanel.add(ePanel);

			templates.put(template.getId(), ePanel);
		}
		searchValue.setFocus(true);

	}

	private interface EditListener {
		public void onCancel();

		public void onSave(TemplateDTO template);

	}

	/**
	 * Widget for the edit template
	 * 
	 * @author jpereira
	 * 
	 */
	public class EditableTemplate extends VerticalPanel implements
			ShortLinksListenerCallBack {
		private TemplateDTO template = null;
		private TextArea templateText = null;
		private InlineHTML remainingChars = null;
		private TextBox templateTags = null;
		private List<EditListener> editListeners = new ArrayList<EditListener>();
		private Image waitingImg = new Image(Constants.WAITING_IMAGE);
		private EditableTemplate instance = null;

		public EditableTemplate() {
			instance = this;
			// Create the main wrapper pannel
			VerticalPanel mainPanel = new VerticalPanel();
			waitingImg.setVisible(false);
			mainPanel.add(waitingImg);
			mainPanel.add(new InlineHTML("Template text:"));

			// Create the TextArea where the template text is
			templateText = new TextArea();
			templateText.setWidth("620px");
			// update.setHeight("35px");
			templateText.addStyleName("input");
			templateText.setVisibleLines(2);
			mainPanel.add(templateText);

			// Add remaining char info
			remainingChars = new InlineHTML("140");
			mainPanel.add(remainingChars);

			// Add short links link
			InlineHTML shortLinksLink = new InlineHTML("Short Links");
			shortLinksLink.addStyleName("link");
			mainPanel.add(shortLinksLink);

			// Add tags

			mainPanel.add(new InlineHTML("Add tags separaded by spaces:"));
			templateTags = new TextBox();
			mainPanel.add(templateTags);

			HorizontalPanel toolsPanel = new HorizontalPanel();
			toolsPanel.setSpacing(5);

			InlineHTML closeLink = new InlineHTML("Cancel");
			closeLink.addStyleName("link");

			shortLinksLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					setUpdating(true);
					SendUpdateWidget.shortLinks(templateText.getValue(),
							controller, instance);

				}
			});
			closeLink.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					fireClose(true);

				}

			});
			Button saveButton = new Button("Save");
			saveButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					save();

				}

			});

			templateText.addKeyUpHandler(new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					updateRemainingChars();

				}
			});

			toolsPanel.add(closeLink);
			toolsPanel.add(saveButton);
			mainPanel.add(toolsPanel);

			this.add(mainPanel);

		}

		protected void updateRemainingChars() {
			int remainingCharsNbr = SendUpdateWidget.DEFAULT_TWEET_SIZE
					- this.templateText.getValue().length();
			remainingChars.setHTML("" + remainingCharsNbr);

		}

		private void save() {

			TemplateDTO newTemplate = new TemplateDTO();
			newTemplate.setCreated(template.getCreated());
			newTemplate.setId(template.getId());
			newTemplate.setTemplateText(getTemplateText());
			// Strip tags
			String[] tags = StringUtils.splitText(getTags());
			for (String tag : tags) {
				newTemplate.addTags(tag);
			}
			newTemplate.setCreated(template.getCreated());
			setUpdating(true);
			try {
				MainController.getInstance().getCurrentPersonaController()
						.saveTemplate(newTemplate,
								new AsyncCallback<TemplateDTO>() {

									@Override
									public void onFailure(Throwable caught) {
										MainController.getInstance()
												.addException(caught);
										setUpdating(false);
									}

									@Override
									public void onSuccess(TemplateDTO result) {
										setUpdating(false);
										for (EditListener listener : editListeners) {
											
											listener.onSave(result);
										}
									}

								});
			} catch (Exception e) {
				MainController.getInstance().addException(e);
				e.printStackTrace();
				setUpdating(false);
			}

		}

		public void setUpdating(boolean b) {
			this.waitingImg.setVisible(b);
		}

		private void fireClose(boolean b) {
			for (EditListener listener : editListeners) {
				listener.onCancel();
			}
		}

		public void addEditListener(EditListener listener) {
			this.editListeners.add(listener);
		}

		@Override
		public void setVisible(boolean visible) {
			super.setVisible(visible);
			templateText.setFocus(visible);

			templateText.setCursorPos(templateText.getValue().length());

		}

		public void setTemplate(TemplateDTO template) {
			this.template = template;
			// update fields
			templateText.setValue(template.getTemplateText());
			templateTags.setValue(template.getTagsAsText());
			updateRemainingChars();

		}

		public TemplateDTO getTemplate() {
			return template;
		}

		public String getTemplateText() {
			// TODO Auto-generated method stub
			return templateText.getValue();
		}

		public String getTags() {
			return templateTags.getValue();

		}

		@Override
		public void onLinksShortened(Map<String, String> result) {
			String newStr = HTMLHelper.get().replaceText(
					templateText.getValue(), result);
			templateText.setFocus(true);
			templateText.setValue(newStr + " ");
			updateRemainingChars();
			setUpdating(false);

		}
	}

	/**
	 * A Selectable Template Widget
	 * 
	 * @author jpereira
	 * 
	 */
	public class SelectableTemplate extends VerticalPanel implements
			HasMouseOutHandlers, HasMouseOverHandlers, HasDoubleClickHandlers,
			HasClickHandlers, EditListener {

		private boolean isEditing = false;
		private TemplateDTO template = null;
		private SelectableTemplate instance = null;
		private TemplatesConfigurationWidget parent = null;
		HorizontalPanel textPanel = null;
		HorizontalPanel toolBar = null;
		HorizontalPanel tags = null;
		EditableTemplate editableTemplate = null;
		private HTML tagsHtml = null;
		private HTML textHtml = null;

		public SelectableTemplate(TemplateDTO theTemplate,
				TemplatesConfigurationWidget theParent) {
			instance = this;
			parent = theParent;

			this.setTemplate(theTemplate);
			this.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
			this.setHeight(Constants.EDITABLE_TEMPLATE_MIN_HEIGHT);
			this.addStyleName("list_item");
			textPanel = new HorizontalPanel();

			textHtml = new HTML();

			textPanel.add(textHtml);

			tags = new HorizontalPanel();
			tagsHtml = new HTML();
			tags.add(tagsHtml);
			tags.addStyleName("tags");

			this.add(textPanel);
			this.add(tags);

			toolBar = createToolbar();
			this.add(toolBar);

			refresh();

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

					setEditing(!isEditing);

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

		private void refresh() {

			textHtml.setHTML(HTMLHelper.get().getParsedUpdateHtml(
					template.getTemplateText()));

			StringBuffer sb = new StringBuffer();
			sb.append("Tags: ");
			for (String tag : template.getTags()) {
				sb.append(tag);
				sb.append(" ");
			}

			tagsHtml.setHTML(sb.toString());
		}

		private HorizontalPanel createToolbar() {

			HorizontalPanel panel = new HorizontalPanel();

			FlexTable table = new FlexTable();

			// panel.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);

			panel.setSpacing(5);
			// Image edit
			Image edit = new Image(Constants.EDIT_ICON);
			edit.setTitle("Edit this template");
			edit.setWidth(Constants.MINI_TOOLBAR_ICON_WIDTH);
			edit.setHeight(Constants.MINI_TOOLBAR_ICON_HEIGHT);
			table.setWidget(0, 0, edit);
			// panel.add(edit);
			Image remove = new Image(Constants.REMOVE_ICON);
			remove.setTitle("Delete this template");
			remove.setWidth(Constants.MINI_TOOLBAR_ICON_WIDTH);
			remove.setHeight(Constants.MINI_TOOLBAR_ICON_HEIGHT);
			table.setWidget(0, 1, remove);

			panel.add(table);

			panel.setVisible(false);

			// Add handler
			remove.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					if (Window
							.confirm("Are you sure you want to delete the template:\n"
									+ template.getTemplateText())) {
						isProcessing(true);
						parent.removeTemplate(instance);
					}

				}

			});
			// Add handler
			edit.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					setEditing(true);
				}

			});

			return panel;
		}

		private void setEditing(boolean editable) {
			if (editableTemplate == null) {
				editableTemplate = new EditableTemplate();
				editableTemplate.addEditListener(this);
				this.add(editableTemplate);

			}
			editableTemplate.setTemplate(this.template);
			// setVisible(!editable);
			textPanel.setVisible(!editable);
			tags.setVisible(!editable);
			toolBar.setVisible(!editable);

			editableTemplate.setVisible(editable);
			isEditing = editable;

		}

		public void onSelected() {
			addStyleName("list_item_selected");
			// add toolbar
			if (!isEditing) {
				this.toolBar.setVisible(true);
			}
		}

		public void onUnSelected() {
			setEditing(false);

			removeStyleName("list_item_selected");
			this.toolBar.setVisible(false);

		}

		protected void select(SelectableTemplate templwidget) {

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

		@Override
		public void onCancel() {
			setEditing(false);

		}

		@Override
		public void onSave(TemplateDTO theTemplate) {

			template = theTemplate;
			refresh();
			setEditing(false);

		}

	}

	@Override
	public void onSelect() {
		loadTemplates();

	}

	protected void removeTemplate(SelectableTemplate instance) {
		getController().removeTemplate(instance.getTemplate(), this);

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
		SelectableTemplate deletedTemplate = templates.get(result.getId());
		if (deletedTemplate != null) {
			contentPanel.remove(deletedTemplate);
			templates.remove(result.getId());
		}
	}

}
