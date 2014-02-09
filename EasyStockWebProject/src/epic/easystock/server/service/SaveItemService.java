/**
 * 
 */
package epic.easystock.server.service;

import java.util.Date;

import com.google.appengine.api.datastore.Entity;

/**
 * @author artur
 *
 */
public class SaveItemService extends SaveService {
	private Entity item; 
	private String name;
	private String type;
	/**
	 * 
	 */
	public SaveItemService(String name, String type) {
		super();
		this.name = name;
		this.type = type;
	}

	@Override
	public void execute() {
		item = new Entity("Item");
		
		item.setProperty("name", name);
		item.setProperty("type", type);
		
		Date currentDate = new Date();
		
		item.setProperty("dateAdded", currentDate);
		
		datastore.put(item);
	}

}
