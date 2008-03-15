package org.yexing.android.sharepath;

import org.yexing.android.sharepath.domain.SharePath;

import android.app.Activity;
import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;

public class Buddy extends ListActivity {
	/**
	 * The columns we are interested in from the database
	 */
	private static final String[] PROJECTION = new String[] {
			SharePath.Buddy._ID, SharePath.Buddy.EMAIL, SharePath.Buddy.NAME };

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

//		mURI = getContentResolver().insert(SharePath.Buddy.CONTENT_URI, null);
//		mCursor = managedQuery(mURI, SharePath.Buddy.PROJECTION, null, null);
//		mCursor.first();
//		mCursor.updateString(SharePath.Buddy.EMIL_INDEX, "yeasing@gmail.com");
//		mCursor.updateString(SharePath.Buddy.NAME_INDEX, "xingye");
//
//		managedCommitUpdates(mCursor);
		
		mCursor = managedQuery(SharePath.Buddy.CONTENT_URI, PROJECTION,
				null, null, null);

		// Used to map notes entries from the database to views
		ListAdapter adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, mCursor, new String[] {
						SharePath.Buddy.NAME, SharePath.Buddy.EMAIL },
				new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
	}

}