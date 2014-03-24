package epic.easystock;

import epic.easystock.PMF;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Named;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@Api(name = "itemjdoendpoint", namespace = @ApiNamespace(ownerDomain = "easystock.epic", ownerName = "easystock.epic", packagePath = ""))
public class ItemJDOEndpoint {

	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@ApiMethod(name = "listItemJDO")
	public CollectionResponse<ItemJDO> listItemJDO(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit) {

		PersistenceManager mgr = null;
		Cursor cursor = null;
		List<ItemJDO> execute = null;

		try {
			mgr = getPersistenceManager();
			Query query = mgr.newQuery(ItemJDO.class);
			if (cursorString != null && cursorString != "") {
				cursor = Cursor.fromWebSafeString(cursorString);
				HashMap<String, Object> extensionMap = new HashMap<String, Object>();
				extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
				query.setExtensions(extensionMap);
			}

			if (limit != null) {
				query.setRange(0, limit);
			}

			execute = (List<ItemJDO>) query.execute();
			cursor = JDOCursorHelper.getCursor(execute);
			if (cursor != null)
				cursorString = cursor.toWebSafeString();

			// Tight loop for fetching all entities from datastore and accomodate
			// for lazy fetch.
			for (ItemJDO obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<ItemJDO> builder().setItems(execute)
				.setNextPageToken(cursorString).build();
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name = "getItemJDO")
	public ItemJDO getItemJDO(@Named("id") String id) {
		PersistenceManager mgr = getPersistenceManager();
		ItemJDO itemjdo = null;
		try {
			itemjdo = mgr.getObjectById(ItemJDO.class, id);
		} finally {
			mgr.close();
		}
		return itemjdo;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param itemjdo the entity to be inserted.
	 * @return The inserted entity.
	 */
	@ApiMethod(name = "insertItemJDO")
	public ItemJDO insertItemJDO(ItemJDO itemjdo) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (containsItemJDO(itemjdo)) {
				throw new EntityExistsException("Object already exists");
			}
			mgr.makePersistent(itemjdo);
		} finally {
			mgr.close();
		}
		return itemjdo;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param itemjdo the entity to be updated.
	 * @return The updated entity.
	 */
	@ApiMethod(name = "updateItemJDO")
	public ItemJDO updateItemJDO(ItemJDO itemjdo) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			if (!containsItemJDO(itemjdo)) {
				throw new EntityNotFoundException("Object does not exist");
			}
			mgr.makePersistent(itemjdo);
		} finally {
			mgr.close();
		}
		return itemjdo;
	}

	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name = "removeItemJDO")
	public void removeItemJDO(@Named("id") String id) {
		PersistenceManager mgr = getPersistenceManager();
		try {
			ItemJDO itemjdo = mgr.getObjectById(ItemJDO.class, id);
			mgr.deletePersistent(itemjdo);
		} finally {
			mgr.close();
		}
	}

	private boolean containsItemJDO(ItemJDO itemjdo) {
		PersistenceManager mgr = getPersistenceManager();
		boolean contains = true;
		try {
			mgr.getObjectById(ItemJDO.class, itemjdo.getName());
		} catch (javax.jdo.JDOObjectNotFoundException ex) {
			contains = false;
		} finally {
			mgr.close();
		}
		return contains;
	}

	private static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
