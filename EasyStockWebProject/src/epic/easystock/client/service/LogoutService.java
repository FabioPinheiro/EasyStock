package epic.easystock.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("logout")
public interface LogoutService extends RemoteService {
	public void logout();
}
