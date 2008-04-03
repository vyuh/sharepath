package org.yexing.android.sharepath;

import java.util.HashMap;
import java.util.Map;

import org.yexing.android.sharepath.domain.Domain;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ChooseBuddy extends ListActivity {
	private static final String LOG_TAG = "SharePath";

	private Cursor mCursor;
	private Uri mURI;

	EditText etName;
	EditText etEmail;
	Map<String, Object> selectedBuddies = new HashMap<String, Object>();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Use a custom layout file
		setContentView(R.layout.choose_buddy);

		// Tell the list view which view to display when the list is empty
		getListView().setEmptyView(findViewById(R.id.empty));

		etName = (EditText) findViewById(R.id.name);
		etEmail = (EditText) findViewById(R.id.email);
		etName.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChanged(android.view.View arg0, boolean arg1) {
				if (arg1 == true) {
					etName.selectAll();
				}

			}
		});
		etEmail.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChanged(android.view.View arg0, boolean arg1) {
				if (arg1 == true) {
					etEmail.setSelection(0, etEmail.getText().toString().indexOf("@"));
				}

			}
		});

		Cursor c = getContentResolver().query(Domain.Buddy.CONTENT_URI, null,
				null, null, null);
		startManagingCursor(c);

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
				R.layout.choose_buddy_list, c, new String[] {
						Domain.Buddy.POINT, Domain.Buddy.NAME,
						Domain.Buddy.EMAIL }, new int[] { R.id.checkbox1,
						R.id.text1, R.id.text2 });

		setListAdapter(adapter);

		// Log.v("SharePath", "view count:" + adapter.getCount());

		// 快速添加好友
		Button add = (Button) findViewById(R.id.add);
		add.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				mURI = getContentResolver().insert(Domain.Buddy.CONTENT_URI,
						null);
				Log.d("SharePath", mURI.toString());
				mCursor = managedQuery(mURI, null, null, null, null);
				mCursor.first();
				mCursor.updateString(Domain.Buddy.EMAIL_INDEX, etEmail
						.getText().toString());
				mCursor.updateString(Domain.Buddy.NAME_INDEX, etName.getText()
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
				SharedPreferences preferences = getSharedPreferences(
						"SharePath", 0);
				SharedPreferences.Editor editor = preferences.edit();
				if (selectedBuddies.keySet().size() == 0) {
					showAlert(getString(R.string.error), R.drawable.about,
							getString(R.string.error_no_selection),
							getString(R.string.button_ok), true);
					return;
				}
				String email = "";
				for (int i = 0; i < selectedBuddies.keySet().size(); i++) {
					email += selectedBuddies.keySet().toArray()[i]
					            + SharePathMap.INNER_SEPARATER;
				}
				editor.putString("email", email);
				Log.v(LOG_TAG, email);
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
//		etName.setText(mCursor.getString(Domain.Buddy.NAME_INDEX));
//		etEmail.setText(mCursor.getString(Domain.Buddy.EMAIL_INDEX));

		TextView tvEmail = (TextView) ((LinearLayout) ((LinearLayout) l
				.getChildAt(position)).getChildAt(1)).getChildAt(1);
		Log.v(LOG_TAG, "email:" + tvEmail.getText());

		CheckBox cb = (CheckBox) ((LinearLayout) l.getChildAt(position))
				.getChildAt(0);
		if (cb.isChecked()) {
			cb.setChecked(false);
			selectedBuddies.remove(tvEmail.getText().toString());
		} else {
			cb.setChecked(true);
			selectedBuddies.put(tvEmail.getText().toString(), null);
		}

		for (int i = 0; i < selectedBuddies.keySet().size(); i++) {
			Log.v(LOG_TAG, "selected buddies:"
					+ selectedBuddies.keySet().toArray()[i]);

		}

		super.onListItemClick(l, v, position, id);
	}

}