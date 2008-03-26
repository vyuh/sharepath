package org.yexing.android.sharepath;

import org.yexing.android.sharepath.domain.Domain;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ChooseBuddy extends ListActivity {

	private Cursor mCursor;
	private Uri mURI;

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

		name = (EditText) findViewById(R.id.name);
		email = (EditText) findViewById(R.id.email);

		Cursor c = getContentResolver().query(Domain.Buddy.CONTENT_URI,
				null, null, null, null);
		startManagingCursor(c);

		ListAdapter adapter = new BuddyAdapter(this,
				R.layout.choose_buddy_list, c, new String[] {
						Domain.Buddy.NAME, Domain.Buddy.EMAIL },
				new int[] { R.id.text1, R.id.text2 });

		setListAdapter(adapter);

//		Log.v("SharePath", "view count:" + adapter.getCount());

		// 添加好友
		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				mURI = getContentResolver().insert(Domain.Buddy.CONTENT_URI,
						null);
				Log.d("SharePath", mURI.toString());
				mCursor = managedQuery(mURI, null, null, null, null);
				mCursor.first();
				mCursor.updateString(Domain.Buddy.EMAIL_INDEX, email
						.getText().toString());
				mCursor.updateString(Domain.Buddy.NAME_INDEX, name.getText()
						.toString());
				managedCommitUpdates(mCursor);
			}
		});

		// 放弃
		Button btnCancel = (Button) findViewById(R.id.cancel1);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		// 确定,发送请求信息
		Button btnOK = (Button) findViewById(R.id.ok1);
		btnOK.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
	            SharedPreferences preferences = getSharedPreferences("SharePath", 0);
	            SharedPreferences.Editor editor = preferences.edit();
	            editor.putString("text", email.getText().toString());
	            if (editor.commit()) {
	                setResult(RESULT_OK);
	            }

	            finish();
			}
		});

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		mURI = Uri
				.parse("content://org.yexing.android.sharepath.domain.SharePath/buddy/"
						+ id);
		mCursor = managedQuery(mURI, null, null, null);
		mCursor.first();
		name.setText(mCursor.getString(Domain.Buddy.NAME_INDEX));
		email.setText(mCursor.getString(Domain.Buddy.EMAIL_INDEX));
		
		super.onListItemClick(l, v, position, id);
	}

	class BuddyAdapter extends SimpleCursorAdapter {

		public BuddyAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			// TODO Auto-generated constructor stub
		}

		// @Override
		// public View getView(int position, View convertView, ViewGroup parent)
		// {
		// //处理checkbox
		// ViewInflate inflate = (ViewInflate)
		// getSystemService(Context.INFLATE_SERVICE);
		// View view = inflate.inflate(R.layout.choose_buddy_list, null, null);
		// Log.v("SharePath","getView view:" + (view==null?"error":"ok"));
		// CheckBox cb = (CheckBox)view.findViewById(R.id.checkbox1);
		// cb.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// Log.v("SharePath","checkbox");
		// showAlert(null, 0, "checkbox", "ok", true);
		// }
		// });
		// return view;
		// }

	}
}