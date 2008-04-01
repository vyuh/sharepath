package org.yexing.android.sharepath;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;

import org.yexing.android.sharepath.domain.Domain;

public class Inbox extends ListActivity {
	private static final String LOG_TAG = "SharePath";
	private Cursor mCursor;
	SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.inbox);
		getListView().setEmptyView(findViewById(R.id.empty));

		mCursor = managedQuery(Domain.Message.CONTENT_URI, null, "_type = 0",
				null, null);

		// Used to map notes entries from the database to views
		adapter = new SimpleCursorAdapter(this,
				R.layout.inbox_list, mCursor, new String[] {
						Domain.Message.FROM, Domain.Message.END, Domain.Message.START }, new int[] {
						R.id.image, R.id.text1, R.id.text2 });
		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder(){

			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				// TODO Auto-generated method stub
//				Log.v(LOG_TAG, "setViewValue");
//				Log.v(LOG_TAG, "columnIndex:" + columnIndex);
//				Log.v(LOG_TAG, "column:" + cursor.getString(columnIndex));
				int resid = 0;
				if(columnIndex == cursor.getColumnIndex(Domain.Message.FROM)) {
					ImageView iv = (ImageView)view;
					Log.v(LOG_TAG, "list type:" + cursor.getInt(cursor.getColumnIndex(Domain.Message.TYPE)));
					if(cursor.getInt(cursor.getColumnIndex(Domain.Message.TYPE)) == 0) {
						if(cursor.getInt(cursor.getColumnIndex(Domain.Message.READ)) == 0) {
							resid = R.drawable.msg_ask_new;
						} else {
							resid = R.drawable.msg_ask_read;
						}
							
					} else {
						if(cursor.getInt(cursor.getColumnIndex(Domain.Message.READ)) == 0) {
							resid = R.drawable.msg_reply_new;
						} else {
							resid = R.drawable.msg_reply_read;
						}
						
					}
					if(resid != 0) {
						iv.setImageResource(resid);
						return true;
					}
				}
				return false;
			}
			
		});
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this,
				org.yexing.android.sharepath.SharePathMap.class);
		intent.putExtra("navfrom", SharePathMap.FROM_INBOX);
		intent.putExtra(Domain.Message._ID, id);
		mCursor.moveTo(position);
		intent.putExtra(Domain.Message.START, 
				mCursor.getString(mCursor.getColumnIndex(Domain.Message.START)));
		intent.putExtra(Domain.Message.END, 
				mCursor.getString(mCursor.getColumnIndex(Domain.Message.END)));
		intent.putExtra(Domain.Message.FROM,
				mCursor.getString(mCursor.getColumnIndex(Domain.Message.FROM)));
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SharePathMap.MENU_EDIT, R.string.reply).setIcon(R.drawable.reply);
		menu.add(0, SharePathMap.MENU_DELETE, R.string.delete).setIcon(R.drawable.delete);
		menu.add(0, SharePathMap.MENU_DELETE_ALL, R.string.delete_all).setIcon(
				R.drawable.delete_all);

		return true;
	}
	
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if(adapter.getCount() == 0){
			menu.get(menu.findItemIndex(SharePathMap.MENU_EDIT)).setShown(false);
			menu.get(menu.findItemIndex(SharePathMap.MENU_DELETE)).setShown(false);
			menu.get(menu.findItemIndex(SharePathMap.MENU_DELETE_ALL)).setShown(false);
		} else {
			menu.get(menu.findItemIndex(SharePathMap.MENU_EDIT)).setShown(true);
			menu.get(menu.findItemIndex(SharePathMap.MENU_DELETE)).setShown(true);
			menu.get(menu.findItemIndex(SharePathMap.MENU_DELETE_ALL)).setShown(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(Item item) {
		super.onOptionsItemSelected(item);
		Bundle bundle = new Bundle();
		switch (item.getId()) {
		case SharePathMap.MENU_EDIT:
			Intent intent = new Intent(this,
					org.yexing.android.sharepath.SharePathMap.class);
			intent.putExtra(Domain.Message._ID, getSelectedItemId());
			startActivity(intent);
			break;
		case SharePathMap.MENU_DELETE:
			bundle.putInt("action", SharePathService.ACTION_DELETE_REQUEST);
			bundle.putLong(Domain.Message._ID, getSelectedItemId());
			startService(new Intent(this, SharePathService.class), bundle);
			break;
		case SharePathMap.MENU_DELETE_ALL:
			bundle.putInt("action", SharePathService.ACTION_DELETE_ALL_REQUEST);
			startService(new Intent(this, SharePathService.class), bundle);
			break;
		}
		return true;
	}
	
	public class MessageViewBinder implements ViewBinder {

		public boolean setViewValue(View arg0, Cursor arg1, int arg2) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
