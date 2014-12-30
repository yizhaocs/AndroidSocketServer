package com.example.androidsocketsserver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;

public class SurfaceViewExample extends Activity {
	OurView v;
	Bitmap ball;
	float x, y;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		v = new OurView(this);
		v.setOnTouchListener(mOnTouchListener);
		ball = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_large);
		x = 0;
		y = 0;
		setContentView(v);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		v.pause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		v.resume();
	}

	public class OurView extends SurfaceView implements Runnable {
		Thread t = null;
		SurfaceHolder holder;
		boolean isItOK = false;

		public OurView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			holder = getHolder();
		}

		@Override
		public void run() {
			while (isItOK) {
				// perform canvas drawing
				if (!holder.getSurface().isValid()) {
					continue;
				}
				Canvas c = holder.lockCanvas();
				c.drawBitmap(ball, x - (ball.getWidth() / 2), y - (ball.getHeight() / 2), null);
				holder.unlockCanvasAndPost(c);
			}
		}

		public void pause() {
			isItOK = false;
			while (true) {
				try {
					t.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;

			}
			t = null;
		}

		public void resume() {
			isItOK = true;
			t = new Thread(this);
			t.start();
		}
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@SuppressLint("ClickableViewAccessibility")
		public boolean onTouch(View view, MotionEvent motionEvent) {

			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d("motionEvent", "ACTION_DOWN");
				x = motionEvent.getX();
				y = motionEvent.getY();
				return true;
			case MotionEvent.ACTION_MOVE:
				Log.d("motionEvent", "ACTION_MOVE");
				x = motionEvent.getX();
				y = motionEvent.getY();
				return true;
			case MotionEvent.ACTION_UP:
				Log.d("motionEvent", "ACTION_UP");
				x = motionEvent.getX();
				y = motionEvent.getY();
				return true;
			default:
				break;
			}
			return false;
		}
	};

}
