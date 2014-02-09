/**
 * 
 */
package epic.easystock.server.service;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

/**
 * @author artur
 *
 */
public abstract class SaveService {
	protected DatastoreService datastore;
	
	public SaveService(){
		datastore = DatastoreServiceFactory.getDatastoreService();
	}
	
	public abstract void execute();
	
	protected  DatastoreService getDatastore(){
		return datastore;
	}
	
	
}
