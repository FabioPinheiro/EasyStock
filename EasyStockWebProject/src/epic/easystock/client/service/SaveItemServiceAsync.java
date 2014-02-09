package epic.easystock.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SaveItemServiceAsync {
	public void login(String requestUri, AsyncCallback<LoginInfo> async);
}
