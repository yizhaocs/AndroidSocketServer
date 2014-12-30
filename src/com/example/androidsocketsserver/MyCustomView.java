package com.example.androidsocketsserver;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCustomView extends LinearLayout {
	public MyCustomView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setOrientation(LinearLayout.VERTICAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		TextView tv1 = new TextView(context);
		tv1.setText("HELLO");
		addView(tv1);

		TextView tv2 = new TextView(context);
		tv2.setText("WORLD");
		addView(tv2);
	}
}
