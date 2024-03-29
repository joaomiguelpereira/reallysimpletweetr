package org.nideasystems.webtools.zwitrng.server;

import com.google.appengine.api.datastore.dev.LocalDatastoreService;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;

public class LocalDataStoreTestCase extends LocalServiceTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ApiProxyLocalImpl proxy = (ApiProxyLocalImpl)ApiProxy.getDelegate();
		proxy.setProperty(LocalDatastoreService.NO_STORAGE_PROPERTY, Boolean.TRUE.toString());
		
	}

	@Override
	protected void tearDown() throws Exception {
		ApiProxyLocalImpl proxy = (ApiProxyLocalImpl) ApiProxy.getDelegate();
        LocalDatastoreService datastoreService = (LocalDatastoreService) proxy.getService("datastore_v3");
        datastoreService.clearProfiles();

		super.tearDown();
	}

}
