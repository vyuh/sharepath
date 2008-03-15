package org.yexing.android.sharepath;

import java.util.Map;

import org.yexing.android.sharepath.domain.SharePath;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts.People;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ChooseBuddyView extends ListView{
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

	public ChooseBuddyView(Context context, AttributeSet attrs,
			Map inflateParams) {
		super(context, attrs, inflateParams);
		ListActivity la = (ListActivity)context;
		
		// TODO Auto-generated constructor stub
		mCursor = la.managedQuery(SharePath.Buddy.CONTENT_URI, PROJECTION,
				null, null, null);
//
//		// Used to map notes entries from the database to views
//		ListAdapter adapter = new SimpleCursorAdapter(la,
//				R.layout.simple_checkbox_list, mCursor, new String[] {
//						SharePath.Buddy.NAME, SharePath.Buddy.EMAIL },
//				new int[] { R.id.checkbox1, R.id.text1 });
//
//		la.setListAdapter(adapter);
		

		
//		setAdapter(new ArrayAdapter<String>(context,
//				android.R.layout.simple_list_item_2, 
//				PROJECTION));
		
		
        // Get a cursor with all people


//        ListAdapter adapter = new SimpleCursorAdapter(context, 
//                // Use a template that displays a text view
//                android.R.layout.simple_list_item_1, 
//                // Give the cursor to the list adatper
//                mCursor, 
//                // Map the NAME column in the people database to...
//                new String[] {People.NAME} ,
//                // The "text1" view defined in the XML template
//                new int[] {android.R.id.text1}); 

        
		ListAdapter adapter = new SimpleCursorAdapter(context,
		R.layout.simple_checkbox_list, mCursor, new String[] {
				SharePath.Buddy.NAME, SharePath.Buddy.EMAIL },
		new int[] { R.id.text1, R.id.text2 });
		
		
        setAdapter(adapter);

	}

	

}
