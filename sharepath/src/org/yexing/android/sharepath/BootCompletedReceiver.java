package org.yexing.android.sharepath;

import android.content.Context;
import android.content.Intent;
import android.content.IntentReceiver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BootCompletedReceiver extends IntentReceiver {
	private static final String LOG_TAG = "SharePath";

	public void onReceiveIntent(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.BOOT_COMPLETED_ACTION)) {
			String info = "boot completed!";
			Log.i(LOG_TAG, info);
			
			Bundle bundle = new Bundle();
			bundle.putInt("action", SharePathService.ACTION_SEARCH_NEW_REQUEST);

			context.startService(new Intent(context, SharePathService.class),
					bundle);
		}
	}
}
