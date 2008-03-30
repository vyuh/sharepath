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

import org.yexing.android.sharepath.domain.Domain;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.xml.dom.InnerNodeImpl;

public class SharePathService extends Service {
	private static final String LOG_TAG = "SharePath";

	public static final int ACTION_SEARCH_NEW_REQUEST = 1;
	public static final int ACTION_NEW_REQUEST = ACTION_SEARCH_NEW_REQUEST + 1;
	public static final int ACTION_NEW_MYMAP = ACTION_NEW_REQUEST + 1;
	public static final int ACTION_EDIT_MYMAP = ACTION_NEW_MYMAP + 1;
	public static final int ACTION_DELETE_REQUEST = ACTION_EDIT_MYMAP + 1;
	public static final int ACTION_DELETE_ALL_REQUEST = ACTION_DELETE_REQUEST + 1;
	public static final int ACTION_DELETE_BUDDY = ACTION_DELETE_ALL_REQUEST + 1;
	public static final int ACTION_DELETE_ALL_BUDDIES = ACTION_DELETE_BUDDY + 1;
	public static final int ACTION_NEW_BUDDY = ACTION_DELETE_ALL_BUDDIES + 1;
	public static final int ACTION_EDIT_BUDDY = ACTION_NEW_BUDDY + 1;	
	public static final int ACTION_DELETE_MYMAP = ACTION_EDIT_BUDDY + 1;	
	public static final int ACTION_DELETE_ALL_MYMAPS = ACTION_DELETE_MYMAP + 1;	
	public static final int ACTION_READ_MESSAGE = ACTION_DELETE_ALL_MYMAPS + 1;	

