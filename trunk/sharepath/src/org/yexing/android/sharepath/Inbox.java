package org.yexing.android.sharepath;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu.Item;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.SimpleCursorAdapter.FilterQueryProvider;
import android.widget.SimpleCursorAdapter.ViewBinder;

import org.yexing.android.sharepath.domain.Domain;

public class Inbox extends ListActivity {
	private Cursor mCursor;
	ListAdapter adapter;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.inbox);
		getListView().setEmptyView(findViewById(R.id.empty));

		mCursor = managedQuery(Domain.Message.CONTENT_URI, null, "_type = 0",
				null, null);

		// Used to map notes entries from the database to views
		adapter = new InboxAdapter(this,
				android.R.layout.simple_list_item_2, mCursor, new String[] {
						Domain.Message.END, Domain.Message.START }, new int[] {
						android.R.id.text1, android.R.id.text2 });
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
	
	public class InboxAdapter extends SimpleCursorAdapter {

		public InboxAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			// TODO Auto-generated method stub
			super.bindView(arg0, arg1, arg2);
		}

		@Override
		protected String convertToString(Cursor cursor) {
			// TODO Auto-generated method stub
			return super.convertToString(cursor);
		}

		@Override
		public CursorToStringConverter getCursorToStringConverter() {
			// TODO Auto-generated method stub
			return super.getCursorToStringConverter();
		}

		@Override
		public FilterQueryProvider getFilterQueryProvider() {
			// TODO Auto-generated method stub
			return super.getFilterQueryProvider();
		}

		@Override
		public int getStringConversionColumn() {
			// TODO Auto-generated method stub
			return super.getStringConversionColumn();
		}

		@Override
		public ViewBinder getViewBinder() {
			// TODO Auto-generated method stub
			return super.getViewBinder();
		}

		@Override
		protected Cursor runQuery(CharSequence arg0) {
			// TODO Auto-generated method stub
			return super.runQuery(arg0);
		}

		@Override
		public void setCursorToStringConverter(
				CursorToStringConverter cursorToStringConverter) {
			// TODO Auto-generated method stub
			super.setCursorToStringConverter(cursorToStringConverter);
		}

		@Override
		public void setFilterQueryProvider(
				FilterQueryProvider filterQueryProvider) {
			// TODO Auto-generated method stub
			super.setFilterQueryProvider(filterQueryProvider);
		}

		@Override
		public void setStringConversionColumn(int stringConversionColumn) {
			// TODO Auto-generated method stub
			super.setStringConversionColumn(stringConversionColumn);
		}

		@Override
		public void setViewBinder(ViewBinder viewBinder) {
			// TODO Auto-generated method stub
			super.setViewBinder(viewBinder);
		}

		@Override
		public void setViewImage(ImageView v, String value) {
			// TODO Auto-generated method stub
			super.setViewImage(v, value);
		}

		@Override
		public void setViewText(TextView v, String text) {
			// TODO Auto-generated method stub
			super.setViewText(v, text);
		}

		@Override
		public View newDropDownView(Context context, Cursor cursor,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.newDropDownView(context, cursor, parent);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.newView(context, cursor, parent);
		}

		@Override
		public void setDropDownViewResource(int arg0) {
			// TODO Auto-generated method stub
			super.setDropDownViewResource(arg0);
		}

		@Override
		public void changeCursor(Cursor cursor) {
			// TODO Auto-generated method stub
			super.changeCursor(cursor);
		}

		@Override
		public float getAlpha(boolean focused, int offset) {
			// TODO Auto-generated method stub
			return super.getAlpha(focused, offset);
		}

		@Override
		public Cursor getCursor() {
			// TODO Auto-generated method stub
			return super.getCursor();
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.getDropDownView(position, convertView, parent);
		}

		@Override
		public Filter getFilter() {
			// TODO Auto-generated method stub
			return super.getFilter();
		}

		@Override
		public float getScale(boolean focused, int offset) {
			// TODO Auto-generated method stub
			return super.getScale(focused, offset);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.getView(position, convertView, parent);
		}

		@Override
		protected void init(Cursor c, Context context, boolean autoRequery) {
			// TODO Auto-generated method stub
			super.init(c, context, autoRequery);
		}

		@Override
		public boolean stableIds() {
			// TODO Auto-generated method stub
			return super.stableIds();
		}

		@Override
		public boolean areAllItemsSelectable() {
			// TODO Auto-generated method stub
			return super.areAllItemsSelectable();
		}

		@Override
		public View getMeasurementView(ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.getMeasurementView(parent);
		}

		@Override
		public int getNewSelectionForKey(int currentSelection, int keyCode,
				KeyEvent event) {
			// TODO Auto-generated method stub
			return super.getNewSelectionForKey(currentSelection, keyCode, event);
		}

		@Override
		public boolean isSelectable(int position) {
			// TODO Auto-generated method stub
			return super.isSelectable(position);
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		@Override
		public void notifyDataSetInvalidated() {
			// TODO Auto-generated method stub
			super.notifyDataSetInvalidated();
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			super.registerDataSetObserver(observer);
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver arg0) {
			// TODO Auto-generated method stub
			super.unregisterDataSetObserver(arg0);
		}
		
	}

}
