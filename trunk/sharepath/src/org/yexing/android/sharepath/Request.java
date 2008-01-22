package org.yexing.android.sharepath;

import org.yexing.android.sharepath.domain.SharePath;

import android.database.Cursor;
import android.net.ContentURI;
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

	private ContentURI mURI;
	private Cursor mCursor;

	TextView txtFrom;
	TextView txtTo;

	private static final String[] PROJECTION = new String[] {
			SharePath.Message._ID, SharePath.Message.TYPE, SharePath.Message.FROM,
			SharePath.Message.TO};

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
	}

	private OnClickListener mSendListener = new OnClickListener() {
		public void onClick(View v) {
			mURI = getContentResolver().insert(SharePath.Message.CONTENT_URI,
					null);
			mCursor = managedQuery(mURI, PROJECTION, null, null);
			mCursor.first();
			mCursor.updateInt(SharePath.Message.TYPE_INDEX, 1);
			mCursor.updateString(SharePath.Message.FROM_INDEX, txtFrom.getText().toString());
			mCursor.updateString(SharePath.Message.TO_INDEX, txtTo.getText().toString());
			managedCommitUpdates(mCursor);
		}
	};
	private OnClickListener mCancelListener = new OnClickListener() {
		public void onClick(View v) {
			finish();
		}
	};
}
