package org.nideasystems.webtools.zwitrng.client.view.configuration;

import org.nideasystems.webtools.zwitrng.shared.model.IModel;


public interface ConfigurationEditListener<T extends IModel> {

		public void onEditCancel();

		public void saveObject(T object);
		
		public void onObjectCreated(T object);
		
		public void onError(Throwable tr);

		public void onObjectSaved(T object);
		
		public void onObjectRemoved(T object);
		
		
		
		


}
