package org.yexing.android.sharepath;

import java.util.ArrayList;

import org.yexing.android.sharepath.domain.SharePath;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.Phones;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ChooseBuddy extends ListActivity {
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

    PhotoAdapter mAdapter;
    
    EditText name;
    EditText email;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Use a custom layout file
        setContentView(R.layout.choose_buddy);
        
        // Tell the list view which view to display when the list is empty
        getListView().setEmptyView(findViewById(R.id.empty));
        
        name = (EditText)findViewById(R.id.name);
        email = (EditText)findViewById(R.id.email);
        
//        // Set up our adapter
//        mAdapter = new PhotoAdapter(this);
//        setListAdapter(mAdapter);

        // Get a cursor with all phones
        Cursor c = getContentResolver().query(SharePath.Buddy.CONTENT_URI, null, null, null, null);
        startManagingCursor(c);
        
        // Map Cursor columns to views defined in simple_list_item_2.xml
        ListAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, c, 
                        new String[] { SharePath.Buddy.NAME, SharePath.Buddy.EMAIL }, 
                        new int[] { android.R.id.text1, android.R.id.text2 });

        setListAdapter(adapter);

        
        // Wire up the clear button to remove all photos
//        Button clear = (Button) findViewById(R.id.cancel1);
//        clear.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                mAdapter.clearPhotos();
//            } });
        
        // Wire up the add button to add a new photo
        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mURI = getContentResolver().insert(SharePath.Buddy.CONTENT_URI, null);
        		Log.d("xingye", mURI.getPath());
                mCursor = managedQuery(mURI, null, null, null, null);
        		mCursor.first();
        		mCursor.updateString(SharePath.Buddy.EMIL_INDEX, email.getText().toString());
        		mCursor.updateString(SharePath.Buddy.NAME_INDEX, name.getText().toString());
        		managedCommitUpdates(mCursor);
            } });

	}
    /**
     * A simple adapter which maintains an ArrayList of photo resource Ids. 
     * Each photo is displayed as an image. This adapter supports clearing the
     * list of photos and adding a new photo.
     *
     */
    public class PhotoAdapter extends BaseAdapter {

        private Context mContext;

        private Integer[] mPhotoPool = {
//                R.drawable.sample_thumb_0, R.drawable.sample_thumb_1, R.drawable.sample_thumb_2,
//                R.drawable.sample_thumb_3, R.drawable.sample_thumb_4, R.drawable.sample_thumb_5,
//                R.drawable.sample_thumb_6, R.drawable.sample_thumb_7
                };

        private String[] mBuddyPool = {
        		"jim", "mike", "tom"
        };
        
        private ArrayList<Integer> mPhotos = new ArrayList<Integer>();
        private ArrayList<String> mBuddies = new ArrayList<String>();
        
        public PhotoAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
//            return mPhotos.size();
        	return mBuddies.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
//            // Make an ImageView to show a photo
//            ImageView i = new ImageView(mContext);
//
//            i.setImageResource(mPhotos.get(position));
//            i.setAdjustViewBounds(true);
//            i.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
//                    LayoutParams.WRAP_CONTENT));
//            // Give it a nice background
//            i.setBackground(android.R.drawable.picture_frame);
            TextView t = new TextView(mContext);
            t.setText(mBuddies.get(position));
            return t;
        }


        public void clearPhotos() {
            //mPhotos.clear();
        	mBuddies.clear();
            notifyDataSetChanged();
        }
        
        public void addPhotos() {
            int whichPhoto = (int)Math.round(Math.random() * (mBuddyPool.length - 1));
            int newPhoto = mPhotoPool[whichPhoto];
            mBuddies.add(mBuddyPool[whichPhoto]);
            notifyDataSetChanged();
        }

    }
}