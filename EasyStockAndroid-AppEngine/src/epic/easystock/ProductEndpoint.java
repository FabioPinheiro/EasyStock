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

@Api(name = "apiEndpoint", namespace = @ApiNamespace(ownerDomain = "easystock.epic", ownerName = "easystock.epic", packagePath = ""), version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
		Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })
public class ProductEndpoint {

	/**
	 * This method lists all the entities inserted in datastore. It uses HTTP
	 * GET method and paging support.
	 * 
	 * @return A CollectionResponse class containing the list of all entities
	 *         persisted and a cursor to the next page.
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
			for (Product obj : execute)
				;
		} finally {
			mgr.close();
		}

		return CollectionResponse.<Product> builder().setItems(execute)
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

	/**
	 * This inserts a new entity into App Engine datastore. If the entity
	 * already exists in the datastore, an exception is thrown. It uses HTTP
	 * POST method.
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
	 * This method is used for updating an existing entity. If the entity does
	 * not exist in the datastore, an exception is thrown. It uses HTTP PUT
	 * method.
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
	 * This method removes the entity with primary key id. It uses HTTP DELETE
	 * method.
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
			if(product == null || product.getKey() == null){
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

	private static EntityManager getEntityManager() {
		return EMF.get().createEntityManager();
	}

}
