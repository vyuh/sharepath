package org.yexing.android.sharepath.map;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.google.android.maps.Overlay;

public class AnchorOverlay extends Overlay {
	BitmapDrawable anchor;
	int x, y, w, h;

	public AnchorOverlay(Drawable d) {
		anchor = (BitmapDrawable) d;
		w = anchor.getIntrinsicWidth();
		h = anchor.getIntrinsicHeight();
	}

	public void draw(Canvas canvas, PixelCalculator calculator, boolean shadow) {
		x = 100;
		y = 50;
		// canvas.clipRect(x, y, x + w, y + h);
		// canvas.clipRect(20, 20, 100, 100);
		// anchor.setBounds(0, 0, w, h);
		anchor.setBounds(x, y, x + w, y + h);
		anchor.draw(canvas);
	}

}
