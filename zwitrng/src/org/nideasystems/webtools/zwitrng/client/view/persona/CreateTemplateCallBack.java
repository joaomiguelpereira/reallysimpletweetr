package org.nideasystems.webtools.zwitrng.client.view.persona;

import org.nideasystems.webtools.zwitrng.shared.model.TemplateDTO;

public interface CreateTemplateCallBack {
	
	public void onSuccessCreateTemplate(TemplateDTO template);
	public void onFailCreateTemplate(Throwable ex);

}
