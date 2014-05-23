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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.google.android.gms.internal.ar;
import com.google.api.client.util.DateTime;

import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.apiEndpoint.model.Pantry;
import epic.easystock.apiEndpoint.model.PantrySynchronizationDTO;
import epic.easystock.io.AddProductToLocalPantryTask;
import epic.easystock.io.EndPointCall;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations for the notepad example, and gives the ability to list all notes as well as retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the addition of better error handling and also using returning a Cursor instead of using a collection of inner classes (which is less scalable and not recommended).
 */
public class PantriesDBAdapter {
	private final String LOG_TAG = this.getClass().getCanonicalName();
	private static final String DATABASE_NAME = "pantry_data";
	private static final int DATABASE_VERSION = 1;
	private static Map<Long, PantryDB> openPantries = new TreeMap<Long, PantryDB>();
	private Context mCtx;
	
	public PantriesDBAdapter(Context context) {
		this.mCtx = context;
	}
	public void closeAllPantries(){
		for (PantryDB it : openPantries.values()) {
			it.close();
		}
	}
	public PantryDB getPantryDB(Long pantryKey){//, String pantryName){
		PantryDB aux = openPantries.get(pantryKey);
		if (aux == null){
			aux = new PantryDB(pantryKey, EndPointCall.getPantryNameFromKey(pantryKey));
			aux.open();
			openPantries.put(pantryKey,aux);
		}
		return aux;
	}
	
	//#***********************************************************************************#//
	public class PantryDB {
		protected final String DATABASE_CREATE;
		protected final String DATABASE_TABLE;
		protected boolean isOpen = false;
		protected DatabaseHelper mDbHelper;
		protected SQLiteDatabase mDb;
		private long pantryKey;

		
		public PantryDB(Long pantryKey, String pantryName) { //REMOVE pantryName
			//this.mCtx = context;
			this.pantryKey = pantryKey;
			this.DATABASE_TABLE = "table_" + pantryKey + "_" + pantryName ;
			this.DATABASE_CREATE = m_DATABASE_CREATE();
			this.mDbHelper = new DatabaseHelper(mCtx);
		}
		private String m_DATABASE_CREATE() {
			return "create table " + this.DATABASE_TABLE + " ("
				+ LocalObject.STR_O_LONG_KEY + " integer primary key, "
				+ LocalObject.STR_O_LONG_TIMESTAMP + " integer not null, "
				+ LocalObject.STR_O_INT_CHANGED + " integer not null, "
				+ LocalObject.STR_MP_REAL_AMOUNT + " real not null, "
				+ LocalObject.STR_MP_LONG_PRODUCT_KEY + " integer not null "
				+ ");";
		}
		/**
		 * Open the notes database. If it cannot be opened, try to create a new instance of the database. If it cannot be created, throw an exception to signal the failure
		 * 
		 * @return this (self reference, allowing this to be chained in an initialization call)
		 * @throws SQLException
		 *             if the database could be neither opened or created
		 */
		public PantryDB open() throws SQLException {
			if(!isOpen){
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
		
		private class DatabaseHelper extends SQLiteOpenHelper { // FIXME pq é que isto era static?
			DatabaseHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}
			@Override
			public void onCreate(SQLiteDatabase db) {
				db.execSQL(DATABASE_CREATE);
			}
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
				db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
				onCreate(db);
			}
		}
		
		public long createProduct(LocalMetaProduct localMetaProduct) {
			return mDb.insert(DATABASE_TABLE, null, localMetaProduct.getContentValues());
		}
		public boolean updateProduct(LocalMetaProduct localMetaProduct) {
			ContentValues args = localMetaProduct.getContentValues();
			return mDb.update(DATABASE_TABLE, args, LocalObject.STR_O_LONG_KEY + "=" + localMetaProduct.getKey(), null) > 0;
		}
		public boolean deleteProduct(long rowId) {
			return mDb.delete(DATABASE_TABLE, LocalObject.STR_O_LONG_KEY + "=" + rowId, null) > 0;
		}
		public Cursor fetchAllProduct() {
			return mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
		}
		public void putAllProducts(Collection<LocalMetaProduct> products) {
			for (LocalMetaProduct lmp : products) {
				try {
					createProduct(lmp);
				} catch (SQLiteConstraintException e) {
					Log.e(LOG_TAG, "error putAllProducts");
					updateProduct(lmp);
				}
			}
		}
		public List<LocalMetaProduct> getAllProducts() {
			List<LocalMetaProduct> products = new ArrayList<LocalMetaProduct>();
			Cursor cursor = mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				LocalMetaProduct prod = new LocalMetaProduct(cursor);
				products.add(prod);
				cursor.moveToNext();
			}
			// make sure to close the cursor
			cursor.close();
			return products;
		}
		public boolean haveProduct(long productKey) throws SQLException {
			Cursor mCursor =
			mDb.query(true, DATABASE_TABLE, null, LocalObject.STR_O_LONG_KEY + "=" + productKey, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return !mCursor.isAfterLast();
		}

		public PantrySynchronizationDTO getPantrySynchronizationDTO() {
			List<LocalMetaProduct> listMP = this.getAllProducts();
			List<MetaProduct> aux = LocalMetaProduct.convert(listMP);
			PantrySynchronizationDTO dto = new PantrySynchronizationDTO();
			dto.setListMetaProducts(aux);
			dto.setPantryKey(this.pantryKey);
			dto.setPantryTimeStamp(new DateTime(0));//new Date())); //FIXME ERROR
			if(dto.getPantryKey() == 0 ) throw new RuntimeException(); //FIXME REMOVE ME
			return dto;
		}
		public void synch(PantrySynchronizationDTO result) {
			for (MetaProduct it : result.getListMetaProducts()) {
				LocalMetaProduct aux = new LocalMetaProduct(it);
				if(haveProduct(it.getProductKey())) {
					updateProduct(aux);
				}else{
					createProduct(aux);
				}
			}
		}
	}
}
