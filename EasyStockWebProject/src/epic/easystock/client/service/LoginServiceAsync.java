package epic.easystock.client.service;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epic.easystock.shared.Item;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<LoginInfo> async);
	public void saveItemService(String name, String type, AsyncCallback<Void> async);
	public void getItems(AsyncCallback<ArrayList<Item>> async);
	//public void getUserItems(AsyncCallback<ArrayList<ItemDTO>> async);
	//public void getUserItemsType(String type, AsyncCallback<ArrayList<ItemDTO>> async);
	//public void getItemsType(String type, AsyncCallback<ArrayList<ItemDTO>> async);

}
