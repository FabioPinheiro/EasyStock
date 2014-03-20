package epic.easystock.client.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import epic.easystock.shared.Item;

@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	public LoginInfo login(String requestUri);
	public void saveItemService(String name, String type);
	public ArrayList<Item> getItems();
	//public ArrayList<ItemDTO> getUserItems();
	//public ArrayList<ItemDTO> getUserItemsType(String type);
	//public ArrayList<ItemDTO> getItemsType(String type);
}
