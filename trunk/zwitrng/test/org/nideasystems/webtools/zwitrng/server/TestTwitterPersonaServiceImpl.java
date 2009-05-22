package org.nideasystems.webtools.zwitrng.server;


import static org.easymock.EasyMock.*;


import org.nideasystems.webtools.zwitrng.shared.model.PersonaDTO;
import org.nideasystems.webtools.zwitrng.shared.model.TwitterAccountDTO;


import com.google.appengine.api.users.UserService;

public class TestTwitterPersonaServiceImpl extends LocalDataStoreTestCase {

	private TwitterPersonaServiceImpl service = null;
	
	
	@Override
	protected void setUp() throws Exception {	
		super.setUp();
		
	
	}


	public void testCreatePersonaBadTwitter() {
		TwitterPersonaServiceImpl service = new TwitterPersonaServiceImpl();
		//Mocking UserService
		UserService userServiceMock = createStrictMock(UserService.class);
		expect(userServiceMock.getCurrentUser()).andReturn(user);
		replay(userServiceMock);
		service.setUserService(userServiceMock);
		
		//Create the persona Obj
		PersonaDTO persona = new PersonaDTO();
		persona.setName("Teste");
		
		TwitterAccountDTO twitterAccountObj = new TwitterAccountDTO();
		twitterAccountObj.setTwitterScreenName(validTwitterName+"ll");
		twitterAccountObj.setTwitterPassword(validTwitterPassword+"..");
		persona.setTwitterAccount(twitterAccountObj);
		
		
		PersonaDTO newPersonaObj = null;
		boolean hasException = false;
		try {
			newPersonaObj = service.createPersona(persona);
		} catch (Exception e) {
			e.printStackTrace();
			hasException = true;
		}
		assertTrue(hasException);
	
		
	}
	public void testCreatePersonaOk() {
		
		TwitterPersonaServiceImpl service = new TwitterPersonaServiceImpl();
		//Mocking UserService
		UserService userServiceMock = createStrictMock(UserService.class);
		expect(userServiceMock.getCurrentUser()).andReturn(user);
		replay(userServiceMock);
		service.setUserService(userServiceMock);
		
	
		//Create the persona Obj
		PersonaDTO persona = new PersonaDTO();
		persona.setName("Teste");
		
		TwitterAccountDTO twitterAccountObj = new TwitterAccountDTO();
		twitterAccountObj.setTwitterScreenName(validTwitterName);
		twitterAccountObj.setTwitterPassword(validTwitterPassword);
		persona.setTwitterAccount(twitterAccountObj);
		
		
		PersonaDTO newPersonaObj = null;
		try {
			newPersonaObj = service.createPersona(persona);
		} catch (Exception e) {
			e.printStackTrace();
			fail("");
		}
		assertNotNull(newPersonaObj);
		assertEquals("Teste",newPersonaObj.getName());
		assertEquals(newPersonaObj.getUserEmail(),"joao");
		
		assertEquals(newPersonaObj.getTwitterAccount().getTwitterPassword(),null);
		assertNotNull(newPersonaObj.getTwitterAccount().getTwitterDescription());
		assertNotNull(newPersonaObj.getTwitterAccount().getTwitterImageUrl());
		assertEquals(newPersonaObj.getTwitterAccount().getTwitterScreenName(),validTwitterName);
		
		assertNotNull(newPersonaObj.getTwitterAccount());
		
		
		
	}
}
