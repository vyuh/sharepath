package org.yexing.android.sharepath.map;

import android.content.Context;
import android.graphics.Canvas;

import com.google.android.maps.MapView;
import android.util.Log;

public class MarkableMapView extends MapView {
	int left = 0, top = 0, right = 0, bottom = 0;
	boolean clear = false;

	public MarkableMapView(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int windowLeft, int windowTop,
			int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		super
				.onLayout(changed, windowLeft, windowTop, left, top, right,
						bottom);
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;

		Log.d("xingye", "onLayout L:" + left + " T:" + top + " R:" + right
				+ " B:" + bottom);
		SharePathMap.instance.addPathOverlay();
	}

//		SharePathMap.instance.mapBMP = this.copyWindowBitmap();
//		if (clear) {
//			SharePathMap.instance.turnToDrawableView();
//			clear = false;
//		}

}
