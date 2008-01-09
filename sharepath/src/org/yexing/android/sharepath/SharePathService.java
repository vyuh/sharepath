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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.BinderNative;
import android.os.IBinder;
import android.os.Parcel;

public class SharePathService extends Service {
	@Override
	protected void onCreate() {
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// This is who should be launched if the user selects our persistent
		// notification.
		Intent intent = new Intent();
		// intent.setClass(this, LocalServiceController.class);

		// Display a notification about us starting. We use both a transient
		// notification and a persistent notification in the status bar.
		mNM.notifyWithText(R.string.sharepath_service_started,
				getText(R.string.sharepath_service_started),
				NotificationManager.LENGTH_SHORT, new Notification(
						R.drawable.stat_sample,
						getText(R.string.sharepath_service), null, null, null));
	}

	@Override
	protected void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(R.string.sharepath_service_started);

		// Tell the user we stopped.
		mNM.notifyWithText(R.string.sharepath_service_stopped,
				getText(R.string.sharepath_service_stopped),
				NotificationManager.LENGTH_SHORT, null);
	}

	@Override
	public IBinder getBinder() {
		return mBinder;
	}

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new BinderNative() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) {
			return super.onTransact(code, data, reply, flags);
		}
	};

	private NotificationManager mNM;
}
