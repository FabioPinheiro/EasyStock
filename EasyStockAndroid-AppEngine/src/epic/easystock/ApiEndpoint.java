package epic.easystock;

import java.util.ArrayList;
import java.util.Date;
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
import com.google.appengine.api.datastore.TransactionOptions;
import com.google.appengine.datanucleus.query.JPACursorHelper;

@Api(name = "apiEndpoint", namespace = @ApiNamespace(ownerDomain = "easystock.epic", ownerName = "easystock.epic", packagePath = ""), version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID /*
																																																								 * ,
																																																								 * Constants
																																																								 * .
																																																								 * ANDROID_CLIENT_ID
																																																								 */}, audiences = { Constants.ANDROID_AUDIENCE })
public class ApiEndpoint {
	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

	// ################################# API INTERFACE ADMIN
	// #################################
	@ApiMethod(name = "insertProduct")
	public Product insertProduct(Product product) {
		EntityManager mgr = getEntityManager();
		try {
			if (mContainsProduct(mgr, product)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(product);
		} finally {
			mgr.close();
		}
		return product;
	}

	@ApiMethod(name = "updateProduct")
	public Product updateProduct(Product product) {
		EntityManager mgr = getEntityManager();
		try {
			if (!mContainsProduct(mgr, product)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(product);
		} finally {
			mgr.close();
		}
		return product;
	}

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

	// ################################# API INTERFACE
	// #################################
	@ApiMethod(name = "registerUser")
	public User registerUser(@Named("userEmail") String userEmail) { // FIXME
																		// não
																		// devia
																		// reseber
																		// nada,
																		// devia
																		// ir
																		// buscar
																		// o
																		// email
																		// ao
																		// cretividado
		EntityManager mgr = getEntityManager();
		User user = null;
		try {
			user = mGetUserByEmail(mgr, userEmail);
			if (user != null) {
				throw new EntityExistsException("Object already exists");
			}
			user = new User();
			user.setEmail(userEmail);
			String[] uMail = userEmail.split("@");
			user.setNick(uMail[0]); // user.setNick("NICK"); //FIXME
			user.setUserPantriesList(new ArrayList<UserPantry>()); // FIXME
																	// NOTWORKING
			mgr.persist(user);
		} finally {
			mgr.close();
		}
		return user;
	}

	@ApiMethod(name = "getUserByEmail", path = "getUserByEmail")
	public User getUserByEmail(@Named("userEmail") String userEmail) { // FIXME
																		// getUserByEmaili
																		// i
		EntityManager mgr = getEntityManager();
		try {
			return mGetUserByEmail(mgr, userEmail);
		} finally {
			mgr.close();
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listUserPantryOfUser")
	public List<UserPantry> listUserPantryOfUser(
			@Named("userKey") String userEmail) {
		EntityManager mgr = null;
		List<UserPantry> userPantriesList = null;
		User user = null;
		try {
			mgr = getEntityManager();
			// FIXME User user = getUserByEmail(userEmail);
			Query query = mgr.createQuery(
					"SELECT u FROM User u WHERE u.email=:email").setParameter(
					"email", userEmail);
			query.setFirstResult(0);
			@SuppressWarnings("unchecked")
			List<User> users = query.getResultList();
			if (users.size() > 0) {
				user = users.get(0);
				for (UserPantry iii : user.getUserPantriesList())
					;
			}
			userPantriesList = user.getUserPantriesList();
		} finally {
			mgr.close();
		}
		return userPantriesList;
		// return CollectionResponse.<UserPantry>
		// builder().setItems(execute).setNextPageToken(cursorString).build();
	}

	@ApiMethod(name = "insertUserPantry")
	public UserPantry insertUserPantry(UserPantryDTO userPantryDTO/*
																 * UserPantry
																 * userpantry,
																 * User user,
																 * Pantry pantry
																 */) {
		UserPantry userpantry;
		Pantry pantry = userPantryDTO.getPantry();
		boolean pantryIsNull = (pantry == null);
		EntityManager mgr = getEntityManager();
		try {
			User user = this.mGetUserByEmail(mgr, userPantryDTO.getEmail());
			if (!mContainsUser(mgr, user)) // FIXME não gosto
				throw new EntityExistsException(
						"insertUserPantry: User is not regiter in the system: user="
								+ user.toString());
			if (mContainsPantry(mgr, pantry))
				throw new EntityExistsException(
						"insertUserPantry: Object (pantry) already exists");
			if (pantryIsNull) {
				pantry = new Pantry(userPantryDTO.getPantryName());
				pantry.setTimeStamp(userPantryDTO.getPantryTimeStamp());
				pantry.setMetaProducts(new ArrayList<MetaProduct>());
				pantry.setName(userPantryDTO.getPantryName());
				mInsertPantry(pantry); // FIXME Caused by:
										// java.lang.IllegalArgumentException:
										// cross-group transaction need to be
										// explicitly specified, see
										// TransactionOptions.Builder.withXG
			} else {
			}// FIXME care pantry.getKey()
			userpantry = new UserPantry();
			userpantry.setUserKey(user.getKey());
			userpantry.setPantry(pantry);
			if (mContainsUserPantry(mgr, userpantry)) {
				throw new EntityExistsException(
						"insertUserPantry: Object (userpantry) already exists");
			}
			mgr.persist(userpantry);
		} finally {
			mgr.close();
		}
		return userpantry;
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
				for (MetaProduct p : ret.getMetaProducts()) {
				} // FIXME isto é inutil ?! era suporto fazer o que?
		} finally {
			mgr.close();
		}
		return ret;
	}

	@ApiMethod(name = "getPantry")
	// TODO REMOVE passar a usar o "synchronizationPantry"
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

	@ApiMethod(name = "synchronizationPantry")
	public PantrySynchronizationDTO synchronizationPantry(PantrySynchronizationDTO pantrySynchronizationDTO) {
		EntityManager mgr = getEntityManager();
		PantrySynchronizationDTO ret = null;
		try {
			Pantry pantry = mgr.find(Pantry.class,pantrySynchronizationDTO.getPantryKey());
			if (null == pantrySynchronizationDTO.getListMetaProducts()) throw new RuntimeException();// ERROR REMOVE
			if (null == pantrySynchronizationDTO.getPantryTimeStamp()) throw new RuntimeException();// ERROR REMOVE
			if (null == pantry) throw new RuntimeException();// ERROR REMOVE
			ret = pantry.synchronizationMetaProducts(pantrySynchronizationDTO);
			mgr.persist(pantry);
		} finally {
			mgr.close();
		}
		if (ret == null) throw new RuntimeException();// ERROR REMOVE
		return ret;
	}

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
			for (Product obj : execute)
				;
		} finally {
			mgr.close();
		}
		return CollectionResponse.<Product> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "getProductByBarCode", path = "getProductByBarCode")
	public Product getProductByBarCode(@Named("id") Long id) {
		EntityManager mgr = null;
		List<Product> execute = null;
		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery(
					"SELECT p FROM Product p WHERE p.BarCode=:BarCode")
					.setParameter("BarCode", id);
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

	// ################################# API não usada INTERFACE
	// #################################
	// @ApiMethod(name = "updateUserPantry")
	public UserPantry updateUserPantry(UserPantry userpantry) {
		EntityManager mgr = getEntityManager();
		try {
			if (!mContainsUserPantry(mgr, userpantry)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(userpantry);
		} finally {
			mgr.close();
		}
		return userpantry;
	}

	// @ApiMethod(name = "removeUserPantry")
	public void removeUserPantry(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			UserPantry userpantry = mgr.find(UserPantry.class, id);
			mgr.remove(userpantry);
		} finally {
			mgr.close();
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	// @ApiMethod(name = "listPantry")
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
			// accomodate for lazy fetch.
			for (Pantry obj : execute)
				;
		} finally {
			mgr.close();
		}
		return CollectionResponse.<Pantry> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	// @ApiMethod(name = "removePantry")
	public void removePantry(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			Pantry pantry = mgr.find(Pantry.class, id);
			mgr.remove(pantry);
		} finally {
			mgr.close();
		}
	}

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

	// ################################# SUPORT
	// #################################
	private User mGetUserByEmail(EntityManager mgr, String userEmail) {
		User user = null;
		Query query = mgr.createQuery(
				"SELECT u FROM User u WHERE u.email=:email").setParameter(
				"email", userEmail);
		query.setFirstResult(0);
		@SuppressWarnings("unchecked")
		List<User> users = query.getResultList();
		if (users.size() > 0) {
			user = users.get(0);

			if (user.getUserPantriesList() == null)
				user.setUserPantriesList(new ArrayList<UserPantry>()); // FIXME
																		// Não
																		// gosto
			else
				for (UserPantry iii : user.getUserPantriesList()) {
					for (MetaProduct p : iii.getPantry().getMetaProducts())
						; // FIXME para carregar !!!
					//iii.getUser();
				}
		}
		return user;
	}

	private boolean mContainsUser(EntityManager mgr, User user) {
		boolean contains = true;
		if (user == null) {
			return false;
		}
		User item = mGetUserByEmail(mgr, user.getEmail());
		if (item == null) {
			contains = false;
		}
		return contains;
	}

	private boolean mContainsUserPantry(EntityManager mgr, UserPantry userpantry) {
		if (userpantry == null || userpantry.getKey() == null) {
			return false;
		}
		UserPantry item = mFindUserPantryByIds(mgr, userpantry);
		if (item == null) {
			return false;
		}
		return true;
	}

	@SuppressWarnings({ "unchecked", "unused" })
	private UserPantry mFindUserPantryByIds(EntityManager mgr,
			UserPantry userpantry) {
		List<UserPantry> execute = null;
		Query query = mgr
				.createQuery("select up from UserPantry up WHERE up.pantry=:pantry AND up.userkey=:userkey")
				.setParameter("userkey", userpantry.getUserKey())
				.setParameter("pantry", userpantry.getPantry());
		query.setFirstResult(0);
		execute = query.getResultList();
		if (execute.size() > 0)
			return execute.get(0);
		return null;
	}

	private void mInsertPantry(Pantry pantry) { // EntityManager mgr, FIXME
												// TransactionOptions.Builder.allowMultipleEntityGroups(true)
		EntityManager mgr = getEntityManager();
		try {
			if (mContainsPantry(mgr, pantry)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(pantry);
		} finally {
			mgr.close();
		}
	}

	private boolean mContainsPantry(EntityManager mgr, Pantry pantry) {
		if (pantry == null || pantry.getKey() == null) {
			return false;
		}
		Pantry item = mgr.find(Pantry.class, pantry.getKey());
		if (item == null) {
			return false;
		}
		return true;
	}

	private boolean mContainsProduct(EntityManager mgr, Product product) {
		if (product == null || product.getKey() == null) {
			return false;
		}
		Product item = mgr.find(Product.class, product.getKey());
		if (item == null) {
			return false;
		}
		return true;
	}

	// ################################# USER #################################
	// ################################# USER PANTRY
	// #################################
	// ################################# PANTRY
	// #################################
	// ################################# PRODUCT
	// #################################
	// ################################# META PRODUCT
	// #################################
	// /**
	// * This method lists all the entities inserted in datastore. It uses HTTP
	// GET method and paging support.
	// *
	// * @return A CollectionResponse class containing the list of all entities
	// persisted and a cursor to the next page.
	// */
	// @SuppressWarnings({ "unchecked", "unused" })
	// @ApiMethod(name = "listMetaProduct")
	// public CollectionResponse<MetaProduct> listMetaProduct(
	// @Nullable @Named("cursor") String cursorString,
	// @Nullable @Named("limit") Integer limit) {
	// EntityManager mgr = null;
	// Cursor cursor = null;
	// List<MetaProduct> execute = null;
	// try {
	// mgr = getEntityManager();
	// Query query = mgr
	// .createQuery("select from MetaProduct as MetaProduct");
	// if (cursorString != null && cursorString != "") {
	// cursor = Cursor.fromWebSafeString(cursorString);
	// query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
	// }
	// if (limit != null) {
	// query.setFirstResult(0);
	// query.setMaxResults(limit);
	// }
	// execute = (List<MetaProduct>) query.getResultList();
	// cursor = JPACursorHelper.getCursor(execute);
	// if (cursor != null)
	// cursorString = cursor.toWebSafeString();
	// // Tight loop for fetching all entities from datastore and
	// // accomodate
	// // for lazy fetch.
	// for (MetaProduct obj : execute);
	// } finally {
	// mgr.close();
	// }
	// return CollectionResponse.<MetaProduct> builder().setItems(execute)
	// .setNextPageToken(cursorString).build();
	// }
	// /**
	// * This method gets the entity having primary key id. It uses HTTP GET
	// method.
	// *
	// * @param id
	// * the primary key of the java bean.
	// * @return The entity with primary key id.
	// */
	// @ApiMethod(name = "getMetaProduct")
	// public MetaProduct getMetaProduct(@Named("id") Long id) {
	// EntityManager mgr = getEntityManager();
	// MetaProduct metaproduct = null;
	// try {
	// metaproduct = mgr.find(MetaProduct.class, id);
	// } finally {
	// mgr.close();
	// }
	// return metaproduct;
	// }
	// /**
	// * This inserts a new entity into App Engine datastore. If the entity
	// already exists in the datastore, an exception is thrown. It uses HTTP
	// POST method.
	// *
	// * @param metaproduct
	// * the entity to be inserted.
	// * @return The inserted entity.
	// */
	// @ApiMethod(name = "insertMetaProduct")
	// public MetaProduct insertMetaProduct(MetaProduct metaproduct) {
	// EntityManager mgr = getEntityManager();
	// try {
	// if (containsMetaProduct(metaproduct)) {
	// metaproduct.setAmount(metaproduct.getAmount() + 1);
	// updateMetaProduct(metaproduct);
	// return metaproduct;
	// }
	// mgr.persist(metaproduct);
	// } finally {
	// mgr.close();
	// }
	// return metaproduct;
	// }
	// /**
	// * This method is used for updating an existing entity. If the entity does
	// not exist in the datastore, an exception is thrown. It uses HTTP PUT
	// method.
	// *
	// * @param metaproduct
	// * the entity to be updated.
	// * @return The updated entity.
	// */
	// @ApiMethod(name = "updateMetaProduct")
	// public MetaProduct updateMetaProduct(MetaProduct metaproduct) {
	// EntityManager mgr = getEntityManager();
	// try {
	// if (!containsMetaProduct(metaproduct)) {
	// throw new EntityNotFoundException("Object does not exist");
	// }
	// mgr.persist(metaproduct);
	// } finally {
	// mgr.close();
	// }
	// return metaproduct;
	// }
	// /**
	// * This method removes the entity with primary key id. It uses HTTP DELETE
	// method.
	// *
	// * @param id
	// * the primary key of the entity to be deleted.
	// */
	// @ApiMethod(name = "removeMetaProduct")
	// public void removeMetaProduct(@Named("id") Long id) {
	// EntityManager mgr = getEntityManager();
	// try {
	// MetaProduct metaproduct = mgr.find(MetaProduct.class, id);
	// mgr.remove(metaproduct);
	// } finally {
	// mgr.close();
	// }
	// }
	// private boolean containsMetaProduct(MetaProduct metaproduct) {
	// EntityManager mgr = getEntityManager();
	// boolean contains = true;
	// try {
	// if (metaproduct == null)
	// return false;
	// MetaProduct item = containsByProduct(metaproduct);
	// if (item == null) {
	// contains = false;
	// }
	// } finally {
	// mgr.close();
	// }
	// return contains;
	// }
	// @SuppressWarnings({ "unused", "unchecked" })
	// private MetaProduct containsByProduct(MetaProduct metaproduct) {
	// EntityManager mgr = null;
	// List<MetaProduct> execute = null;
	// try {
	// mgr = getEntityManager();
	// Query query = mgr.createQuery(
	// "SELECT m FROM MetaProduct m WHERE m.product=:product")
	// .setParameter("product", metaproduct.getProduct());
	// query.setFirstResult(0);
	// execute = query.getResultList();
	// } finally {
	// mgr.close();
	// }
	// if (execute.size() > 0)
	// return execute.get(0);
	// return null;
	// }
}