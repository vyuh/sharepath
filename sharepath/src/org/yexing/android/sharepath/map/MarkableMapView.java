package org.yexing.android.sharepath.map;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.MapView;
import android.util.Log;
import android.view.MotionEvent;

public class MarkableMapView extends MapView {
	int left = 0, top = 0, right = 0, bottom = 0;
	boolean clear = false;
	public int switcher = 0;

	public Path path;
	boolean start = false;
	CornerPathEffect cpe;
	Paint p;
	Bitmap b;
	int delta = 10;
	int lastX = 0, lastY = 0;
	PathOverlay po;
	SharePathMap activity;

	public ArrayList<ToolTip> toolTips;
	
	public MarkableMapView(Context context) {
		super(context);
		activity = (SharePathMap) context;
		cpe = new CornerPathEffect(4);
		p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2);
		p.setPathEffect(cpe);
		
		toolTips = new ArrayList<ToolTip>();
	}

	// @Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;

		Log.d("SharePath", "onLayout L:" + left + " T:" + top + " R:" + right
				+ " B:" + bottom);
		SharePathMap.instance.addPathOverlay();
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// Log.d("SharePath", "MarkableView.onDraw");
	// if (path != null) {
	// canvas.drawPath(path, p);
	// }
	// }

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.d("SharePath", "ontouch");
		if (switcher == 0) {
			Log.d("SharePath", "m");
			return super.onTouchEvent(ev);
		} else {
			int action = ev.getAction();
//			Log.d("SharePath", "n action=" + action + " MotionEvent.ACTION_DOWN =" + MotionEvent.ACTION_DOWN);
			int x = (int) ev.getX();
			int y = (int) ev.getY();
			if (action == MotionEvent.ACTION_DOWN) {
				if (!start) {
					start = true;
					path = new Path();
					path.moveTo(x, y);
					lastX = x;
					lastY = y;
				} else {
					if (Math.abs(lastX - x) > delta / 4
							|| Math.abs(lastY - y) > delta / 4) {
						path.lineTo(x, y);
						lastX = x;
						lastY = y;
					}
				}
				
				ToolTip tt = new ToolTip(new Point(x,y), null);
				toolTips.add(tt);
//			} else if (action == MotionEvent.ACTION_MOVE) {
//				if (Math.abs(lastX - x) > delta || Math.abs(lastY - y) > delta) {
//					path.lineTo(x, y);
//					lastX = x;
//					lastY = y;
//				}
			}
//			Log.d("SharePath", "onTouchEvent L:" + left + " T:" + top + " R:" + right
//					+ " B:" + bottom);
			SharePathMap.instance.addPathOverlay();
			return true;
		}
	}

	// SharePathMap.instance.mapBMP = this.copyWindowBitmap();
	// if (clear) {
	// SharePathMap.instance.turnToDrawableView();
	// clear = false;
	// }

//	public boolean onMotionEvent(MotionEvent event) {
//		int action = event.getAction();
//		int x = (int) event.getX();
//		int y = (int) event.getY();
//		if (action == MotionEvent.ACTION_DOWN) {
//			if (!start) {
//				start = true;
//				path = new Path();
//				path.moveTo(x, y);
//				lastX = x;
//				lastY = y;
//			} else {
//				if (Math.abs(lastX - x) > delta / 4
//						|| Math.abs(lastY - y) > delta / 4) {
//					path.lineTo(x, y);
//					lastX = x;
//					lastY = y;
//				}
//			}
//		} else if (action == MotionEvent.ACTION_MOVE) {
//			if (Math.abs(lastX - x) > delta || Math.abs(lastY - y) > delta) {
//				path.lineTo(x, y);
//				lastX = x;
//				lastY = y;
//			}
//		}
//		invalidate();
//		return true;
//	}
}
