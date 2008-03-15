package org.yexing.android.sharepath.map;

import org.yexing.android.sharepath.R;

import android.content.Context;
import android.content.Resources;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.OverlayController;
import com.google.android.maps.Point;

public class SharePathMap extends MapActivity {
	/** Called when the activity is first created. */
	// MapView mv;
	MarkableMapView mv;
	MapController mc;
	OverlayController oc;

	Resources r;
	DrawableView dv;

	LocationManager lm;
	Location l;

	Point center;

	int switcher = 0;
	PathOverlay po;
	int zoomlevel = 16;
	
	Bitmap mapBMP = null;

	public static SharePathMap instance;

	int width, height; // 屏幕的宽度和高度

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.NO_STATUS_BAR_FLAG,
		// WindowManager.LayoutParams.NO_STATUS_BAR_FLAG);

		// screen width and height
		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();
		width = d.getWidth();
		height = d.getHeight();

		// 地图
		mv = new MarkableMapView(this);

		setContentView(mv);

		r = mv.getContext().getResources();
		mc = mv.getController();
		// mc.zoomTo(zoomlevel);

		// 得到当前位置的gps坐标
		lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		l = lm.getCurrentLocation("gps");
		// List list = lm.getProviders();

		// 重新定位地图到当前位置
		mc.centerMapTo(new Point((int) (l.getLatitude() * 1e6), (int) (l
				.getLongitude() * 1e6)), true);

		center = mv.getMapCenter();
		zoomlevel = mv.getZoomLevel();

		// overlay controller
//		oc = mv.createOverlayController();

		// 绘图界面
		dv = new DrawableView(this);

		 //增加显示路径的overlay
		 oc = mv.createOverlayController();
		 Log.d("SharePath", "W:" + mv.right + " H:" + mv.bottom);
		 po = new PathOverlay(dv.path, center, 0, 46, 480, 320);
		 po.zoomlevel = zoomlevel;
		 po.lastlevel = zoomlevel;
		 oc.add(po, true);

		// Criteria cr = new Criteria();
		instance = this;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_S:
			mv.toggleSatellite();
			break;
		case KeyEvent.KEYCODE_K:
			Point p = mv.getMapCenter();
			// p = new Point((int) l.getLatitude(), (int) l.getLongitude());
			// showAlert("位置", "La:" + p.getLatitudeE6() + " Lo:" +
			// p.getLongitudeE6(), "OK", true);
			// AnchorOverlay ao = new
			// AnchorOverlay(r.getDrawable(R.drawable.icon));
			GraphicOverlay go = new GraphicOverlay(r
					.getDrawable(R.drawable.anchor), p);
//			OverlayController oc = mv.createOverlayController();
			// oc.add(ao, true);
			oc.add(go, true);
			mv.invalidate();
			break;
		case KeyEvent.KEYCODE_M: //切换到地图界面
			if (mv.switcher == 1) {
//				po.path = dv.path;
//				po.center = mv.getMapCenter();
//				po.lastlevel = zoomlevel;
//				setContentView(mv);
				mv.switcher = 0;
			}

			break;
		case KeyEvent.KEYCODE_N: //切换到标图界面
			int i = 1;
			if (mv.switcher == 0) {
//				Log.d("SharePath", "" + i++);
//				dv.left = width-mv.right;
//				dv.top = height-mv.bottom;
//				Log.d("SharePath", "" + i++);
//				dv.path = po.showedPath;
//				Log.d("SharePath", "" + i++);
////				oc.clear();
////				mv.invalidate();
//			//	po = null;
//				//dv.b = mv.copyWindowBitmap();
//				Log.d("SharePath", "" + i++);
//				center = mv.getMapCenter();
//				Log.d("SharePath", "" + i++);
//				setContentView(dv);
//				Log.d("SharePath", "" + i++);
				mv.switcher = 1;
			}
			// dv.invalidate();
			break;
		case KeyEvent.KEYCODE_O:
			showAlert("位置", 1, "La:" + mv.getLatitudeSpan() + " Lo:"
					+ mv.getLongitudeSpan(), "OK", true);
			Point pp = mv.getMapCenter();
			showAlert("位置2", 1, "La:" + pp.getLatitudeE6() + " Lo:"
					+ pp.getLongitudeE6(), "OK", true);
			break;
		case KeyEvent.KEYCODE_T:
			if (switcher == 0) {
				po.zoomlevel = zoomlevel < 21 ? ++zoomlevel : zoomlevel;
				mc.zoomTo(zoomlevel);
			}
			break;
		case KeyEvent.KEYCODE_G:
			if (switcher == 0) {
				po.zoomlevel = zoomlevel > 1 ? --zoomlevel : zoomlevel;
				mc.zoomTo(zoomlevel);
			}
			break;
		case KeyEvent.KEYCODE_C: //清除当前路径
			oc.clear();
			po.path = new Path();
//			switch(switcher) {
//			case 0: //map
//				mv.invalidate();
//				break;
//			case 1: //mark
//				dv.b = null;
//				mv.clear = true;
//				setContentView(mv);
//				dv.b = mv.copyWindowBitmap();
//				dv.path = new Path();
//				dv.start = false;
//			//	setContentView(dv);
//				dv.invalidate();
//				break;
//			}
			setContentView(mv);
			mv.invalidate();
			switcher = 0;
			break;
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		}
		return true;
	}

	public boolean addPathOverlay() {
		if (po == null) {
			po = new PathOverlay(dv.path, center, width-mv.right, height-mv.bottom, width, height);
			po.zoomlevel = zoomlevel;
			po.lastlevel = zoomlevel;
		}
		oc.add(po, true);
		return true;
	}
	public boolean turnToDrawableView() {
		dv.b = mv.copyWindowBitmap();
		dv.path = new Path();
		dv.start = false;
		setContentView(dv);
		switcher = 1;
		return true;
	}
}
