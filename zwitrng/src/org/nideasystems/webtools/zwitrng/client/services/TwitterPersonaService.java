package org.nideasystems.webtools.zwitrng.client.services;




import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("personaService")
public interface TwitterPersonaService extends RemoteService {
	String createPersona(String persona);
	String getPesonas();
	String deletePersona(String persona);
}
