package epic.easystock.server;

import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import epic.easystock.client.service.LoginInfo;
import epic.easystock.client.service.LoginService;

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
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity item = new Entity("Item");
		
		item.setProperty("name", name);
		item.setProperty("type", type);
		
		Date currentDate = new Date();
		
		item.setProperty("dateAdded", currentDate);
		item.setProperty("#",1);
		datastore.put(item);
		return;
	}
	
	@Override
	public PreparedQuery getItems(){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Item");
		
		PreparedQuery pq = datastore.prepare(q);
		
		return pq;
		/*
		 * Como usar o preparedQuery:
		 
		 	for (Entity result : pq.asIterable()) {
  				String name = (String) result.getProperty("name");
  				String type = (String) result.getProperty("type");
  				Date date = (Date) result.getProperty("dateAdded");
  				Long number = (Long) result.getProperty("#");
				
				...(fazer cenas)
			}
		 */
		
	}
	
}
