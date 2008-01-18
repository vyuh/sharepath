package org.yexing.android.sharepath.map;

import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.google.android.maps.Overlay;
import com.google.android.maps.Point;

public class GraphicOverlay extends Overlay {
	BitmapDrawable bmp;
	static int w, h;
	Point p;

	public GraphicOverlay(Drawable d, Point p) {
		bmp = (BitmapDrawable) d;
		this.p = p;
		w = bmp.getIntrinsicWidth();
		h = bmp.getIntrinsicHeight();
	}

	public void draw(Canvas canvas, PixelCalculator calculator, boolean shadow) {
		calculator.getPointXY(p, sXYCoords);
		bmp.setBounds(sXYCoords[0] - w / 2, sXYCoords[1] - h,
				sXYCoords[0] + w / 2, sXYCoords[1]);
		bmp.setAlpha(70);
		bmp.draw(canvas);
		
		/*
		RectF oval = new RectF(xyCoords[0], xyCoords[1],
				xyCoords[0] + 5, xyCoords[1] + 5);

		Paint paint = new Paint();
		paint.setARGB(200, 255, 0, 0);
		canvas.drawOval(oval, paint);
		*/
	}

	public Point getCenter() {
		return p;
	}

	public boolean dispatchMotionEvent(MotionEvent ev) {
		return false;
	}

}
