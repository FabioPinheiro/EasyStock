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

package epic.easystock.assist;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.internal.ar;

import epic.easystock.apiEndpoint.model.MetaProduct;
import epic.easystock.data.LocalMetaProduct;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Simple notes database access helper class. Defines the basic CRUD operations
 * for the notepad example, and gives the ability to list all notes as well as
 * retrieve or modify a specific note.
 * 
 * This has been improved from the first version of this tutorial through the
 * addition of better error handling and also using returning a Cursor instead
 * of using a collection of inner classes (which is less scalable and not
 * recommended).
 */
public class PantryDbAdapter {
	public static final String TABLE_NAME = "pantry";
	public static final String PROD_NAME = "name";
	public static final String PROD_DESCRIPTION = "description";
	public static final String PROD_ID = "_id";
	public static final String PROD_BARCODE = "BarCode";
	public static final String PROD_AMOUNT = "amount";

	private static final String TAG = "NotesDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table " + TABLE_NAME
			+ " (" + PROD_ID + " integer primary key, " + PROD_BARCODE
			+ " integer, " + PROD_NAME + " text not null, " + PROD_DESCRIPTION
			+ " text not null," + PROD_AMOUNT + " real not null );";

	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = TABLE_NAME;
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

	/**
	 * Constructor - takes the context to allow the database to be
	 * opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public PantryDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public PantryDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new note using the title and body provided. If the note is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param barcode
	 * @param id
	 * 
	 * @param title
	 *            the title of the note
	 * @param body
	 *            the body of the note
	 * @return rowId or -1 if failed
	 */
	public long createProduct(String name, String description, Long barcode,
			Long id, Double amount) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(PROD_NAME, name);
		initialValues.put(PROD_DESCRIPTION, description);
		initialValues.put(PROD_BARCODE, barcode);
		initialValues.put(PROD_ID, id);
		initialValues.put(PROD_AMOUNT, amount);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId
	 *            id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteNote(long rowId) {

		return mDb.delete(DATABASE_TABLE, PROD_ID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllNotes() {

		return mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public void putAllProducts(List<LocalMetaProduct> products) {

		for (LocalMetaProduct lmp : products) {
			try {
				createProduct(lmp.getName(), lmp.getDescription(),
						lmp.getBarCode(), lmp.getId(), lmp.getAmount());
			} catch (SQLiteConstraintException e) {
				updateProduct(lmp.getName(), lmp.getDescription(),
						lmp.getBarCode(), lmp.getId(), lmp.getAmount()+1);
			}
		}

	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public List<LocalMetaProduct> getAllProducts() {
		List<LocalMetaProduct> products = new ArrayList<LocalMetaProduct>();

		Cursor cursor = mDb.query(DATABASE_TABLE, null, null, null, null, null,
				null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			LocalMetaProduct prod = cursorToMetaProduct(cursor);
			products.add(prod);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();

		return products;
	}

	private LocalMetaProduct cursorToMetaProduct(Cursor cursor) {
		Long id = cursor.getLong(cursor.getColumnIndex(PROD_ID));
		String name = cursor.getString(cursor.getColumnIndex(PROD_NAME));
		Long barCode = cursor.getLong(cursor.getColumnIndex(PROD_BARCODE));
		String desc = cursor.getString(cursor.getColumnIndex(PROD_DESCRIPTION));
		Double amount = cursor.getDouble(cursor.getColumnIndex(PROD_AMOUNT));
		LocalMetaProduct prod = new LocalMetaProduct(barCode, name, desc, id,
				amount);
		return prod;
	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param id
	 *            id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException
	 *             if note could not be found/retrieved
	 */
	public Cursor fetchNote(long id) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, null, PROD_ID + "=" + id, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Update the note using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param id
	 *            id of note to update
	 * @param amount
	 * @param title
	 *            value to set note title to
	 * @param body
	 *            value to set note body to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateProduct(String name, String description, Long barcode,
			Long id, Double amount) {
		ContentValues args = new ContentValues();
		args.put(PROD_NAME, name);
		args.put(PROD_DESCRIPTION, description);
		args.put(PROD_BARCODE, barcode);
		args.put(PROD_ID, id);
		args.put(PROD_AMOUNT, amount);
		return mDb.update(DATABASE_TABLE, args, PROD_ID + "=" + id, null) > 0;

	}
}
