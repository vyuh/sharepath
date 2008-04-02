package org.yexing.android.sharepath;

import java.util.ArrayList;

import android.content.Resources;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

import com.google.android.maps.Overlay;
import com.google.android.maps.Point;

public class PathOverlay extends Overlay {
	private static final String LOG_TAG = "SharePath";

	public Path showedPath;
	public ArrayList<KeyPoint> showedPoints;

	Point center;
	Paint p, pfont;
	int[] curXYCoords = new int[2];
	int zoomlevel, lastlevel;
	Matrix m = new Matrix();
	Resources r;
	MarkableMapView mv;

	// int left, top, right, bottom;

	// 标志图片
	int labelWidth, labelHeight;
	BitmapDrawable startLabel;
	BitmapDrawable infoLabel;
	BitmapDrawable endLabel;

	BitmapDrawable stateLogo = null;
	String start, end;

	public PathOverlay(Resources r, MarkableMapView mv, int screenWidth,
			int screenHeight) {

		Log.v(LOG_TAG, "PathOverlay");
		
		this.mv = mv;

		// 载入显示转折点的三个标志label
		this.r = r;
		startLabel = (BitmapDrawable) r.getDrawable(R.drawable.greenstar);
		infoLabel = (BitmapDrawable) r.getDrawable(R.drawable.yellowstar);
		endLabel = (BitmapDrawable) r.getDrawable(R.drawable.redstar);
		labelWidth = 10; // startLabel.getIntrinsicWidth();
		labelHeight = 10; // startLabel.getIntrinsicHeight();

		this.center = mv.getMapCenter();
		this.zoomlevel = mv.getZoomLevel();
		this.lastlevel = zoomlevel;

		// 路径的样式
		p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2);
		p.setARGB(255, 255, 80, 80);
		p.setPathEffect(new CornerPathEffect(4));
		// 文字样式
		pfont = new Paint(Paint.UNDERLINE_TEXT_FLAG);
		pfont.setTextSize(16);
		pfont.setXfermode(new Xfermode());
	}

	public void draw(Canvas canvas, PixelCalculator calculator, boolean shadow) {
		if (mv.marking) {
			stateLogo = (BitmapDrawable) r.getDrawable(R.drawable.mark);
		} else {
			stateLogo = (BitmapDrawable) r.getDrawable(R.drawable.browse);
		}
		canvas.drawBitmap(stateLogo.getBitmap(), mv.left + 10, mv.top + 10,
				pfont);
		if (start != null) {
			canvas.drawText("From:" + start, mv.left + 60, mv.top + 25, pfont);
			canvas.drawText("To:" + end, mv.left + 60, mv.top + 45, pfont);
		}

		if (mv.points != null) {
			zoomlevel = mv.getZoomLevel();
			Log.v(LOG_TAG, "drawpath last:" + lastlevel + " cur:" +
					zoomlevel);
			// Log.i("SharePath", "points count:" + mv.points.size());

			// 计算地图中点的屏幕坐标，即路径中点的屏幕坐标
			calculator.getPointXY(center, sXYCoords);
			// Log.d("SharePath", "center x:" + center.getLatitudeE6() + "
			// center Y:" + center.getLongitudeE6());
			// Log.d("SharePath", "x:" + sXYCoords[0] + " Y:" + sXYCoords[1]);

			// 移动路径到正确位置，以屏幕中点为基准进行平移
			float xoffset = (sXYCoords[0]) * (float) Math.pow(2, lastlevel)
					/ (float) Math.pow(2, zoomlevel) - mv.right / 2;
			float yoffset = (sXYCoords[1]) * (float) Math.pow(2, lastlevel)
					/ (float) Math.pow(2, zoomlevel) - mv.bottom / 2;
			// Log.d("SharePath", "ox:" + xoffset + " oy:" + yoffset);

			showedPath = new Path();
			showedPoints = new ArrayList<KeyPoint>();

			// 根据地图level缩放路径
			m.setPolyToPoly(new float[] { 0, 0, 0,
					(float) Math.pow(2, lastlevel),
					(float) Math.pow(2, lastlevel), 0 }, 0, new float[] { 0, 0,
					0, (float) Math.pow(2, zoomlevel),
					(float) Math.pow(2, zoomlevel), 0 }, 0, 3);
			// showedPath.transform(m);
			// canvas.drawPath(showedPath, p);

			// 偏移量也得转换
			float[] dst = new float[2];
			m.mapPoints(dst, new float[] { xoffset, yoffset });
			int xoffset2 = (int) dst[0];
			int yoffset2 = (int) dst[1];

			for (int i = 0; i < mv.points.size(); i++) {
				KeyPoint tt = mv.points.get(i);
				int x = tt.p.x;
				int y = tt.p.y;

				// 转换坐标
				dst = new float[2];
				m.mapPoints(dst, new float[] { x, y });
				int x2 = (int) dst[0];
				int y2 = (int) dst[1];

				KeyPoint tt2 = new KeyPoint(new android.graphics.Point(x2
						+ xoffset2, y2 + yoffset2), tt.info);
				showedPoints.add(tt2);

				String info = tt.info;
				if (i == 0) {
					startLabel.setBounds(x2 - labelWidth / 2 + (int) xoffset2,
							y2 - labelHeight / 2 + (int) yoffset2, x2
									+ labelWidth + (int) xoffset2, y2
									+ labelHeight + (int) yoffset2);
					startLabel.draw(canvas);
					showedPath.moveTo(x2, y2);

				} else if (i == mv.points.size() - 1) {
					endLabel
							.setBounds(x2 - labelWidth / 2 + (int) xoffset2, y2
									- labelHeight / 2 + (int) yoffset2, x2
									+ labelWidth + (int) xoffset2, y2
									+ labelHeight + (int) yoffset2);
					endLabel.draw(canvas);
					showedPath.lineTo(x2, y2);
				} else {
					infoLabel.setBounds(x2 - labelWidth / 2 + (int) xoffset2,
							y2 - labelHeight / 2 + (int) yoffset2, x2
									+ labelWidth + (int) xoffset2, y2
									+ labelHeight + (int) yoffset2);
					infoLabel.draw(canvas);
					showedPath.lineTo(x2, y2);
				}
				if (info != null) {
					canvas.drawText(info, 0, info.length(), x2 + 15
							+ (int) xoffset2, y2 - 5 + (int) yoffset2, pfont);
				}
			}
			showedPath.offset(xoffset2, yoffset2);
			canvas.drawPath(showedPath, p);
		}
	}
}
