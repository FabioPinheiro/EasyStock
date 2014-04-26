package epic.easystock;

import epic.easystock.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.ArrayList;
import java.util.List;

import epic.easystock.Constants;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

//pantryendpoint

@Api(name = "apiEndpoint", namespace = @ApiNamespace(ownerDomain = "easystock.epic", ownerName = "easystock.epic", packagePath = ""), version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = { Constants.WEB_CLIENT_ID /*
																																																								 * ,
																																																								 * Constants
																																																								 * .
																																																								 * ANDROID_CLIENT_ID
																																																								 */}, audiences = { Constants.ANDROID_AUDIENCE })
public class PantryEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
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
			for (Pantry obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Pantry> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
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
				for (MetaProduct p : ret.getProducts()) {
				}

		} finally {
			mgr.close();
		}
		return ret;
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET
	 * method.
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

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
	 * 
	 * @param pantry
	 *            the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertPantry")
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
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
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
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
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

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
