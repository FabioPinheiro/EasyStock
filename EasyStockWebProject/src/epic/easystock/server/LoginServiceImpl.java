package epic.easystock.server;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import epic.easystock.client.service.LoginInfo;
import epic.easystock.client.service.LoginService;
import epic.easystock.shared.Item;
import epic.easystock.shared.PMF;

public class LoginServiceImpl extends RemoteServiceServlet implements
		LoginService {

	private static final long serialVersionUID = 1L;

	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();
		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}

	@Override
	public void saveItemService(String name, String type) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Item item;
		Long amount = (long) 1;
		Item e = pm.getObjectById(Item.class, name);

		if (e != null) {
			try {
				e.setAmount(e.getAmount() + 1);
			} finally {
				pm.close();
			}

		} else {
			if (user != null) {
				item = new Item(user.getEmail(), name, type, amount);
			} else {
				item = new Item(null, name, type, amount);
			}
			try {
				pm.makePersistent(item);
			} finally {
				pm.close();
			}
		}
		return;
	}

	@Override
	public List<Item> getItems() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Item.class);
		List<Item> results;
		try {
			results = (List<Item>) q.execute();

		} finally {
			q.closeAll();
		}

		return results;

	}

}
