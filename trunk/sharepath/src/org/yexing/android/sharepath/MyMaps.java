package org.yexing.android.sharepath;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Menu.Item;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.yexing.android.sharepath.domain.Domain;

public class MyMaps extends ListActivity {
	private static final String LOG_TAG = "SharePath";

	private Cursor mCursor;
	ListAdapter adapter;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		
		setContentView(R.layout.mymaps);
		getListView().setEmptyView(findViewById(R.id.empty));

		mCursor = managedQuery(Domain.Message.CONTENT_URI, null,
				"_type = 3", null, null);

		adapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_2, mCursor, new String[] {
						Domain.Message.END, Domain.Message.START },
				new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, org.yexing.android.sharepath.SharePathMap.class);
		intent.putExtra(Domain.Message._ID, id);
		intent.putExtra("navfrom", SharePathMap.FROM_MYMAPS);
		mCursor.moveTo(position);
		intent.putExtra(Domain.Message.START, 
				mCursor.getString(mCursor.getColumnIndex(Domain.Message.START)));
		intent.putExtra(Domain.Message.END, 
				mCursor.getString(mCursor.getColumnIndex(Domain.Message.END)));
		Log.v(LOG_TAG, mCursor.getString(mCursor.getColumnIndex(Domain.Message.START)));
		startActivity(intent);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
//		menu.add(0, SharePathMap.MENU_ADD, R.string.add).setIcon(R.drawable.add);
		// µã»÷±à¼­		
//		menu.add(0, SharePathMap.MENU_EDIT, R.string.edit).setIcon(R.drawable.edit);
		menu.add(0, SharePathMap.MENU_DELETE, R.string.delete).setIcon(R.drawable.delete);
		menu.add(0, SharePathMap.MENU_DELETE_ALL, R.string.delete_all).setIcon(
				R.drawable.delete_all);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if(adapter.getCount() == 0){
//			menu.get(menu.findItemIndex(SharePathMap.MENU_EDIT)).setShown(false);
			menu.get(menu.findItemIndex(SharePathMap.MENU_DELETE)).setShown(false);
			menu.get(menu.findItemIndex(SharePathMap.MENU_DELETE_ALL)).setShown(false);
		} else {
//			menu.get(menu.findItemIndex(SharePathMap.MENU_EDIT)).setShown(true);
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
//		case SharePathMap.MENU_ADD:
//			intent = new Intent(this,
//					org.yexing.android.sharepath.EditBuddy.class);
//			startActivity(intent);
//			break;
//		case SharePathMap.MENU_EDIT:
//			edit();
//			break;
		case SharePathMap.MENU_DELETE:
			bundle.putInt("action", SharePathService.ACTION_DELETE_MYMAP);
			bundle.putLong(Domain.Message._ID, getSelectedItemId());
			startService(new Intent(this, SharePathService.class), bundle);
			break;
		case SharePathMap.MENU_DELETE_ALL:
			bundle.putInt("action", SharePathService.ACTION_DELETE_ALL_MYMAPS);
			startService(new Intent(this, SharePathService.class), bundle);
			break;
		}
		return true;
	}
	
}

