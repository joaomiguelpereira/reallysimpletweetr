package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.List;

public class TemplateFragmentDTOList implements IModel {

	private List<TemplateFragmentDTO> templateFragmentList = null;

	public TemplateFragmentDTOList() {
		this.templateFragmentList = new ArrayList<TemplateFragmentDTO>();
	}
	public void addTemplateFragmentList(TemplateFragmentDTO frag) {
		this.templateFragmentList.add(frag);
	}

	public List<TemplateFragmentDTO> getTemplateFragmentList() {
		return templateFragmentList;
	}
	
	
	
}
