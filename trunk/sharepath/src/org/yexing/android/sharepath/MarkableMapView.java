package org.yexing.android.sharepath;

import java.util.ArrayList;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;


import com.google.android.maps.MapView;
import com.google.android.maps.Point;

import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author xingye
 *
 */
public class MarkableMapView extends MapView {
	private static final String LOG_TAG = "SharePath";

	int left = 0, top = 0, right = 0, bottom = 0;

	boolean marking = false; //是否处在标图状态
	
	boolean clear = false;

	public Path path;
	CornerPathEffect cpe;
	Paint p;
	Bitmap b;
	int delta = 10;
	int lastX = 0, lastY = 0;
	
	//可能需要通过context取回po，比如在onlayout中，暂时保留
//	PathOverlay po;
	SharePathMap context;


	public ArrayList<KeyPoint> points;//路径上的转折点
	Dialog badgeDialog; //用来输入提示信息的对话框
	int pindex; // 当前点的index
	
	int lastZoomLevel;
	
	public MarkableMapView(Context context, AttributeSet attrs, Map inflateParams) {
		super(context, attrs, inflateParams);

		Log.v(LOG_TAG, "MarkableMapView");
		
		this.context = (SharePathMap)context;
		
		//路径样式
		cpe = new CornerPathEffect(4);
		p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2);
		p.setPathEffect(cpe);

		points = new ArrayList<KeyPoint>();
//		if(!isEdgeZooming())
//			toggleEdgeZooming();
//			toggleShowMyLocation();
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
		Log.v(LOG_TAG, "left:" + left + " top:" + top
				+ " right:" + right + " bottom:" + bottom);
	}
	
	
	float degrees = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		canvas.rotate(degrees,right/2, bottom/2);
		super.onDraw(canvas);
	}



	//	boolean bZoom = false;
	boolean bMoved = false; //用户是否采取了移动操作
	boolean bBorderLR = false; //action的位置是否在屏幕左右两侧边缘
	boolean bBorderTB = false; //action的位置是否在屏幕上下边缘
	int borderSize = 20; //定义边缘的宽度，宽度以内的即属于屏幕边缘
	int oldX, oldY; //记录down操作发生时的位置，以便在up操作发生时进行比较
	Point pCenter; //down操作发生时的地图中点
	int latspan;
	int lonspan;
	
	long lastClick = 0; //上次点击的时间
	long clickDurence = 300; //两次点击间隔的时间(ms)，小于这个时间视为双击
	long longPress = 180;
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		
			int action = ev.getAction();
			int x = (int) ev.getX();
			int y = (int) ev.getY();
//			Log.v(LOG_TAG, "action:" + action + " x:" + x + " y:" + y);
			
			latspan = getLatitudeSpan();
			lonspan = getLongitudeSpan();
