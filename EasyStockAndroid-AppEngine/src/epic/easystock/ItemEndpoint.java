package epic.easystock;

import epic.easystock.EMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JPACursorHelper;

import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Api(name = "itemendpoint", namespace = @ApiNamespace(ownerDomain = "easystock.epic", ownerName = "easystock.epic", packagePath = ""))
public class ItemEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listItem")
	public CollectionResponse<ItemData> listItem(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		EntityManager mgr = null;
		Cursor cursor = null;
		List<ItemData> execute = null;

		try {
			mgr = getEntityManager();
			Query query = mgr.createQuery("select from Item as Item");
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				query.setHint(JPACursorHelper.CURSOR_HINT, cursor);
			}

			if (limit != null) {
				query.setFirstResult(0);
				query.setMaxResults(limit);
			}

			execute = (List<ItemData>) query.getResultList();
			cursor = JPACursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (ItemData obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<ItemData> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getItem")
	public ItemData getItem(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		ItemData item = null;
		try {
			item = mgr.find(ItemData.class, id);
		} finally {
			mgr.close();
		}
		return item;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param item the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertItem")
	public ItemData insertItem(@Named("item") ItemData item) {
		EntityManager mgr = getEntityManager();
		try {
			if (containsItem(item)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(item);
		} finally {
			mgr.close();
		}
		return item;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param item the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateItem")
	public ItemData updateItem(@Named("item") ItemData item) {
		EntityManager mgr = getEntityManager();
		try {
			if (!containsItem(item)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.persist(item);
		} finally {
			mgr.close();
		}
		return item;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeItem")
	public void removeItem(@Named("id") Long id) {
		EntityManager mgr = getEntityManager();
		try {
			ItemData item = mgr.find(ItemData.class, id);
			mgr.remove(item);
		} finally {
			mgr.close();
		}
	}

	private boolean containsItem(ItemData item) {
		EntityManager mgr = getEntityManager();
		boolean contains = true;
		try {
			ItemData item1 = mgr.find(ItemData.class, item.getKey());
			if (item1 == null) {
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
