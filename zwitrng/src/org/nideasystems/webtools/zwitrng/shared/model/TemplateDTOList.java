package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.List;


public class TemplateDTOList implements IModel {

	private  List<TemplateDTO> templates = new ArrayList<TemplateDTO>();;


	public List<TemplateDTO> getTemplates() {
		return templates;
	}
	
	public void addTemplate(TemplateDTO template) {
		this.templates.add(template);
	}
	
	
}
