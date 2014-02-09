package epic.easystock.client.service;

import com.google.appengine.api.datastore.PreparedQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	public void login(String requestUri, AsyncCallback<LoginInfo> async);
	public void saveItemService(String name, String type, AsyncCallback<Void> async);
	public void getItems(AsyncCallback<PreparedQuery> async);
}
