package epic.easystock;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

@Api(name = "apiEndpoint", namespace = @ApiNamespace(ownerDomain = "easystock.epic", ownerName = "easystock.epic", packagePath = ""), version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
Constants.WEB_CLIENT_ID /* , Constants.ANDROID_CLIENT_ID */}, audiences = { Constants.ANDROID_AUDIENCE })
public class ApiEndpoint {
	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}
	// ################################# USER #################################
	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listUser")
	public CollectionResponse<User> listUser(
	@Nullable @Named("cursor") String cursorString,
	@Nullable @Named("limit") Integer limit) {
		EntityManager mgr = null;
		Cursor cursor = null;
		List<User> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from User as User");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}
			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}
			execute = (List<User>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();
			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (User obj : execute);
		} finally {
			mgr.close();
		}
		return CollectionResponse.<User> builder().setItems(execute)
		.setNextPageToken(cursorString).build();
	}
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 * 
	 * @param id
	 *            return false;
	 * 
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUser")
	public User getUser(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		User user = null;
		try {
			user = mgr.find(User.class, id);
		} finally {
			mgr.close();
		}
		return user;
	}
	/**
	 * This inserts a new entity into App Engine datastore. If the entity already exists in the datastore, an excepti on is thrown. It uses HTTP POST method.
	 * 
	 * @param user
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUser")
	public User insertUser(User user) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsUser(user)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(user);
		} finally {
			mgr.close();
		}
		return user;
	}
	/**
	 * This method is used for updating an existing entity. If the entity does not exist in the datastore, an exception is thrown. It uses HTTP PUT method.
	 * 
	 * @param user
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUser")
	public User updateUser(User user) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsUser(user)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(user);
		} finally {
			mgr.close();
		}
		return user;
	}
	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeUser")
	public void removeUser(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			User user = mgr.find(User.class, id);
			mgr.remove(user);
		} finally {
			mgr.close();
		}
	}
	private boolean containsUser(User user) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			if (user == null) {
				return false;
			}
			User item = findUserByMail(user);
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}
	@SuppressWarnings("unchecked")
	@ApiMethod(name = "findUserByMail")
	public User findUserByMail(User user) {
		EntityManager mgr = null;
		List<User> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery(
			"SELECT u FROM User u WHERE u.email=:email").setParameter(
			"email", user.getEmail());
			query.setFirstResult(0);
			execute = query.getResultList();
		} finally {
			mgr.close();
		}
		if (execute.size() > 0)
			return execute.get(0);
		return null;
	}
	// ################################# USER PANTRY #################################
	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listUserPantry")
	public CollectionResponse<UserPantry> listUserPantry(
	@Nullable @Named("cursor") String cursorString,
	@Nullable @Named("limit") Integer limit) {
		EntityManager mgr = null;
		Cursor cursor = null;
		List<UserPantry> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr
			.createQuery("select from UserPantry as UserPantry");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}
			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}
			execute = (List<UserPantry>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();
			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (UserPantry obj : execute);
		} finally {
			mgr.close();
		}
		return CollectionResponse.<UserPantry> builder().setItems(execute)
		.setNextPageToken(cursorString).build();
	}
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getUserPantry")
	public UserPantry getUserPantry(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		UserPantry userpantry = null;
		try {
			userpantry = mgr.find(UserPantry.class, id);
		} finally {
			mgr.close();
		}
		return userpantry;
	}
	
	
	/**
	 * This inserts a new entity into App Engine datastore. If the entity already exists in the datastore, an exception is thrown. It uses HTTP POST method.
	 * 
	 * @param userpantry
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertUserPantry")
	public UserPantry insertUserPantry(UserPantryDTO userPantryDTO/* UserPantry userpantry, User user, Pantry pantry */) {
		EntityManager mgr = getEntityManager();
		UserPantry userpantry;
		User user = userPantryDTO.getUser();
		Pantry pantry = userPantryDTO.getPantry();
		boolean pantryIsNull = (pantry == null);
		try {
			if (!containsUser(user))
				throw new EntityExistsException("insertUserPantry: User is not regiter in the system: user=" + user.toString());
			if (containsPantry(pantry))
				throw new EntityExistsException("insertUserPantry: Object (pantry) already exists");
			if (pantryIsNull) {
				pantry = new Pantry();
				pantry.setProducts(new ArrayList<MetaProduct>());
				pantry.setName(userPantryDTO.getPantryName());
				pantry = insertPantry(pantry); //mgr.persist();// FIXME verificar se está aqui bem devido if (containsUserPantry(userpantry))			
			}else {}//FIXME care pantry.getKey()
			userpantry = new UserPantry();
			userpantry.setUser(user.getKey().getId());
			userpantry.setPantry(pantry.getKey());
			if (containsUserPantry(userpantry)) {
				throw new EntityExistsException("insertUserPantry: Object (userpantry) already exists");
			}
			mgr.persist(userpantry);
		} finally {
		
			mgr.close();
		}
		return userpantry;
	}
	/**
	 * This method is used for updating an existing entity. If the entity does not exist in the datastore, an exception is thrown. It uses HTTP PUT method.
	 * 
	 * @param userpantry
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateUserPantry")
	public UserPantry updateUserPantry(UserPantry userpantry) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsUserPantry(userpantry)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(userpantry);
		} finally {
			mgr.close();
		}
		return userpantry;
	}
	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE method. FIXME need to remove Pantry maybe to! And maybe the User as despença podem ser partilhadas
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeUserPantry")
	public void removeUserPantry(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			UserPantry userpantry = mgr.find(UserPantry.class, id);
			mgr.remove(userpantry);
		} finally {
			mgr.close();
		}
	}
	private boolean containsUserPantry(UserPantry userpantry) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			if (userpantry == null || userpantry.getKey() == null) {
				return false;
			}
			UserPantry item = findUserPantryByIds(userpantry);
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}
	@SuppressWarnings({ "unchecked", "unused" })
	private UserPantry findUserPantryByIds(UserPantry userpantry) {
		EntityManager mgr = null;
		List<UserPantry> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery(
			"select up from UserPantry up WHERE up.pantry=:pantry AND up.user=:user").setParameter(
			"user", userpantry.getUser()).setParameter("pantry", userpantry.getPantry());
			query.setFirstResult(0);
			execute = query.getResultList();
		} finally {
			mgr.close();
		}
		if (execute.size() > 0)
			return execute.get(0);
		return null;
	}
	@SuppressWarnings("unchecked")
	@ApiMethod(name = "getMyPantryByMail", path = "getPantryByMail")
	public Pantry getMyPantryByMail(@Named("mail") String mail) {
		EntityManager mgr = null;
		List<UserPantry> execute = null;
		try {
			mgr = getEntityManager();
			Query queryUP = mgr
			.createQuery("select from UserPantry as UserPantry");
			execute = (List<UserPantry>) queryUP.getResultList();
			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			Pantry ret = null;
			for (UserPantry obj : execute) {
				User usr = mgr.find(User.class, obj.getUser());
				if (usr != null && usr.getEmail().equals(mail)) {
					ret = mgr.find(Pantry.class, obj.getPantry());
					break;
				}
			}
			if (ret != null) {
				for (MetaProduct p : ret.getProducts())
					p.getProduct();
				return ret;
			}
		} finally {
			mgr.close();
		}
		return null;
	}
	// ################################# PANTRY #################################
	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listPantry")
	public CollectionResponse<Pantry> listPantry(
	@Nullable @Named("cursor") String cursorString,
	@Nullable @Named("limit") Integer limit) {
		EntityManager mgr = null;
		Cursor cursor = null;
		List<Pantry> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Pantry as Pantry");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}
			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}
			execute = (List<Pantry>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();
			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Pantry obj : execute);
		} finally {
			mgr.close();
		}
		return CollectionResponse.<Pantry> builder().setItems(execute)
		.setNextPageToken(cursorString).build();
	}
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getPantry")
	public Pantry getPantry(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Pantry pantry = null;
		try {
			pantry = mgr.find(Pantry.class, id);
		} finally {
			mgr.close();
		}
		return pantry;
	}
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "getPantryByMailAndName")
	public Pantry getPantryByMailAndName(@Named("mail") String mail,
	@Named("name") String name) {
		EntityManager mgr = null;
		List<UserPantry> userPantries = null;
		List<Pantry> pantries = null;
		List<User> users = null;
		Pantry ret = null;
		try {
			mgr = getEntityManager();
			Query query1 = mgr.createQuery(
			"SELECT u FROM User u WHERE u.email=:email").setParameter(
			"email", mail);
			query1.setFirstResult(0);
			users = query1.getResultList();
			User user = users.get(0);
			Query query2 = mgr.createQuery(
			"SELECT u FROM UserPantry u WHERE u.user=:user")
			.setParameter("user", user.getKey().getId());
			query2.setFirstResult(0);
			userPantries = query2.getResultList();
			pantries = new ArrayList<Pantry>();
			for (UserPantry up : userPantries) {
				pantries.add(mgr.find(Pantry.class, up.getPantry()));
			}
			for (Pantry p : pantries) {
				if (p.getName().equals(name))
					ret = p;
			}
			if (ret != null)
				for (MetaProduct p : ret.getProducts()) {} // FIXME isto é inutil ?! era suporto fazer o que?
		} finally {
			mgr.close();
		}
		return ret;
	}
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getPantryProducts")
	public List<MetaProduct> getPantryProducts(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Pantry pantry = null;
		try {
			pantry = mgr.find(Pantry.class, id);
		} finally {
			mgr.close();
		}
		return pantry.getProducts();
	}
	/*
	 * LIXO FIXME see insertUserPantry(userpantry, user, pantry) This inserts a new entity into App Engine datastore. If the entity already exists in the datastore, an exception is thrown. It uses HTTP POST method.
	 * 
	 * @param pantry the entity to be inserted.
	 * 
	 * @return The inserted entity.
	 */
	//@ApiMethod(name = "insertPantry")
	public Pantry insertPantry(Pantry pantry) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsPantry(pantry)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(pantry);
		} finally {
			mgr.close();
		}
		return pantry;
	}
	/**
	 * This method is used for updating an existing entity. If the entity does not exist in the datastore, an exception is thrown. It uses HTTP PUT method.
	 * 
	 * @param pantry
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updatePantry")
	public Pantry updatePantry(Pantry pantry) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsPantry(pantry)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(pantry);
		} finally {
			mgr.close();
		}
		return pantry;
	}
	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removePantry")
	public void removePantry(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Pantry pantry = mgr.find(Pantry.class, id);
			mgr.remove(pantry);
		} finally {
			mgr.close();
		}
	}
	private boolean containsPantry(Pantry pantry) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			if (pantry == null || pantry.getKey() == null) {
				return false;
			}
			Pantry item = mgr.find(Pantry.class, pantry.getKey());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}
	// ################################# PRODUCT #################################
	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listProduct")
	public CollectionResponse<Product> listProduct(
	@Nullable @Named("cursor") String cursorString,
	@Nullable @Named("limit") Integer limit) {
		EntityManager mgr = null;
		Cursor cursor = null;
		List<Product> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Product as Product");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}
			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}
			execute = (List<Product>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();
			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (Product obj : execute);
		} finally {
			mgr.close();
		}
		return CollectionResponse.<Product> builder().setItems(execute)
		.setNextPageToken(cursorString).build();
	}
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getProduct")
	public Product getProduct(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		Product product = null;
		try {
			product = mgr.find(Product.class, id);
		} finally {
			mgr.close();
		}
		return product;
	}
	@ApiMethod(name = "getProductByBarCode", path = "getProductByBarCode")
	public Product getProductByBarCode(@Named("id") Long id) {
		EntityManager mgr = null;
		List<Product> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery(
			"SELECT p FROM Product p WHERE p.BarCode=:BarCode").setParameter(
			"BarCode", id);
			query.setFirstResult(0);
			query.setMaxResults(1);
			execute = query.getResultList();
		} finally {
			mgr.close();
		}
		if (execute.size() == 1)
			return execute.get(0);
		return null;
	}
	/**
	 * This inserts a new entity into App Engine datastore. If the entity already exists in the datastore, an exception is thrown. It uses HTTP POST method.
	 * 
	 * @param product
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertProduct")
	public Product insertProduct(Product product) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsProduct(product)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(product);
		} finally {
			mgr.close();
		}
		return product;
	}
	/**
	 * This method is used for updating an existing entity. If the entity does not exist in the datastore, an exception is thrown. It uses HTTP PUT method.
	 * 
	 * @param product
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateProduct")
	public Product updateProduct(Product product) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsProduct(product)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(product);
		} finally {
			mgr.close();
		}
		return product;
	}
	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeProduct")
	public void removeProduct(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Product product = mgr.find(Product.class, id);
			mgr.remove(product);
		} finally {
			mgr.close();
		}
	}
	private boolean containsProduct(Product product) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			if (product == null || product.getKey() == null) {
				return false;
			}
			Product item = mgr.find(Product.class, product.getKey());
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}
	// ################################# META PRODUCT #################################
	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listMetaProduct")
	public CollectionResponse<MetaProduct> listMetaProduct(
	@Nullable @Named("cursor") String cursorString,
	@Nullable @Named("limit") Integer limit) {
		EntityManager mgr = null;
		Cursor cursor = null;
		List<MetaProduct> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr
			.createQuery("select from MetaProduct as MetaProduct");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}
			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}
			execute = (List<MetaProduct>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();
			// Tight loop for fetching all entities from datastore and
			// accomodate
			// for lazy fetch.
			for (MetaProduct obj : execute);
		} finally {
			mgr.close();
		}
		return CollectionResponse.<MetaProduct> builder().setItems(execute)
		.setNextPageToken(cursorString).build();
	}
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 * 
	 * @param id
	 *            the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getMetaProduct")
	public MetaProduct getMetaProduct(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		MetaProduct metaproduct = null;
		try {
			metaproduct = mgr.find(MetaProduct.class, id);
		} finally {
			mgr.close();
		}
		return metaproduct;
	}
	/**
	 * This inserts a new entity into App Engine datastore. If the entity already exists in the datastore, an exception is thrown. It uses HTTP POST method.
	 * 
	 * @param metaproduct
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertMetaProduct")
	public MetaProduct insertMetaProduct(MetaProduct metaproduct) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsMetaProduct(metaproduct)) {
				metaproduct.setAmount(metaproduct.getAmount() + 1);
				updateMetaProduct(metaproduct);
				return metaproduct;
			}
			mgr.persist(metaproduct);
		} finally {
			mgr.close();
		}
		return metaproduct;
	}
	/**
	 * This method is used for updating an existing entity. If the entity does not exist in the datastore, an exception is thrown. It uses HTTP PUT method.
	 * 
	 * @param metaproduct
	 *            the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateMetaProduct")
	public MetaProduct updateMetaProduct(MetaProduct metaproduct) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsMetaProduct(metaproduct)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(metaproduct);
		} finally {
			mgr.close();
		}
		return metaproduct;
	}
	/**
	 * This method removes the entity with primary key id. It uses HTTP DELETE method.
	 * 
	 * @param id
	 *            the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeMetaProduct")
	public void removeMetaProduct(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			MetaProduct metaproduct = mgr.find(MetaProduct.class, id);
			mgr.remove(metaproduct);
		} finally {
			mgr.close();
		}
	}
	private boolean containsMetaProduct(MetaProduct metaproduct) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			if (metaproduct == null)
				return false;
			MetaProduct item = containsByProduct(metaproduct);
			if (item == null) {
				contains = false;
			}
		} finally {
			mgr.close();
		}
		return contains;
	}
	@SuppressWarnings({ "unused", "unchecked" })
	private MetaProduct containsByProduct(MetaProduct metaproduct) {
		EntityManager mgr = null;
		List<MetaProduct> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery(
			"SELECT m FROM MetaProduct m WHERE m.product=:product")
			.setParameter("product", metaproduct.getProduct());
			query.setFirstResult(0);
			execute = query.getResultList();
		} finally {
			mgr.close();
		}
		if (execute.size() > 0)
			return execute.get(0);
		return null;
	}
}
