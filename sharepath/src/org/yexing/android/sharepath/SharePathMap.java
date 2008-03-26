package org.yexing.android.sharepath;

import java.util.ArrayList;

import org.yexing.android.sharepath.domain.Domain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.Resources;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.Menu.Item;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.OverlayController;
import com.google.android.maps.Point;

public class SharePathMap extends MapActivity {
	private static final String LOG_TAG = "SharePath";

	// request code
	static final int CODE_CHOOSE_BUDDY = 1;
	static final int CODE_INBOX = CODE_CHOOSE_BUDDY + 1;
	static final int CODE_MYMAPS = CODE_INBOX + 1;
	static final int CODE_BUDDIES = CODE_MYMAPS + 1;

	// 通用菜单项
	public static final int MENU_ADD = 0;
	public static final int MENU_EDIT = MENU_ADD + 1;
	public static final int MENU_DELETE = MENU_EDIT + 1;
	public static final int MENU_DELETE_ALL = MENU_DELETE + 1;

	// 从哪个界面转到map界面的
	public static final int FROM_MAIN = 0;
	public static final int FROM_MYMAPS = FROM_MAIN + 1;
	public static final int FROM_INBOX = FROM_MYMAPS + 1;

	static Resources r;

	long id = 0;
	int navfrom = 0;
	String start;
	String end;

	public static MarkableMapView mv;
	public PathOverlay po;
	public MapController mc;
	public OverlayController oc;
	LocationManager lm;
	Location l;

	int screenWidth, screenHeight; // 屏幕的宽度和高度

	private Cursor mCursor;
	private Uri mURI;

	Dialog savePathDialog; // 保存路径对话框

	// 菜单
	private static final int MENU_BROWSE = 0;
	private static final int MENU_MARK = MENU_BROWSE + 1;
	private static final int MENU_CLEAN = MENU_MARK + 1;
	private static final int MENU_ADDBADGE = MENU_CLEAN + 1;
	private static final int MENU_SAVE = MENU_ADDBADGE + 1;
	private static final int MENU_LAYERS = MENU_SAVE + 1;
	private static final int MENU_ABOUT = MENU_LAYERS + 1;
	private static final int MENU_ZOOM = MENU_ABOUT + 1;
	private static final int MENU_SEND = MENU_ZOOM + 1;
	private static final int MENU_ASK = MENU_SEND + 1;
	private static final int MENU_INBOX = MENU_ASK + 1;
	private static final int MENU_MYMAPS = MENU_INBOX + 1;
	private static final int MENU_BUDDIES = MENU_MYMAPS + 1;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		r = getResources();

		// 检查启动参数
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		// 取得数据库中路径的id
		if (extras != null) {
			id = extras.getLong(Domain.Message._ID);
			navfrom = extras.getInt("navfrom");
			start = extras.getString(Domain.Message.START);
			Log.v(LOG_TAG, "map navfrom:" + navfrom);
			end = extras.getString(Domain.Message.END);
		}

		// 去掉title bar
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 去掉toolbar
		// getWindow().setFlags(WindowManager.LayoutParams.NO_STATUS_BAR_FLAG,
		// WindowManager.LayoutParams.NO_STATUS_BAR_FLAG);

		// screen width and height
		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();
		screenWidth = d.getWidth();
		screenHeight = d.getHeight();

		// 地图
		// setContentView(R.layout.sharepath_map);
		// mv = (MarkableMapView) findViewById(R.id.mv);
		mv = new MarkableMapView(this, null, null);
		setContentView(mv);

		oc = mv.createOverlayController();
		mc = mv.getController();

		if (id != 0) { // 设定了id, 取出记录
			mURI = Uri
					.parse("content://org.yexing.android.sharepath.domain.SharePath/message/"
							+ id);
			// Log.v(LOG_TAG, mURI.toString());
			mCursor = managedQuery(mURI, null, null, null);
			mCursor.first();

			// 设置中点坐标
			String[] temp = mCursor.getString(Domain.Message.CENTER_INDEX)
					.split("\b");
			mc.centerMapTo(new Point(Integer.parseInt(temp[0]), Integer
					.parseInt(temp[1])), true);
			// 缩放
			mc.zoomTo(mCursor.getInt(Domain.Message.LEVEL_INDEX));

			// 路径信息
			String[] temp2;
			if (mCursor.getString(Domain.Message.PATH_INDEX) != null) {
				temp = mCursor.getString(Domain.Message.PATH_INDEX).split("\f");
				mv.points.clear();

				// Log.v(LOG_TAG, "temp " + temp.length);
				// Log.v(LOG_TAG, "path " +
				// mCursor.getString(SharePath.Message.PATH_INDEX));

				for (int i = 0; i < temp.length; i++) {
					temp2 = temp[i].split("\b");
					// Log.v(LOG_TAG, "temp2 " + temp2.length);
					KeyPoint tt = new KeyPoint(new android.graphics.Point(
							Integer.parseInt(temp2[0]), Integer
									.parseInt(temp2[1])),
							(temp2.length == 3 ? temp2[2] : ""));
					mv.points.add(tt);
				}
			}
		} else {

			// 得到当前位置的gps坐标
			lm = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);
			l = lm.getCurrentLocation("gps");
			// List list = lm.getProviders();

