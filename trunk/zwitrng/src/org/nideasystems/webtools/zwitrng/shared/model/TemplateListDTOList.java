package org.nideasystems.webtools.zwitrng.shared.model;

import java.util.ArrayList;
import java.util.List;

public class TemplateListDTOList implements IDTO {

	private List<TemplateListDTO> templateFragmentList = null;

	public TemplateListDTOList() {
		this.templateFragmentList = new ArrayList<TemplateListDTO>();
	}
	public void addTemplateFragmentList(TemplateListDTO frag) {
		this.templateFragmentList.add(frag);
	}

	public List<TemplateListDTO> getTemplateFragmentList() {
		return templateFragmentList;
	}
	
	
	
}
