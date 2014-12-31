package com.example.androidsocketsserver;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ViewTraversal {
	public static List<View> travasalViews(View rootView) {
		List<View> res = new ArrayList<View>();
		//res.add(rootView);
		recursion(rootView, res);
		return res;
	}

	public static boolean recursion(View rootView, List<View> res) {
		if (rootView == null || (rootView instanceof LinearLayout == false && rootView instanceof RelativeLayout == false && rootView instanceof GridLayout == false)) {
			return false;
		} else {
			for (int i = 0; i < ((ViewGroup) rootView).getChildCount(); i++) {
				View nextChild = ((ViewGroup) rootView).getChildAt(i);
				//Log.d("views", String.valueOf(nextChild.getId()));
				//if ((nextChild instanceof LinearLayout == false && nextChild instanceof RelativeLayout == false && nextChild instanceof GridLayout == false)) 
					res.add(nextChild);
				
				if (recursion(nextChild, res)) {
					return true;
				}
			}
			return false;
		}
	}

	public static View getView(float x, float y, List<View> views) {
		for (View v : views) {
			if (isPointInsideView(x, y, v)) {
				return v;
			}
		}
		return null;
	}

	public static boolean isPointInsideView(float x, float y, View view) {
		int location[] = new int[2];
		view.getLocationOnScreen(location);
		int viewX = location[0];
		int viewY = location[1];

		// point is inside view bounds
		if ((x > viewX && x < (viewX + view.getWidth())) && (y > viewY && y < (viewY + view.getHeight()))) {
			return true;
		} else {
			return false;
		}
	}
}
