package com.example.androidsocketsserver;

import java.util.List;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AndroidSocketsRecieverForDragAndDrop extends Activity {
	private static final int NONE = 0;
	private static final int MOVE = 1;
	private int mode = NONE;
	List<View> viewlist;
	private View movingView = null;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recieverfordraganddrop);
		View rootView = findViewById(R.id.rootview);
		viewlist = ViewTraversal.travasalViews(rootView);
		for (View v : viewlist) {
			if (v instanceof ImageView)
				Log.d("draganddroplog", String.valueOf(v.getId()));
		}
		findViewById(R.id.myimage1).setOnTouchListener(new OverrideTouchListener());
		
		findViewById(R.id.topleft).setOnDragListener(new MyDragListener());
		findViewById(R.id.topright).setOnDragListener(new MyDragListener());
		findViewById(R.id.bottomleft).setOnDragListener(new MyDragListener());
		findViewById(R.id.bottomright).setOnDragListener(new MyDragListener());

	}

	class MyDragListener implements OnDragListener {
		Drawable enterShape = getResources().getDrawable(R.drawable.shape_droptarget);
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);

		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
				// v.setVisibility(View.VISIBLE);

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				// v.setVisibility(View.INVISIBLE);
				v.setBackgroundDrawable(normalShape);
			default:
				break;
			}

			movingView = null;

			return true;
		}
	}

	private final class OverrideTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, view, 0);
				view.setVisibility(View.INVISIBLE);

				return true;
			} else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
				view.setVisibility(View.VISIBLE);
				return true;
			} else {
				return false;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (movingView == null) {
			PointerCoords outPointerCoords = new PointerCoords();
			event.getPointerCoords(0, outPointerCoords);
			movingView = ViewTraversal.getView(outPointerCoords.x, outPointerCoords.y, viewlist);
			Log.d("movingView.getId():", String.valueOf(movingView.getId()));

			if (movingView.getId() == R.id.myimage1) {
				ClipData data = ClipData.newPlainText("", "");
				Log.d("movingView", String.valueOf(outPointerCoords.x));
				Log.d("movingView", String.valueOf(outPointerCoords.y));
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(movingView);
				movingView.startDrag(data, shadowBuilder, movingView, 0);
				// movingView.setVisibility(View.INVISIBLE);
			} else {
				movingView = null;
			}
		}

		return super.onTouchEvent(event);
	}
	// private final class OverrideTouchListener implements OnTouchListener {
	// @Override
	// public boolean onTouch(View view, MotionEvent event) {
	// if (event.getAction() == MotionEvent.ACTION_DOWN) {
	//
	//
	// PointerCoords outPointerCoords = new PointerCoords();
	// event.getPointerCoords(0, outPointerCoords);
	// movingView = ViewTraversal.getView(outPointerCoords.x, outPointerCoords.y, viewlist);
	// Log.d("movingView", String.valueOf(outPointerCoords.x));
	// Log.d("movingView", String.valueOf(outPointerCoords.y));
	// ClipData data = ClipData.newPlainText("", "");
	// DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
	// movingView.startDrag(data, shadowBuilder, view, 0);
	// movingView.setVisibility(View.INVISIBLE);
	// return true;
	// } else {
	// return false;
	// }
	// }
	// }
}