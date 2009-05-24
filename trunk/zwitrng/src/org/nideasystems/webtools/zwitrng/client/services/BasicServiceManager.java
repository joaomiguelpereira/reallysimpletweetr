package org.nideasystems.webtools.zwitrng.client.services;



public class BasicServiceManager implements IServiceManager {

	BasicAutehnticationService authService = new BasicAutehnticationService();
	
	RPCService rpcServiceInService = new RPCService(); 
	
	
	 
	
	@Override
	public IService getPesonaService() throws Exception {
		return null;
	}

	@Override
	public RPCService getRPCService() throws Exception {
		return rpcServiceInService; 
		
	}

	@Override
	public IService getAuthenticationService() throws Exception {
		return authService;
	}

}
