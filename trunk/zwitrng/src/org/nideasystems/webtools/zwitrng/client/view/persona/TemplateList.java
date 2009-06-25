package org.nideasystems.webtools.zwitrng.client.view.persona;

import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTOList;

public interface TemplateList {

	public void onNewTemplate(TemplateDTO tmplate);
	public void onFailedLoadTemplates(Throwable tr);
	public void onFailedDeleteTemplate(Throwable tr);
	public void onSuccessDeleteTemplates(TemplateDTO result);
	public void onSuccessLoadTemplates(TemplateDTOList result);
	
}
