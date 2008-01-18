package org.yexing.android.sharepath.map;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.Overlay;
import com.google.android.maps.Point;
import com.google.android.maps.Overlay.PixelCalculator;

public class PathOverlay extends Overlay {
	Path path;
	Path showedPath;
	Point center;
	Paint p;
	int[] curXYCoords = new int[2];
	int zoomlevel, lastlevel;
	Matrix m = new Matrix();
	
	int left, top, right, bottom;

	public PathOverlay(Path path, Point center, int left, int top, int right, int bottom) {
		// this.path = path;
		// path.offset(100, 100);
		// this.center = center;
		
		//路径的样式
		p = new Paint(Paint.ANTI_ALIAS_FLAG);
		p.setStyle(Paint.Style.STROKE);
		p.setStrokeWidth(2);
		p.setARGB(255, 255, 80, 80);
		p.setPathEffect(new CornerPathEffect(4));
		
		//定位屏幕坐标
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		Log.d("xingye", "L:" + left + " T:" + top + " R:" + right + " B:" + bottom);
		
	}

	public void draw(Canvas canvas, PixelCalculator calculator, boolean shadow) {
//		String s = "Map应用演示 作者：xingye 欢迎访问 androidcn.net";
//		canvas.drawText(s, 0, s.length(), 0, 21, new Paint());
//		s = "使用320x240分辨率 按M,N切换画图模式,按T G缩放";
//		canvas.drawText(s, 0, s.length(), 0, 40, new Paint());
		if (path != null && center != null) {
//			Log.v("xingye", "last:" + lastlevel + " cur:" + zoomlevel);
			
			//根据地图level缩放路径
			m.setPolyToPoly(new float[] { 0, 0, 0, (float)Math.pow(2,lastlevel), (float)Math.pow(2,lastlevel), 0 },
					0,
					new float[] { 0, 0, 0, (float)Math.pow(2,zoomlevel), (float)Math.pow(2,zoomlevel), 0 },
					0,
					3);
			showedPath = new Path();
			showedPath.set(path);
			
			//计算地图中点的屏幕坐标，即路径中点的屏幕坐标
			calculator.getPointXY(center, sXYCoords);
			//String s = "x:" + sXYCoords[0] + " Y:" + sXYCoords[1];
			//canvas.drawText(s, 0, s.length(), 0, 21, new Paint());
			// calculator.getPointXY(this.getCenter(), curXYCoords);

			//移动路径到正确位置，以屏幕中点为基准进行平移
			showedPath.offset((sXYCoords[0])*(float)Math.pow(2,lastlevel)/(float)Math.pow(2,zoomlevel)-(right-left)/2,
					(sXYCoords[1])*(float)Math.pow(2,lastlevel)/(float)Math.pow(2,zoomlevel)-(bottom-top)/2);
			showedPath.transform(m);
			canvas.drawPath(showedPath, p);

		}
	}

}
