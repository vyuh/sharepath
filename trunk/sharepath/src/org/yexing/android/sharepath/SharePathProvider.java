/* 
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yexing.android.sharepath;

import android.content.ContentProvider;
import android.content.ContentProviderDatabaseHelper;
import android.content.ContentURIParser;
import android.content.ContentValues;
import android.content.QueryBuilder;
import android.content.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ContentURI;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;

public class SharePathProvider extends ContentProvider {

	private SQLiteDatabase mDB;

	private static final String TAG = "SharePathProvider";
	private static final String DATABASE_NAME = "sharepath.db";
	private static final int DATABASE_VERSION = 1;

	private static HashMap<String, String> NOTES_LIST_PROJECTION_MAP;

	private static final int NOTES = 1;
	private static final int NOTE_ID = 2;

	private static final ContentURIParser URL_MATCHER;

	private static class DatabaseHelper extends ContentProviderDatabaseHelper {

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE notes (_id INTEGER PRIMARY KEY,"
					+ "title TEXT," + "note TEXT," + "created INTEGER,"
					+ "modified INTEGER" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	@Override
	public boolean onCreate() {
		DatabaseHelper dbHelper = new DatabaseHelper();
		mDB = dbHelper.openDatabase(getContext(), DATABASE_NAME, null,
				DATABASE_VERSION);
		return (mDB == null) ? false : true;
	}

	@Override
	public Cursor query(ContentURI url, String[] projection, String selection,
			String[] selectionArgs, String groupBy, String having, String sort) {
		QueryBuilder qb = new QueryBuilder();

		switch (URL_MATCHER.match(url)) {
		case NOTES:
			qb.setTables("notes");
			qb.setProjectionMap(NOTES_LIST_PROJECTION_MAP);
			break;

		case NOTE_ID:
			qb.setTables("notes");
			qb.appendWhere("_id=" + url.getPathSegment(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sort)) {
			orderBy = NotePad.Notes.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sort;
		}

		Cursor c = qb.query(mDB, projection, selection, selectionArgs, groupBy,
				having, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	@Override
	public String getType(ContentURI url) {
		switch (URL_MATCHER.match(url)) {
		case NOTES:
			return "vnd.android.cursor.dir/vnd.google.note";

		case NOTE_ID:
			return "vnd.android.cursor.item/vnd.google.note";

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}
	}

	@Override
	public ContentURI insert(ContentURI url, ContentValues initialValues) {
		long rowID;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		if (URL_MATCHER.match(url) != NOTES) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		Long now = Long.valueOf(System.currentTimeMillis());
		Resources r = Resources.getSystem();

		// Make sure that the fields are all set
		if (values.containsKey(NotePad.Notes.CREATED_DATE) == false) {
			values.put(NotePad.Notes.CREATED_DATE, now);
		}

		if (values.containsKey(NotePad.Notes.MODIFIED_DATE) == false) {
			values.put(NotePad.Notes.MODIFIED_DATE, now);
		}

		if (values.containsKey(NotePad.Notes.TITLE) == false) {
			values.put(NotePad.Notes.TITLE, r
					.getString(android.R.string.untitled));
		}

		if (values.containsKey(NotePad.Notes.NOTE) == false) {
			values.put(NotePad.Notes.NOTE, "");
		}

		rowID = mDB.insert("notes", "note", values);
		if (rowID > 0) {
			ContentURI uri = NotePad.Notes.CONTENT_URI.addId(rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}

		throw new SQLException("Failed to insert row into " + url);
	}

	@Override
	public int delete(ContentURI url, String where, String[] whereArgs) {
		int count;
		long rowId = 0;
		switch (URL_MATCHER.match(url)) {
		case NOTES:
			count = mDB.delete("note_pad", where, whereArgs);
			break;

		case NOTE_ID:
			String segment = url.getPathSegment(1);
			rowId = Long.parseLong(segment);
			count = mDB.delete("notes",
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	@Override
	public int update(ContentURI url, ContentValues values, String where,
			String[] whereArgs) {
		int count;
		switch (URL_MATCHER.match(url)) {
		case NOTES:
			count = mDB.update("notes", values, where, whereArgs);
			break;

		case NOTE_ID:
			String segment = url.getPathSegment(1);
			count = mDB.update("notes", values,
					"_id="
							+ segment
							+ (!TextUtils.isEmpty(where) ? " AND (" + where
									+ ')' : ""), whereArgs);
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	static {
		URL_MATCHER = new ContentURIParser(ContentURIParser.NO_MATCH);
		URL_MATCHER.addURI("com.google.provider.NotePad", "notes", NOTES);
		URL_MATCHER.addURI("com.google.provider.NotePad", "notes/#", NOTE_ID);

		NOTES_LIST_PROJECTION_MAP = new HashMap<String, String>();
		NOTES_LIST_PROJECTION_MAP.put(NotePad.Notes._ID, "_id");
		NOTES_LIST_PROJECTION_MAP.put(NotePad.Notes.TITLE, "title");
		NOTES_LIST_PROJECTION_MAP.put(NotePad.Notes.NOTE, "note");
		NOTES_LIST_PROJECTION_MAP.put(NotePad.Notes.CREATED_DATE, "created");
		NOTES_LIST_PROJECTION_MAP.put(NotePad.Notes.MODIFIED_DATE, "modified");
	}
}
