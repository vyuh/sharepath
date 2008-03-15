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
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentUris;
import android.content.UriMatcher;
import android.content.ContentValues;
import android.database.sqlite.SQLiteQueryBuilder;
import android.content.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class SharePathProvider extends ContentProvider {

	private SQLiteDatabase mDB;

	private static final String TAG = "SharePathProvider";
	private static final String DATABASE_NAME = "sharepath.db";
	private static final int DATABASE_VERSION = 9;


	private static final int MESSAGE = 1;
	private static final int MESSAGE_ID = 2;
	private static final int BUDDY = 3;
	private static final int BUDDY_ID = 4;

	private static final UriMatcher URL_MATCHER;

	static {
		URL_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URL_MATCHER.addURI("org.yexing.android.sharepath.domain.SharePath",
				"message", MESSAGE);
		URL_MATCHER.addURI("org.yexing.android.sharepath.domain.SharePath",
				"message/#", MESSAGE_ID);

		URL_MATCHER.addURI("org.yexing.android.sharepath.domain.SharePath",
				"buddy", BUDDY);
		URL_MATCHER.addURI("org.yexing.android.sharepath.domain.SharePath",
				"buddy/#", BUDDY_ID);

	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper {

		@Override
		public void onCreate(SQLiteDatabase db) {
			db
			.execSQL("CREATE TABLE message (_id INTEGER PRIMARY KEY,"
					+ "_type INTEGER," + "_from_person TEXT," + "_to_person TEXT,"
					+ "_from TEXT," + "_to TEXT,"
					+ "_date INTEGER," + "_sent INTEGER,"
					+ "_read INTEGER," + "_level INTEGER,"
					+ "_center TEXT," + "_path TEXT" + ");");
			db
			.execSQL("CREATE TABLE buddy (_id INTEGER PRIMARY KEY,"
					+ "_email TEXT," + "_name TEXT," + "_point INTEGER" + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS message");
			db.execSQL("DROP TABLE IF EXISTS buddy");
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
	public Cursor query(Uri url, String[] projection, String selection,
			String[] selectionArgs, String sort) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String orderBy;

		switch (URL_MATCHER.match(url)) {
		case MESSAGE:
			qb.setTables("message");
			qb.setProjectionMap(SharePath.Message.MESSAGE_LIST_PROJECTION_MAP);
			if (TextUtils.isEmpty(sort)) {
				orderBy = SharePath.Message.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sort;
			}
			break;

		case MESSAGE_ID:
			qb.setTables("message");
			qb.appendWhere("_id=" + url.getLastPathSegment());
			if (TextUtils.isEmpty(sort)) {
				orderBy = SharePath.Message.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sort;
			}
			break;

		case BUDDY:
			qb.setTables("buddy");
			qb.setProjectionMap(SharePath.Buddy.BUDDY_LIST_PROJECTION_MAP);
			if (TextUtils.isEmpty(sort)) {
				orderBy = SharePath.Buddy.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sort;
			}
			break;

		case BUDDY_ID:
			qb.setTables("buddy");
			qb.appendWhere("_id=" + url.getLastPathSegment());
			if (TextUtils.isEmpty(sort)) {
				orderBy = SharePath.Buddy.DEFAULT_SORT_ORDER;
			} else {
				orderBy = sort;
			}
			break;

		default:
			throw new IllegalArgumentException("Unknown URL " + url);
		}


		Cursor c = qb.query(mDB, projection, selection, selectionArgs, null,
				null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	@Override
	public String getType(Uri url) {
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
	public Uri insert(Uri url, ContentValues initialValues) {
		long rowID;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}

		switch(URL_MATCHER.match(url)) {
		case MESSAGE:
			rowID = mDB.insert("message", "_from", values);
			if (rowID > 0) {
				Uri uri = ContentUris.appendId(SharePath.Message.CONTENT_URI.buildUpon(), rowID).build();
				getContext().getContentResolver().notifyChange(uri, null);
				return uri;
			}
			break;
		case BUDDY:
			rowID = mDB.insert("buddy", "_name", values);
			if (rowID > 0) {
				Uri uri = ContentUris.appendId(SharePath.Buddy.CONTENT_URI.buildUpon(), rowID).build();
				getContext().getContentResolver().notifyChange(uri, null);
				return uri;
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URL " + url);
				
		}

//		Long now = Long.valueOf(System.currentTimeMillis());
//		Resources r = Resources.getSystem();

		// Make sure that the fields are all set
		// if (values.containsKey(SharePath.Message.DATE) == false) {
		// values.put(SharePath.Message.DATE, now);
		// }

//		if (values.containsKey(SharePath.Message.FROM) == false) {
//			values.put(SharePath.Message.FROM, "unknow");
//		}


		throw new SQLException("Failed to insert row into " + url);
	}

	@Override
	public int delete(Uri url, String where, String[] whereArgs) {
		int count;
		switch (URL_MATCHER.match(url)) {
		case MESSAGE:
			count = mDB.delete("message", where, whereArgs);
			break;

		case MESSAGE_ID:
			String segment = url.getLastPathSegment();
			count = mDB.delete("message",
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
	public int update(Uri url, ContentValues values, String where,
			String[] whereArgs) {
		int count;
		switch (URL_MATCHER.match(url)) {
		case MESSAGE:
			count = mDB.update("message", values, where, whereArgs);
			break;

		case MESSAGE_ID:
			String segment = url.getLastPathSegment();
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

}
