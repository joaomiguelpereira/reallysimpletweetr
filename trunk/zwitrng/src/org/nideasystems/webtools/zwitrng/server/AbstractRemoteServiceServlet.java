package org.nideasystems.webtools.zwitrng.server;

import java.util.Date;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.nideasystems.webtools.zwitrng.server.domain.PersonaDAO;
import org.nideasystems.webtools.zwitrng.server.pojos.BusinessHelper;
import org.nideasystems.webtools.zwitrng.server.pojos.PersonaPojo;
import org.nideasystems.webtools.zwitrng.server.pojos.TemplatePojo;

import com.google.appengine.api.users.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public abstract class AbstractRemoteServiceServlet extends RemoteServiceServlet{

	private static final Logger log = Logger
	.getLogger(AbstractRemoteServiceServlet.class.getName());
	
	
	//SerialVersionId
	private static final long serialVersionUID = -5536799464897873496L;
	//Logged user
	protected User user = null;
	//Start time when the transaction started
	private long transactionStartTime;
	//The current Persistence Manager
	private PersistenceManager pm;
	
	
	private ThreadLocal<BusinessHelper> businessHelper = new ThreadLocal<BusinessHelper>() {
		@Override
		protected BusinessHelper initialValue() {
			return new BusinessHelper();
		}
	};
	
	public BusinessHelper getBusinessHelper() {
		BusinessHelper bHelper = businessHelper.get();
		bHelper.setPm(pm);
		return bHelper;
	}
	
	
	/**
	 * Ends a transaction
	 */
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

	/**
	 * Start a transaction
	 * @param persistenceNeeded true if a persistenceManages is needed for the transaction
	 * @throws Exception
	 */
	protected void startTransaction(boolean persistenceNeeded) throws Exception {
		log.fine("Starting transaction. PersistenceNeeded? "
				+ persistenceNeeded);
		if (persistenceNeeded) {
			this.pm = PMF.get().getPersistenceManager();
		}
		this.user = AuthorizationManager.checkAuthentication();
		this.transactionStartTime = new Date().getTime();

	}


}
