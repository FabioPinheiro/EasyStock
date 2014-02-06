package epic.easystock.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LogoutServiceAsync {
	void logout(AsyncCallback<Void> callback);
}
