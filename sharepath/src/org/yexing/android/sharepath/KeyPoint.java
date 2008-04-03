package org.yexing.android.sharepath;

import com.google.android.maps.Point;


class KeyPoint {
	Point point;
	String info;

	KeyPoint() {
		
	}
	
	KeyPoint(Point p, String info) {
		this.point = p;
		this.info = info;
	}
}
