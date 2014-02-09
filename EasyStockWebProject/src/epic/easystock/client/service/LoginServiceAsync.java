package epic.easystock.client.service;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import epic.easystock.shared.Item;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<LoginInfo> async);
	public void saveItemService(String name, String type, AsyncCallback<Void> async);
	public void getItems(AsyncCallback<List<Item>> async);
}
