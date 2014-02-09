package epic.easystock.client.service;

import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epic.easystock.server.components.Item;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	public LoginInfo login(String requestUri);
	public void saveItemService(String name, String type);
	public List<Item> getItems();
}
