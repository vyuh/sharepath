package org.yexing.android.sharepath;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Point;

public class Request extends MapActivity {
	MapView mv;
	MapController mc;

	private Uri mURI;
	private Cursor mCursor;

	TextView txtFrom;
	TextView txtTo;
	
	static int CHOOSE_BUDDY = 1;
	
	Intent intent;
	
	Dialog dlgChooseBuddy;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.request);
		mv = (MapView) findViewById(R.id.mapview);
		mc = mv.getController();
		mc.centerMapTo(new Point(40708181, -74012030), true);
		mc.zoomTo(16);

		Button button = (Button) findViewById(R.id.send);
		button.setOnClickListener(mSendListener);
		button = (Button) findViewById(R.id.cancel);
		button.setOnClickListener(mCancelListener);

		txtFrom = (TextView) findViewById(R.id.from);
		txtTo = (TextView) findViewById(R.id.to);

		intent = new Intent(this, org.yexing.android.sharepath.ChooseBuddy.class);
//		dlgChooseBuddy = new Dialog(this);
	}

	private OnClickListener mSendListener = new OnClickListener() {
		public void onClick(View v) {
//			mURI = getContentResolver().insert(SharePath.Message.CONTENT_URI,
//					null);
//			mCursor = managedQuery(mURI, SharePath.Message.PROJECTION, null, null);
//			mCursor.first();
//			mCursor.updateInt(SharePath.Message.TYPE_INDEX, 0);
//			mCursor.updateString(SharePath.Message.FROM_INDEX, txtFrom.getText().toString());
//			mCursor.updateString(SharePath.Message.TO_INDEX, txtTo.getText().toString());
//			managedCommitUpdates(mCursor);
//			finish();

			startSubActivity(intent, CHOOSE_BUDDY);
		    
//			dlgChooseBuddy.setContentView(R.layout.simple_checkbox_list);
//			dlgChooseBuddy.setContentView(view)
//		    dlgChooseBuddy.show();

		}
	};
	private OnClickListener mCancelListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};
}
