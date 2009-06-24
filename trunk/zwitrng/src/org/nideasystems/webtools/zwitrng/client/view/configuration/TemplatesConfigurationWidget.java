package org.nideasystems.webtools.zwitrng.client.view.configuration;

import org.nideasystems.webtools.zwitrng.client.Constants;
import org.nideasystems.webtools.zwitrng.client.view.persona.TemplateList;
import org.nideasystems.webtools.zwitrng.client.view.utils.HTMLHelper;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;

import com.google.gwt.event.dom.client.HasMouseOutHandlers;
import com.google.gwt.event.dom.client.HasMouseOverHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TemplatesConfigurationWidget extends ConfigurationWidget implements TemplateList{

	private VerticalPanel contents;
	@Override
	public void init() {
		
		HorizontalPanel searchPanel = new HorizontalPanel();
		InlineHTML searchTextLabel = new InlineHTML("Search: ");
		TextBox searchValue = new TextBox();
		searchPanel.add(searchTextLabel);
		searchPanel.add(searchValue);
		this.add(searchPanel);
		contents = new VerticalPanel();
		ScrollPanel scrollPanel = new ScrollPanel(contents);
		scrollPanel.setHeight(Constants.CONFIGURATION_PANEL_MAX_HEIGH);
		this.add(scrollPanel);
		loadTemplates();
		
		
		
	}

	private void loadTemplates() {
		contents.clear();
		getController().getTemplates(this);
		
		
	}

	@Override
	public void onFailedLoadTemplates(Throwable tr) {
		Window.alert("Error");
		
	}

	@Override
	public void onNewTemplate(TemplateDTO tmplate) {
		Window.alert("on New Template");
		
	}

	@Override
	public void onSuccessLoadTemplates(TemplateDTOList result) {
		for ( TemplateDTO template : result.getTemplates() ) {
			contents.insert(new EditableTemplate(template), 0);
		}
		
	}
	
	public class EditableTemplate extends VerticalPanel implements HasMouseOutHandlers, HasMouseOverHandlers {
		
		private TemplateDTO template = null;
		
		public EditableTemplate(TemplateDTO template) {
			this.setTemplate(template);
			this.setWidth(Constants.EDITABLE_TEMPLATE_WIDTH);
			this.setHeight(Constants.EDITABLE_TEMPLATE_MIN_HEIGHT);
			this.addStyleName("list_item");			
			HorizontalPanel textPanel = new HorizontalPanel();

			textPanel.add(new HTML(HTMLHelper.get().getParsedUpdateHtml(
					template.getTemplateText())));
			
			HorizontalPanel tags = new HorizontalPanel();
			StringBuffer sb = new StringBuffer();
			sb.append("Tags: ");
			for (String tag : template.getTags()) {
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

		
	}

	@Override
	public void onSelect() {
		loadTemplates();
		
	}

}
