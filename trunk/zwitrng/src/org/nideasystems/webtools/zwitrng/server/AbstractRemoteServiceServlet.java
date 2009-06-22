package org.nideasystems.webtools.zwitrng.server;

import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;
import org.nideasystems.webtools.zwitrng.server.pojos.PersonaPojo;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class AbstractRemoteServiceServlet extends RemoteServiceServlet{

	private static final Logger log = Logger
	.getLogger(AbstractRemoteServiceServlet.class.getName());
	/**
	 * 
	 */
	private static final long serialVersionUID = -5536799464897873496L;

	private long transactionStartTime;
	private PersistenceManager pm;
	private ThreadLocal<PersonaPojo> personaPojo = new ThreadLocal<PersonaPojo>() {

		@Override
		protected PersonaPojo initialValue() {
			return new PersonaPojo();
		}

	};

	
	protected PersonaPojo getPersonaPojo() {
		PersonaPojo pojo = personaPojo.get();
		pojo.setPm(pm);
		return pojo;
		
	}
	
	
	
	protected void endTransaction() {
		long endTransactionTime = new Date().getTime();
		log.fine("End trasaction in "
				+ (endTransactionTime - transactionStartTime) + " ms");
		if (this.pm != null && !this.pm.isClosed()) {
			try {
				pm.close();
			} catch (Exception e) {
				log.severe("exception: " + e.getMessage());
				e.printStackTrace();
			}
		}

	}

	protected void startTransaction(boolean persistenceNeeded) {
		log.fine("Starting transaction. PersistenceNeeded? "
				+ persistenceNeeded);
		if (persistenceNeeded) {
			this.pm = PMF.get().getPersistenceManager();
		}
		this.transactionStartTime = new Date().getTime();

	}


}
