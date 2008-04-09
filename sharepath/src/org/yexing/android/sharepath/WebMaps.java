package org.yexing.android.sharepath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewInflate;
import android.view.Menu.Item;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import org.yexing.android.sharepath.Main.ImageSimpleAdapter;
import org.yexing.android.sharepath.domain.Domain;

public class WebMaps extends ListActivity {
	private static final String LOG_TAG = "SharePath";	

	List<Map> list;
	ImageSimpleAdapter isa;
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.webmaps);
		getListView().setEmptyView(findViewById(R.id.empty));

		list = new ArrayList<Map>();
		isa = new ImageSimpleAdapter(this, list,
				R.layout.inbox_list, null, null);
		
		setListAdapter(isa);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this,
				org.yexing.android.sharepath.SharePathMap.class);
		intent.putExtra("navfrom", SharePathMap.FROM_WEBMAPS);
		intent.putExtra(Domain.Message.START, (String)list.get(position).get("start"));
		intent.putExtra(Domain.Message.END, (String)list.get(position).get("end"));
		intent.putExtra(Domain.Message.LEVEL, (String)list.get(position).get("level"));
		intent.putExtra(Domain.Message.CENTER, (String)list.get(position).get("center"));
		intent.putExtra(Domain.Message.PATH, (String)list.get(position).get("path"));
		startActivity(intent);
	}
	
	class ImageSimpleAdapter extends SimpleAdapter {

		public ImageSimpleAdapter(Context context, List data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView == null) {
		        ViewInflate inflate = (ViewInflate) getSystemService(Context.INFLATE_SERVICE);
		        convertView = inflate.inflate(R.layout.inbox_list, null, null);
		        TextView tv = (TextView)convertView.findViewById(R.id.text1);
		        tv.setText((String)list.get(position).get("start"));
		        TextView tv2 = (TextView)convertView.findViewById(R.id.text2);
		        tv2.setText((String)list.get(position).get("end"));
		        ImageView iv = (ImageView)convertView.findViewById(R.id.image);
		        iv.setImageResource(R.drawable.star);
//		        Uri uri = Uri.parse("http://www.yexing.org/image.axd?picture=browse.png");
		        
//		        iv.setImageURI(uri);
//		        Drawable drawable = Drawable.createFromPath(uri.getPath());
//		        try {
//			        URL url = new URL("http://www.yexing.org/image.axd?picture=browse.png");
//			        InputStream is = url.openStream();
//			        Drawable drawable = Drawable.createFromStream(is, "none");
//			        iv.setImageDrawable(drawable);
//		        } catch(Exception e) {
//		        	Log.e(LOG_TAG, "load image error! \n" + e.toString());
//		        }
			}
			return convertView; //super.getView(position, convertView, parent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SharePathMap.MENU_SEARCH, R.string.search).setIcon(
				R.drawable.search);
//		menu.add(0, SharePathMap.MENU_REFRESH, R.string.refresh)
//			.setIcon(R.drawable.refresh);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);

		return true;
	}
	@Override
	public boolean onOptionsItemSelected(Item item) {
		super.onOptionsItemSelected(item);
		switch (item.getId()) {
		case SharePathMap.MENU_SEARCH:
			WebHelper wh = new WebHelper();
			if(wh.Request(getString(R.string.url) + "?action=search",
					list) == false) {
				this.showAlert("Error", R.drawable.about, "Connect time out!", "OK", true);
			} else {
				if(list.size()>0) {
					isa.notifyDataSetChanged();
				} else {
					this.showAlert("Information", R.drawable.about, "Here is no map available!", "OK", true);					
				}
			}
			break;
		}
		return true;
	}
	
}