			// 重新定位地图到当前位置
			mc.centerMapTo(new Point((int) (l.getLatitude() * 1e6), (int) (l
					.getLongitude() * 1e6)), true);
			mc.zoomTo(16);
		}

		po = new PathOverlay(r, mv, screenWidth, screenHeight);
		if(navfrom != 0) {
			po.start = start;
			po.end = end;
		}
		oc.add(po, false);

		// GraphicOverlay go = new GraphicOverlay(getResources().getDrawable(
		// R.drawable.zoom_in), new android.graphics.Point(100, 100));
		// oc.add(go, true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Log.i(LOG_TAG, "keyCode:" + keyCode);
		switch (keyCode) {
		case KeyEvent.KEYCODE_S:
			layers();
			break;
		case KeyEvent.KEYCODE_M: // 切换到地图界面
			browse();
			break;
		case KeyEvent.KEYCODE_N: // 切换到标图界面
			mark();
			break;
		case KeyEvent.KEYCODE_SOFT_LEFT:
			openOptionsMenu();
			break;
		case KeyEvent.KEYCODE_C: // 清除当前路径
			clean();
			break;
		case KeyEvent.KEYCODE_R: // 设置提示
			addBadge();
			break;
		case KeyEvent.KEYCODE_H: // 保存路径
			save();
			break;
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Log.i(LOG_TAG, "onCreateOptionsMenu");

		super.onCreateOptionsMenu(menu);

//		menu.add(1, MENU_INBOX, "inbox").setIcon(
//				r.getDrawable(R.drawable.about));
//		menu.add(1, MENU_MYMAPS, "mymaps").setIcon(
//				r.getDrawable(R.drawable.about));
//		menu.add(1, MENU_BUDDIES, "Buddies").setIcon(
//				r.getDrawable(R.drawable.about));

		menu.add(0, MENU_BROWSE, R.string.browse).setIcon(
				r.getDrawable(R.drawable.browse));
		// menu.add(0, MENU_MARK,
		// "Mark").setIcon(r.getDrawable(R.drawable.mark));
		menu.add(0, MENU_CLEAN, "Clean").setIcon(
				r.getDrawable(R.drawable.clean));
		menu.add(0, MENU_ASK, "Ask").setIcon(r.getDrawable(R.drawable.ask));
		menu.add(0, MENU_ZOOM, "Zoom").setIcon(
				r.getDrawable(R.drawable.zoom_in));
		menu.add(0, MENU_ADDBADGE, "Add Badge").setIcon(
				r.getDrawable(R.drawable.badge));
		menu.add(0, MENU_SAVE, "Save").setIcon(r.getDrawable(R.drawable.save));
		menu.add(0, MENU_SEND, "Send").setIcon(r.getDrawable(R.drawable.send));
		menu.add(0, MENU_LAYERS, "Layers").setIcon(
				r.getDrawable(R.drawable.layers));

		menu.add(0, MENU_ABOUT, "About").setIcon(
				r.getDrawable(R.drawable.about));

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Log.i(LOG_TAG, "onPrepareOptionsMenu");
		super.onPrepareOptionsMenu(menu);
		Item itemBrowse = menu.get(MENU_BROWSE);
		if (mv.marking) {
			itemBrowse.setIcon(r.getDrawable(R.drawable.browse));
			itemBrowse.setTitle(getText(R.string.browse));
		} else {
			itemBrowse.setIcon(r.getDrawable(R.drawable.mark));
			itemBrowse.setTitle(getText(R.string.mark));
		}

		// switch(navfrom) {
		// case FROM_INBOX:
		// menu.get(menu.findItemIndex(MENU_SEND)).
		// }
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(Menu.Item item) {
		// Log.i(LOG_TAG, "onOptionsItemSelected");
		switch (item.getId()) {
		case MENU_BROWSE:
			if (mv.marking) {
				browse();
			} else {
				mark();
			}
			break;
		// case MENU_MARK:
		// mark();
		// break;
		case MENU_CLEAN:
			clean();
			break;
		case MENU_ADDBADGE:
			addBadge();
			break;
		case MENU_SAVE:
			save();
			break;
		case MENU_LAYERS:
			layers();
			break;
		case MENU_ABOUT:
			about();
			break;
		case MENU_ZOOM:
			zoom();
			break;
		case MENU_SEND:
			send();
			break;
		case MENU_ASK:
			ask();
			break;
		case MENU_INBOX:
			inbox();
			break;
		case MENU_MYMAPS:
			mymaps();
			break;
		case MENU_BUDDIES:
			buddies();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void browse() {
		if (mv.marking == true) {
			mv.marking = false;
			Toast.makeText(this, "Turn to BROWSE mode...", Toast.LENGTH_LONG)
					.show();
			mv.invalidate();
		}
	}

	public void mark() {
		if (mv.marking == false) {
			mv.points = po.showedPoints;
			po.lastlevel = mv.getZoomLevel();
			po.center = mv.getMapCenter();
			mv.marking = true;
			Toast.makeText(this, "Turn to MARK mode...", Toast.LENGTH_LONG)
					.show();
			mv.invalidate();
		}
	}

	public void addBadge() {
		if (mv.points.size() > 0)
			mv.setBadge();
		else
			showAlert(null, R.drawable.about, "Can't add any badge!", "OK",
					true);

	}

	public void zoom() {
		mv.displayZoomDialog(screenWidth / 2, screenHeight / 2);
	}

	public void layers() {

	}

	public void save() {

		if (mv.points.size() > 0) {// 已经开始标图，有路径信息可以保存

			// show a dialog to input start place and end place
			savePathDialog = new Dialog(this);

			savePathDialog.setContentView(R.layout.save_path_dialog);
			savePathDialog.setTitle("Save");

			// Log.d(LOG_TAG, "mDialog " + (mDialog == null ? "error" :
			// "ok"));

			EditText etStart = (EditText) savePathDialog
					.findViewById(R.id.start);
			EditText etEnd = (EditText) savePathDialog.findViewById(R.id.end);
			if (navfrom != 0) {
				etStart.setText(start);
				etEnd.setText(end);
			}

			Button bOk = (Button) savePathDialog.findViewById(R.id.pathinfo_ok);
			bOk.setOnClickListener(new OnClickListener() {
				public void onClick(final View v) {
					EditText etStart = (EditText) savePathDialog
							.findViewById(R.id.start);
					EditText etEnd = (EditText) savePathDialog
							.findViewById(R.id.end);
					Bundle bundle = new Bundle();

					if (navfrom != 0) {
						bundle.putInt("action",
								SharePathService.ACTION_EDIT_MYMAP);
						bundle.putLong(Domain.Message._ID, id);
					} else {
						bundle.putInt("action",
								SharePathService.ACTION_NEW_MYMAP);
					}
					bundle.putInt(Domain.Message.TYPE, 3);
					bundle.putString(Domain.Message.START, etStart.getText()
							.toString());
					bundle.putString(Domain.Message.END, etEnd.getText()
							.toString());

					KeyPoint tt;
					String path = "";
					for (int i = 0; i < po.showedPoints.size(); i++) {
						tt = po.showedPoints.get(i);
						path += "" + tt.p.x + "\b" + tt.p.y + "\b"
								+ (tt.info == null ? "" : tt.info) + "\f";
					}
					bundle.putString(Domain.Message.PATH, path);

					bundle.putInt(Domain.Message.LEVEL, mv.getZoomLevel());
					Log.v(LOG_TAG, "zoom level:" + po.zoomlevel);
					
					bundle.putInt(Domain.Message.CENTER + "lat", mv
							.getMapCenter().getLatitudeE6());
					bundle.putInt(Domain.Message.CENTER + "lon", mv
							.getMapCenter().getLongitudeE6());

					startService(new Intent(SharePathMap.this,
							SharePathService.class), bundle);
					savePathDialog.dismiss();
				}
			});

			Button bCancel = (Button) savePathDialog
					.findViewById(R.id.pathinfo_cancel);
			bCancel.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					savePathDialog.cancel();
				}
			});

			savePathDialog.show();
		} else {
			showAlert(null, R.drawable.about, "Nothing saved!", "OK", true);
		}
	}

	public void clean() {
		mv.points = new ArrayList<KeyPoint>();
		mv.invalidate();
	}

	public void about() {
		showAlert("", 0, "SharePath\n\nAuthor:xingye", "OK", true);
	}

	public void send() {
		startSubActivity(new Intent(this,
				org.yexing.android.sharepath.ChooseBuddy.class),
				CODE_CHOOSE_BUDDY);
	}

	public void ask() {
		Intent intent = new Intent(this, Request.class);
		intent.putExtra(Domain.Message.CENTER + "lat", 
				mv.getMapCenter().getLatitudeE6());
		intent.putExtra(Domain.Message.CENTER + "lon", 
				mv.getMapCenter().getLongitudeE6());
		intent.putExtra(Domain.Message.LEVEL, mv.getZoomLevel());
		startActivity(intent);
	}

	public void inbox() {
		startSubActivity(new Intent(this,
				org.yexing.android.sharepath.Inbox.class), CODE_INBOX);
	}

	public void mymaps() {
		startSubActivity(new Intent(this,
				org.yexing.android.sharepath.MyMaps.class), CODE_MYMAPS);
	}

	public void buddies() {
		startSubActivity(new Intent(this,
				org.yexing.android.sharepath.Buddy.class), CODE_BUDDIES);
	}

}
