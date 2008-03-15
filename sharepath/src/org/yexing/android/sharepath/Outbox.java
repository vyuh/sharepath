package org.yexing.android.sharepath;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

import org.yexing.android.sharepath.domain.SharePath;

public class Outbox extends ListActivity {
	/**
	 * The columns we are interested in from the database
	 */
	private static final String[] PROJECTION = new String[] {
			SharePath.Message._ID, SharePath.Message.FROM };

	/**
	 * Cursor which holds list of all notes
	 */
	private Cursor mCursor;
	private Uri mURI;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setDefaultKeyMode(SHORTCUT_DEFAULT_KEYS);

		// setupListStripes();

		mCursor = managedQuery(SharePath.Message.CONTENT_URI, PROJECTION,
				"_type = 1", null, null);

		// Used to map notes entries from the database to views
		ListAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, mCursor, new String[] {
						SharePath.Message._ID, SharePath.Message.FROM },
				new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
	}

}
