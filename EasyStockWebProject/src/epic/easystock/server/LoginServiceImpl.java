package epic.easystock.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
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

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService {

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
		Long amount = (long) 1;
		Item old;
		
		Item e = new Item(user.getEmail(), name, type, amount);
		try {
			pm.makePersistent(e);
		} finally {
			pm.close();
		}
		
		/*try {
			old = pm.getObjectById(Item.class, name);
		} catch (JDOObjectNotFoundException e) {
			old = null;
		}

		if (old != null) {
			try {
				old.setAmount(old.getAmount() + 1);
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
		}*/
		return;
	}

	@Override
	public ArrayList<Item> getItems() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Item.class);
		ArrayList<Item> results = new ArrayList<Item>();
		try {
			for (Item it : (Collection<Item>) q.execute()) {
				results.add(it);
			}
		} finally {
			q.closeAll();
		}

		return results;

	}
/*
	@Override
	public ArrayList<ItemDTO> getUserItems() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Item.class);
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String email;
		
		if (user != null) {
			email = user.getEmail();
		} else {
			email = "";
		}
		
		
		q.setFilter("email == " + email);

		ArrayList<ItemDTO> results;
		try {
			results = new ArrayList<ItemDTO>((Collection<ItemDTO>)q.execute());

		} finally {
			q.closeAll();
		}

		return results;

	}
	
	@Override
	public ArrayList<ItemDTO> getItemsType(String type) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(ItemDTO.class);
		
		
		q.setFilter("type == " + type);
		ArrayList<ItemDTO> results;
		try {
			results = new ArrayList<ItemDTO>((Collection<ItemDTO>)q.execute());

		} finally {
			q.closeAll();
		}

		return results;

	}
	
	@Override
	public ArrayList<ItemDTO> getUserItemsType(String type) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(ItemDTO.class);
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		String email;
		
		if (user != null) {
			email = user.getEmail();
		} else {
			email = "";
		}
		
		q.setFilter("email == " + email);
		q.setFilter("type == " + type);
		
		ArrayList<ItemDTO> results;
		try {
			results = new ArrayList<ItemDTO>((Collection<ItemDTO>)q.execute());

		} finally {
			q.closeAll();
		}

		return results;

	}*/

}
