package org.yexing.android.sharepath;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewInflate;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Main extends ListActivity {
	private static final String LOG_TAG = "SharePath";

	List<Map<String, Object>> list;
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		list = new ArrayList<Map<String, Object>>();
		int index = 1;

//		addItem(list, index++ + ". test", activityIntent(
//				"org.yexing.android.sharepath",
//				"org.yexing.android.sharepath.ChooseBuddy"));
		
		
//		addItem(list, index++ + ". Ask for Directions", activityIntent(
//				"org.yexing.android.sharepath",
//				"org.yexing.android.sharepath.Request"));
		addItem(list, index++ + ". Ask or Record", activityIntent(
				"org.yexing.android.sharepath",
				"org.yexing.android.sharepath.SharePathMap"));
		addItem(list, index++ + ". Inbox", activityIntent(
				"org.yexing.android.sharepath",
				"org.yexing.android.sharepath.Inbox"));
//		addItem(list, index++ + ". Outbox", activityIntent(
//				"org.yexing.android.sharepath",
//				"org.yexing.android.sharepath.Outbox"));
//		addItem(list, index++ + ". Sent Request", activityIntent(
//				"org.yexing.android.sharepath",
//				"org.yexing.android.sharepath.Sent"));
		addItem(list, index++ + ". My Maps", activityIntent(
				"org.yexing.android.sharepath",
				"org.yexing.android.sharepath.MyMaps"));
		addItem(list, index++ + ". My Buddies", activityIntent(
				"org.yexing.android.sharepath",
				"org.yexing.android.sharepath.Buddy"));

		setListAdapter(new ImageSimpleAdapter(this, list,
				R.layout.main_list, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		
		// Ä£ÄâbootupµÄ°´Å¥
//		Button btnBoot = (Button) findViewById(R.id.boot);
//		btnBoot.setOnClickListener(new View.OnClickListener() {
//
//			public void onClick(View v) {
//				Intent intent = new Intent(Intent.BOOT_COMPLETED_ACTION);
//				broadcastIntent(intent);
//			}
//		});
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map map = (Map) l.obtainItem(position);

		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}

	@SuppressWarnings("unchecked")
	protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put("intent", intent);
		data.add(temp);
	}

	protected Intent activityIntent(String pkg, String componentName) {
		Intent result = new Intent();
		result.setClassName(pkg, componentName);
		return result;
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
		        convertView = inflate.inflate(R.layout.main_list, null, null);
		        TextView tv = (TextView)convertView.findViewById(R.id.text1);
		        tv.setText((String)list.get(position).get("title"));
		        ImageView iv = (ImageView)convertView.findViewById(R.id.image);
//		        iv.setImageResource(R.drawable.badge);
		        Uri uri = Uri.parse("http://www.yexing.org/image.axd?picture=browse.png");
		        
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
}