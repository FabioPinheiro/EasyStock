/**
 * 
 */
package epic.easystock.server.service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * @author artur
 *
 */
public abstract class SaveService extends RemoteServiceServlet {
	
	private static final long serialVersionUID = 1L; //FIXME faz sendito?
	
	protected DatastoreService datastore;
	
	public SaveService(){
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	
	public abstract void execute();
	
	protected  DatastoreService getDatastore(){
		return datastore;
	}
	
	
}
