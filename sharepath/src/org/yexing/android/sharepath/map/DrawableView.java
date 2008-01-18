package org.yexing.android.sharepath.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class DrawableView extends View {

	Path path;
	boolean start = false;
	CornerPathEffect cpe;
	Paint p;
	Bitmap b;
	int delta = 10;
	int lastX = 0, lastY = 0;

	int left, top;

	public DrawableView(Context c) {
		super(c);

		cpe = new CornerPathEffect(4);
		p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2);
		p.setPathEffect(cpe);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d("xingye", "DrawableView.onDraw");
		canvas.drawBitmap(b, -left, -top, null);
		if (path != null) {
			canvas.drawPath(path, p);
		}
	}

	@Override
	public boolean onMotionEvent(MotionEvent event) {
		int action = event.getAction();
		int x = (int) event.getX();
		int y = (int) event.getY();
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
		} else if (action == MotionEvent.ACTION_MOVE) {
			if (Math.abs(lastX - x) > delta || Math.abs(lastY - y) > delta) {
				path.lineTo(x, y);
				lastX = x;
				lastY = y;
			}
		}
		invalidate();
		return true;
	}
}
