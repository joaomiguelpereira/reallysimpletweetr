package org.nideasystems.webtools.zwitrng.client.services;

import java.util.HashMap;
import java.util.Map;

public class BasicServiceManager implements IServiceManager {

	RPCService rpcServiceInService = new RPCService(); 
	
	@Override
	public IService getPesonaService() throws Exception {
		return null;
	}

	@Override
	public RPCService getRPCService() throws Exception {
		return rpcServiceInService; 
		
	}

}
