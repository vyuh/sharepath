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

import java.util.HashMap;

import org.yexing.android.sharepath.domain.SharePath;

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

public class SharePathProvider extends ContentProvider {

	private SQLiteDatabase mDB;

	private static final String TAG = "SharePathProvider";
	private static final String DATABASE_NAME = "sharepath.db";
	private static final int DATABASE_VERSION = 2;

	private static HashMap<String, String> MESSAGE_LIST_PROJECTION_MAP;

	private static final int MESSAGE = 1;
	private static final int MESSAGE_ID = 2;

	private static final ContentURIParser URL_MATCHER;

	private static class DatabaseHelper extends ContentProviderDatabaseHelper {

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE message (_id INTEGER PRIMARY KEY,"
					+ "_type INTEGER," + "_from TEXT," + "_to TEXT,"
					+ "_date INTEGER," + "_read INTEGER," + "_level INTEGER,"
					+ "_center TEXT," + "_path TEXT" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS message");
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
		case MESSAGE:
			qb.setTables("message");
			qb.setProjectionMap(MESSAGE_LIST_PROJECTION_MAP);
			break;

		case MESSAGE_ID:
			qb.setTables("message");
			qb.appendWhere("_id=" + url.getPathSegment(1));
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		// If no sort order is specified use the default
		String orderBy;
		if (TextUtils.isEmpty(sort)) {
			orderBy = SharePath.Message.DEFAULT_SORT_ORDER;
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
		case MESSAGE:
			return "vnd.android.cursor.dir/vnd.yexing.sharepath.message";

		case MESSAGE_ID:
			return "vnd.android.cursor.item/vnd.yexing.sharepath.message";

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

		if (URL_MATCHER.match(url) != MESSAGE) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		Long now = Long.valueOf(System.currentTimeMillis());
		Resources r = Resources.getSystem();

		// Make sure that the fields are all set
//		if (values.containsKey(SharePath.Message.DATE) == false) {
//			values.put(SharePath.Message.DATE, now);
//		}

		if (values.containsKey(SharePath.Message.FROM) == false) {
			values.put(SharePath.Message.FROM, "unknow");
		}

		
		rowID = mDB.insert("message", "_from", values);
		if (rowID > 0) {
			ContentURI uri = SharePath.Message.CONTENT_URI.addId(rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}

		throw new SQLException("Failed to insert row into " + url);
	}

	@Override
	public int delete(ContentURI url, String where, String[] whereArgs) {
		// int count;
		// long rowId = 0;
		// switch (URL_MATCHER.match(url)) {
		// case NOTES:
		// count = mDB.delete("note_pad", where, whereArgs);
		// break;
		//
		// case NOTE_ID:
		// String segment = url.getPathSegment(1);
		// rowId = Long.parseLong(segment);
		// count = mDB.delete("notes",
		// "_id="
		// + segment
		// + (!TextUtils.isEmpty(where) ? " AND (" + where
		// + ')' : ""), whereArgs);
		// break;
		//
		// default:
		// throw new IllegalArgumentException("Unknown URL " + url);
		// }
		//
		// getContext().getContentResolver().notifyChange(url, null);
		// return count;
		return 0;
	}

	@Override
	public int update(ContentURI url, ContentValues values, String where,
			String[] whereArgs) {
		 int count;
		switch (URL_MATCHER.match(url)) {
		case MESSAGE:
			count = mDB.update("message", values, where, whereArgs);
			break;

		case MESSAGE_ID:
			String segment = url.getPathSegment(1);
			count = mDB.update("message", values,
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
		URL_MATCHER.addURI("org.yexing.android.sharepath.domain.SharePath",
				"message", MESSAGE);
		URL_MATCHER.addURI("org.yexing.android.sharepath.domain.SharePath",
				"message/#", MESSAGE_ID);

		MESSAGE_LIST_PROJECTION_MAP = new HashMap<String, String>();
		MESSAGE_LIST_PROJECTION_MAP.put(SharePath.Message._ID, "_id");
		MESSAGE_LIST_PROJECTION_MAP.put(SharePath.Message.FROM, "_from");

	}
}
