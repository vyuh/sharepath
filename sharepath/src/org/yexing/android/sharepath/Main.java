package org.yexing.android.sharepath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.MeasureSpec;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Main extends ListActivity {
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		setupListStripes();
		// setContentView(R.layout.main);
		List<Map> list = new ArrayList<Map>();
		int index = 1;

//		addItem(list, index++ + ". test", activityIntent(
//				"org.yexing.android.sharepath",
//				"org.yexing.android.sharepath.Draft"));
		
		
//		addItem(list, index++ + ". Ask for Directions", activityIntent(
//				"org.yexing.android.sharepath",
//				"org.yexing.android.sharepath.Request"));
		addItem(list, index++ + ". Record a Path", activityIntent(
				"org.yexing.android.sharepath",
				"org.yexing.android.sharepath.Record"));
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

		setListAdapter(new SimpleAdapter(this, list,
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
	}

//	/**
//	 * Add stripes to the list view.
//	 */
//	private void setupListStripes() {
//		// Get Drawables for alternating stripes
//		Drawable[] lineBackgrounds = new Drawable[2];
//
//		lineBackgrounds[0] = getResources().getDrawable(R.drawable.even_stripe);
//		lineBackgrounds[1] = getResources().getDrawable(R.drawable.odd_stripe);
//
//		// Make and measure a sample TextView of the sort our adapter will
//		// return
//		View view = getViewInflate().inflate(
//				android.R.layout.simple_list_item_1, null, null);
//
//		TextView v = (TextView) view.findViewById(android.R.id.text1);
//		v.setText("X");
//		// Make it 100 pixels wide, and let it choose its own height.
//		v.measure(MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY),
//				MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//		int height = v.getMeasuredHeight();
//		getListView().setStripes(lineBackgrounds, height);
//	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Map map = (Map) l.obtainItem(position);

		Intent intent = (Intent) map.get("intent");
		startActivity(intent);
	}

	@SuppressWarnings("unchecked")
	protected void addItem(List<Map> data, String name, Intent intent) {
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
}