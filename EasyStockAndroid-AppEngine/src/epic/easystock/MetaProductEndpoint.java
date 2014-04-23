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

//metaproductendpoint
@Api(name = "apiEndpoint", namespace = @ApiNamespace(ownerDomain = "easystock.epic", ownerName = "easystock.epic", packagePath = ""), version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
		Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })
public class MetaProductEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
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
			for (MetaProduct obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<MetaProduct> builder().setItems(execute)
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
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
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
				throw new EntityExistsException("Object already exists");
			}
			mgr.persist(metaproduct);
		} finally {
			mgr.close();
		}
		return metaproduct;
	}

	/**
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
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
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
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
			if (metaproduct == null || metaproduct.getKey() == null){
				return false;
			}
			MetaProduct item = mgr
					.find(MetaProduct.class, metaproduct.getKey());
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
