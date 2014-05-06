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
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;

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
public class UserBdAdapter {
	public static final String TABLE_NAME = "users";
	public static final String USER = "user_id";
	public static final String PANTRY_ID = "pantry_id";
	public static final String PANTRY_NAME = "pantry_name";
	private static final String TAG = "NotesDbAdapter";
	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = TABLE_NAME;
	private static final int DATABASE_VERSION = 1;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table " + TABLE_NAME
	+ " (" + PANTRY_NAME + " text not null, " + USER + " text not null ," + PANTRY_ID + " bigint not null, "
	+ "primary key(" + PANTRY_ID + ", " + USER + "));";
	
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
	public UserBdAdapter(Context ctx) {
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
	public UserBdAdapter open() throws SQLException {
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
	 */
	public long createPantry(String user, long pantryID ,String pantryName) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(USER, user);
		initialValues.put(PANTRY_ID, pantryID);
		initialValues.put(PANTRY_NAME, pantryName);
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId
	 *            id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deletePantry(String user, String pantry_id) {
		return mDb.delete(DATABASE_TABLE, PANTRY_ID + "=" + pantry_id + "and" +
		USER + "=" + user, null) > 0;
	}
	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllPantry() {
		return mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
	}
	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public List<UserPantryAux> getAllPantry() {
		List<UserPantryAux> users_pantrys = new ArrayList<UserPantryAux>();
		Cursor cursor = fetchAllPantry();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			UserPantryAux prod = cursorToUserPantry(cursor);
			users_pantrys.add(prod);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return users_pantrys;
	}
	private/*FIXME public*/ List<UserPantryAux> avalablePantrysFromUser(String user) {
		List<UserPantryAux> list = getAllPantry();
		List<UserPantryAux> ret = new ArrayList<UserPantryAux>();
		for (UserPantryAux el : list) {
			if (el.user.equalsIgnoreCase(user))  {
				ret.add(el);
			}
		}
		return ret;
	}
	public String[] avalablePantrysNamesFromUser(String user) {
		List<UserBdAdapter.UserPantryAux> aux = avalablePantrysFromUser(user);
		String[] pantreisName = new String[aux.size()];
		int i = 0;
		for (UserBdAdapter.UserPantryAux el : aux) {
			pantreisName[i]= el.pantryName;
			i++;
		}
		return pantreisName;
	}
	
	static public class UserPantryAux{
		public String user;
		public long pantryID;
		public String pantryName;
		public UserPantryAux(String user, long pantryID, String pantryName) {
			super();
			this.user = user;
			this.pantryID = pantryID;
			this.pantryName = pantryName;
		}
		
	}
	private UserPantryAux cursorToUserPantry(Cursor cursor) {
		String user = cursor.getString(cursor.getColumnIndex(USER));
		long pantryID = cursor.getLong(cursor.getColumnIndex(PANTRY_ID));
		String pantryName = cursor.getString(cursor.getColumnIndex(PANTRY_NAME));
		return new UserPantryAux(user, pantryID, pantryName);
	}
}