//			Log.v(LOG_TAG, "lat:" + latspan + " lon:" + lonspan);

			if(action == MotionEvent.ACTION_DOWN) {
				oldX = x;
				oldY = y;
				bMoved = false;
				pCenter = getMapCenter();
				
				//检测是否点击在边缘
				if(right - x < borderSize || x < borderSize) {
					bBorderLR = true;
				} else {
					bBorderLR = false;
				}								
				if(bottom - y < borderSize || y < borderSize) {
					bBorderTB = true;
				} else {
					bBorderTB = false;
				}								
			}
			if(action == MotionEvent.ACTION_MOVE) {
				bMoved = true;
				
				if(right - x > borderSize && x > borderSize) {
					bBorderLR = false;
				}
				if(bottom - y > borderSize && y > borderSize) {
					bBorderTB = false;
				}
				if(!bBorderLR) {
					int deltaX = lonspan / right * (x - oldX);
					int deltaY = latspan / bottom * (y - oldY);
//					Log.v(LOG_TAG, "dx:" + deltaX + " dy:" + deltaY);
					
					getController().animateTo(new Point(pCenter.getLatitudeE6() + deltaY,
							pCenter.getLongitudeE6() - deltaX));//, true);
					
				}
			}
			
			if (action == MotionEvent.ACTION_UP) {
//				Log.d(LOG_TAG, "UP x:" + x + " y:" + y + " R:"	+ right + " B:" + bottom);
				if(bBorderLR && bMoved) {
					lastZoomLevel = getZoomLevel();
					
//					if(oldY > y) {
						getController().zoomTo(getZoomLevel()
								- (oldY - y)/borderSize);
//					} 
					
					// 暂时不再作其他处理了
//					else {
//						getController().zoomTo(getZoomLevel()
//								+ (y - oldY)/borderSize);						
//					}
					
				} else if(bBorderTB && bMoved) {
					degrees += 10 * (x - oldX)/borderSize;
				} else if(!bMoved) {
					if(ev.getEventTime() - ev.getDownTime() < longPress) { // 点击 非长按
						if(ev.getEventTime() - lastClick > clickDurence) { // 单击
							KeyPoint tt = new KeyPoint(screenToGeo(x, y), null);
							points.add(tt);	
						} else { // 双击
							if(points.size()==1) {
								points.remove(0);
							} else {
								points.remove(points.size()-1);
								points.remove(points.size()-1);
							}
							
						}
					} else { // 长按 在已有点上 添加badge 否则send,save,clean
						pindex = getNearbyPoint(screenToGeo(x, y));
						Log.v(LOG_TAG, "point:" + pindex);
						if(pindex >-1) {
							setBadge();
						} else {
							
						}
					}
//					Log.v(LOG_TAG, "event:" + ev.getEventTime() + " last:" + lastClick);
				}
				invalidate();
				lastClick = ev.getEventTime();
			}
			return true;

	}

	
	
//	@Override
//	public boolean onLongPress(float x, float y) {
//		// TODO Auto-generated method stub
//		pindex = getNearbyPoint(screenToGeo((int)x, (int)y));
//		if(pindex >-1) {
//			setBadge();
//		} else {
//			
//		}
//		return true; //super.onLongPress(x, y);
//	}

	/**
	 * 设置工具提示
	 */
	public void setBadge() {
		badgeDialog = new Dialog(context);
		badgeDialog.setContentView(R.layout.set_tooltip_dialog);
		badgeDialog.setTitle(context.getString(R.string.tooltip_dialog));

		Button bOk = (Button) badgeDialog.findViewById(R.id.tooltip_ok);
		bOk.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				EditText et = (EditText) badgeDialog.findViewById(R.id.tooltip);
				//Log.d("SharePath", "et " + (et == null ? "error" : "ok"));
				KeyPoint tt = points.get(pindex==-1?points.size()-1:pindex);
				//Log.d("SharePath", "tt " + (tt == null ? "error" : "ok"));
				tt.info = new String(et.getText().toString());
				//Log.d("SharePath", "text " + tt.info);
				
				invalidate();
				badgeDialog.dismiss();
			}
		});

		Button bCancel = (Button) badgeDialog.findViewById(R.id.tooltip_cancel);
		bCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				badgeDialog.cancel();
			}
		});

		badgeDialog.show();

	}
	
	public Point screenToGeo(int x, int y) {
		
		int deltaX = lonspan / right * (x - right/2);
		int deltaY = latspan / bottom * (y - bottom/2);
		Log.v(LOG_TAG, "screenToGeo: deltaX:" + deltaX + " deltaY:" + deltaY);

		Point geoPoint = new Point(this.getMapCenter().getLatitudeE6() - deltaY, 
				this.getMapCenter().getLongitudeE6() + deltaX);
		
		return geoPoint;
	}
	
	public int getNearbyPoint(Point p) {
		int deltaLat = latspan / bottom * 15;
		int deltaLon = lonspan / right * 15;
		int lat = p.getLatitudeE6();
		int lon = p.getLongitudeE6();
		for(int i=0; i<points.size(); i++) {
			int curlat = points.get(i).point.getLatitudeE6();
			int curlon = points.get(i).point.getLongitudeE6();
			if(curlat>lat-deltaLat && curlat<lat+deltaLat
					&& curlon > lon - deltaLon && curlon < lon + deltaLon)
				return i;
		}
		return -1;
		
	}
	
}
