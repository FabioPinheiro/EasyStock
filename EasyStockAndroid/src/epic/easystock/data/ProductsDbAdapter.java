/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package epic.easystock.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import epic.easystock.io.EndPointCall;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations for the notepad example, and gives the ability to list all notes as well as retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the addition of better error handling and also using returning a Cursor instead of using a collection of inner classes (which is less scalable and not recommended).
 */
public class ProductsDbAdapter {
	private final String LOG_TAG = this.getClass().getCanonicalName();
	private static final String DATABASE_NAME = "Protucts_data";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "PantryDbAdapter";
	protected final String DATABASE_TABLE = "products_table";
	protected final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " ("
		+ LocalObject.STR_O_LONG_KEY + " integer primary key, "
		+ LocalObject.STR_O_LONG_TIMESTAMP + " integer not null, "
		+ LocalObject.STR_O_INT_CHANGED + " integer not null, "
		+ LocalObject.STR_P_LONG_BARCODE + " integer not null, "
		+ LocalObject.STR_P_STRING_NAME + " text not null, "
		+ LocalObject.STR_P_STRING_DESCRIPTION + " text not null"
		+ ");";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	private Context mCtx;
	
	private class DatabaseHelper extends SQLiteOpenHelper { // FIXME pq é que isto era static?
		DatabaseHelper(Context context) {
			super(context, DATABASE_CREATE, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}
	
	public ProductsDbAdapter(Context context) {
		this.mCtx = context;
		this.mDbHelper = new DatabaseHelper(mCtx);
	}
	
	private boolean isOpen = false;
	
	/**
	 * Open the notes database. If it cannot be opened, try to create a new instance of the database. If it cannot be created, throw an exception to signal the failure
	 * @return 
	 * @return 
	 * 
	 * @return this (self reference, allowing this to be chained in an initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public ProductsDbAdapter open() throws SQLException {
		if (!isOpen) {
			Log.d(LOG_TAG, EndPointCall.PANTRY_IS_ALREADY_OPEN);
			mDbHelper = new DatabaseHelper(mCtx);
			mDb = mDbHelper.getWritableDatabase();
			isOpen = true;
		}
		return this;
	}
	public void close() {
		isOpen = false;
		mDbHelper.close();
	}
	public long createProduct(LocalProduct localProduct) {
		return mDb.insert(DATABASE_TABLE, null, localProduct.getContentValues());
	}
	public boolean deleteProduct(long rowId) {
		return mDb.delete(DATABASE_TABLE, LocalObject.STR_O_LONG_KEY + "=" + rowId, null) > 0;
	}
	public Cursor fetchAllProduct() {
		return mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
	}
	/*public void putAllProducts(Collection<LocalProduct> products) {
		for (LocalProduct lmp : products) {
			try {
				createProduct(lmp);
			} catch (SQLiteConstraintException e) {
				Log.e("PantryBDAdapter", "error putAllProducts");
				updateProduct(lmp);
			}
		}
	}*/
	public List<LocalProduct> getAllProducts() {
		List<LocalProduct> products = new ArrayList<LocalProduct>();
		Cursor cursor = mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			LocalProduct prod = new LocalProduct(cursor);
			products.add(prod);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return products;
	}
	public Cursor fetchProductByBarCode(long barCode) throws SQLException {
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, null, LocalObject.STR_P_LONG_BARCODE + "=" + barCode, null, null,
		null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor fetchProductByKey(long key) throws SQLException {
		Cursor mCursor = mDb.query(true, DATABASE_TABLE, null, LocalObject.STR_O_LONG_KEY + "=" + key, null, null,
		null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public boolean updateProduct(LocalProduct localProduct) {
		ContentValues aux = localProduct.getContentValues();
		return mDb.update(DATABASE_TABLE, aux, LocalObject.STR_O_LONG_KEY + "=" + localProduct.getKey(), null) > 0;
	}
	public List<LocalProduct> getProductByBarCode(Long barCode) {
		List<LocalProduct> list = new ArrayList<LocalProduct>();
		Cursor cursor = fetchProductByBarCode(barCode);
		for (; !cursor.isAfterLast(); cursor.moveToNext()){
			list.add(new LocalProduct(cursor));
		}
		return list;
	}
	public LocalProduct getProductByKey(long key) {
		Cursor cursor = fetchProductByKey(key);
		if(cursor.isAfterLast()){
			Log.e(LOG_TAG, "The Porduct does not exist key=" + key);
			return null;
		}
		return new LocalProduct(cursor);
	}
	public void synchronizeAllProducts(Collection<LocalProduct> products) {
		for (LocalProduct lmp : products) {
			try {
				
				if (haveProduct(lmp.getKey())){
					updateProduct(lmp); //FIXME pode não ser nessecario
					Log.d(LOG_TAG,"synchronizeAllProducts: updateProduct");
				}else {
					createProduct(lmp);
					Log.d(LOG_TAG,"synchronizeAllProducts: createProduct");
				}
				
			} catch (SQLiteConstraintException e) {
				Log.e(LOG_TAG, "error synchronizeAllProducts"); //TEXT 
			}
		}
	}
	private boolean haveProduct(long key) {
		Cursor cursor = fetchProductByKey(key);
		return cursor.isAfterLast() ? false : true;
	}
}
