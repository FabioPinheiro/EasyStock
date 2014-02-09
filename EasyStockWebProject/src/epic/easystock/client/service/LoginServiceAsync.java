package epic.easystock.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<LoginInfo> async);
	public void saveItemService(String name, String type, AsyncCallback<Void> async);
}
