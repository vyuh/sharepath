package org.yexing.android.sharepath;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.ContentURI;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import org.yexing.android.sharepath.domain.SharePath;

public class Inbox extends ListActivity {
	/**
	 * The columns we are interested in from the database
	 */
	private static final String[] PROJECTION = new String[] {
			SharePath.Message._ID, SharePath.Message.FROM };

	/**
	 * Cursor which holds list of all notes
	 */
	private Cursor mCursor;

	private ContentURI mURI;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setDefaultKeyMode(SHORTCUT_DEFAULT_KEYS);

		// If no data was given in the intent (because we were started
		// as a MAIN activity), then use our default content provider.
		Intent intent = getIntent();
		if (intent.getData() == null) {
			intent.setData(SharePath.Message.CONTENT_URI);
		}

		// setupListStripes();

		mCursor = managedQuery(getIntent().getData(), PROJECTION, null, null);

		// Used to map notes entries from the database to views
		ListAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, mCursor, new String[] {
						SharePath.Message._ID, SharePath.Message.FROM },
				new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
	}

}