	@Override
	protected void onStart(int startId, Bundle bundle) {
		// TODO Auto-generated method stub
		super.onStart(startId, bundle);
		int action = bundle.getInt("action");
		Log.v(LOG_TAG, "service start:" + action);
		if (action == 0) {
			return;
		}
		switch (action) {
		case SharePathService.ACTION_SEARCH_NEW_REQUEST:
		{
			Cursor cursor = getContentResolver().query(Domain.Message.CONTENT_URI,
					null, Domain.Message.READ + "=0 and " 
					+ Domain.Message.TYPE + "<2", null, null);
			Log.v(LOG_TAG, "islast:" + cursor.isLast());
			Log.v(LOG_TAG, "count 1:" + cursor.count());
			cursor.first();
			Log.v(LOG_TAG, "count 2:" + cursor.count());
			if(cursor.count() > 0) {
				showNotification(getString(R.string.msg_not_read));
			} else {
				cleanNotification();
			}
			break;
		}
		case SharePathService.ACTION_READ_MESSAGE:
		{
			Cursor cursor = getContentResolver().query(Domain.Message.CONTENT_URI,
					null, Domain.Message.READ + "=0 and " 
					+ Domain.Message.TYPE + "<2", null, null);
			if(cursor.count() == 0) {
				cleanNotification();
			}
			break;
		}
		case SharePathService.ACTION_NEW_REQUEST:
		{
			ContentValues cv = createMessageValue(bundle);
			getContentResolver().insert(Domain.Message.CONTENT_URI, cv);
			showNotification(bundle.getString(Domain.Message.FROM));
		}
			break;
		case SharePathService.ACTION_DELETE_REQUEST:
			getContentResolver().delete(Domain.Message.CONTENT_URI,
					"_id=" + bundle.getLong(Domain.Message._ID), null);
			break;
		case SharePathService.ACTION_NEW_MYMAP:
		{
			ContentValues cv = createMessageValue(bundle);
//			cv.put(Domain.Message.TYPE, 3);
			getContentResolver().insert(Domain.Message.CONTENT_URI, cv);
//			showNotification(bundle.getString(Domain.Message.FROM));
		}
			break;
		case SharePathService.ACTION_EDIT_MYMAP:
		{
			Log.i(LOG_TAG, "ACTION_EDIT_MAP");
			Long id = bundle.getLong(Domain.Message._ID);
			ContentValues cv = createMessageValue(bundle);
			cv.put(Domain.Message.TYPE, 3);
			getContentResolver().update(Domain.Message.CONTENT_URI, cv, 
					"_id=" + id, null);
//			showNotification(bundle.getString(Domain.Message.FROM));
		}
			break;
		case SharePathService.ACTION_DELETE_ALL_REQUEST:
			getContentResolver().delete(Domain.Message.CONTENT_URI,
					"_type=0", null);
			break;
		case SharePathService.ACTION_DELETE_BUDDY:
			getContentResolver().delete(Domain.Buddy.CONTENT_URI,
					"_id=" + bundle.getLong(Domain.Buddy._ID), null);
			break;
		case SharePathService.ACTION_DELETE_ALL_BUDDIES:
			getContentResolver().delete(Domain.Buddy.CONTENT_URI,
					null, null);
			break;
		case SharePathService.ACTION_NEW_BUDDY:
			getContentResolver().insert(Domain.Buddy.CONTENT_URI, createBuddyValue(bundle));
			break;
		case SharePathService.ACTION_EDIT_BUDDY:
			getContentResolver().update(Domain.Buddy.CONTENT_URI, createBuddyValue(bundle),
					"_id=" + bundle.getLong(Domain.Buddy._ID), null);
			break;
		case SharePathService.ACTION_DELETE_MYMAP:
			getContentResolver().delete(Domain.Message.CONTENT_URI,
					"_id=" + bundle.getLong(Domain.Message._ID), null);
			break;
		case SharePathService.ACTION_DELETE_ALL_MYMAPS:
			getContentResolver().delete(Domain.Message.CONTENT_URI,
					"_type=3", null);
			break;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private ContentValues createMessageValue(Bundle bundle) {
		ContentValues cv = new ContentValues();

		cv.put(Domain.Message.TYPE, bundle.getInt(Domain.Message.TYPE));
		cv.put(Domain.Message.FROM, 
				bundle.getString(Domain.Message.FROM) == ""?
						"unknow":bundle.getString(Domain.Message.FROM));
		cv.put(Domain.Message.TO, 
				bundle.getString(Domain.Message.TO) == ""?
						"unknow":bundle.getString(Domain.Message.TO));
		cv.put(Domain.Message.DATE, bundle.getLong(Domain.Message.DATE));
		cv.put(Domain.Message.READ, bundle.getInt(Domain.Message.READ));
		cv.put(Domain.Message.START, bundle.getString(Domain.Message.START));
		cv.put(Domain.Message.END, bundle.getString(Domain.Message.END));
		cv.put(Domain.Message.LEVEL, bundle.getInt(Domain.Message.LEVEL));
		cv.put(Domain.Message.CENTER, bundle.getInt(Domain.Message.CENTER
				+ "lat")
				+ SharePathMap.INNER_SEPARATER + bundle.getInt(Domain.Message.CENTER + "lon"));
		cv.put(Domain.Message.PATH, bundle.getString(Domain.Message.PATH));
		
		Log.v(LOG_TAG, "createMessageValue:" + bundle.getInt(Domain.Message.TYPE));
		
//		Log.v(LOG_TAG, "service:" + bundle.getInt(Domain.Message.CENTER
//				+ "lat")
//				+ SharePathMap.INNER_SEPARATER + bundle.getInt(Domain.Message.CENTER + "lon")
//				+ " L:" + bundle.getInt(Domain.Message.LEVEL));
		return cv;
	}

	private ContentValues createBuddyValue(Bundle bundle) {
		ContentValues cv = new ContentValues();

		cv.put(Domain.Buddy.NAME, bundle.getString(Domain.Buddy.NAME));
		cv.put(Domain.Buddy.EMAIL, bundle.getString(Domain.Buddy.EMAIL));
		return cv;
	}
	/**
	 * The notification is the icon and associated expanded entry in the status
	 * bar.
	 */
	protected void showNotification(String from) {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		Intent contentIntent = new Intent(this, 
				org.yexing.android.sharepath.Inbox.class);

		String tickerText = getString(R.string.imcoming_message_ticker_text,
				from);

		Intent appIntent = new Intent(this,
				org.yexing.android.sharepath.Main.class);

		Notification notif = new Notification(this, // our context
				R.drawable.notify_icon, // the icon for the status bar
				tickerText, // the text to display in the ticker
				System.currentTimeMillis(), // the timestamp for the
											// notification
				from, // the title for the notification
				tickerText, // the details to display in the notification
				contentIntent, // the contentIntent (see above)
				R.drawable.icon, // the app icon
				getText(R.string.app_name), // the name of the app
				appIntent);

		notif.vibrate = new long[] { 100, 250, 100, 500 };

		nm.notify(R.string.imcoming_message_ticker_text, notif);
	}

	protected void cleanNotification() {
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(R.string.imcoming_message_ticker_text);
	}
}
