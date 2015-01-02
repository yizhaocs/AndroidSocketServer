package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AndroidSocketsRecieverForDragAndDropSetViewV16 extends Activity {
	private Boolean exit = false;
	private Boolean isDragging = false;

	private List<View> viewsList;
	// private View movingView = null;
	private AndroidSocketsRecieverForDragAndDropSetViewV16 a = this;
	ConvertorOfJsonObjectToMotionEvent mConvertorOfJsonObjectToMotionEvent = ConvertorOfJsonObjectToMotionEvent.getInstance();
	int dragingViewID;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recieverfordraganddrop);
		findViewById(R.id.myimage1).setOnTouchListener(mOnTouchListener);
		MyTask mSocketsServer = new MyTask();
		mSocketsServer.execute();

	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@SuppressLint("ClickableViewAccessibility")
		public boolean onTouch(View view, MotionEvent motionEvent) {
			ViewGroup rootView = (ViewGroup) findViewById(R.id.rootview);
			int x = (int) (motionEvent.getRawX() - view.getWidth() / 2);
			int y = (int) (motionEvent.getRawY() - view.getHeight() - view.getHeight() / 2);
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d("motionEvent", "ACTION_DOWN");
				isDragging = true;
				dragingViewID = view.getId();
				ViewGroup firstOuterLayout = (ViewGroup) view.getParent();
				firstOuterLayout.removeView(view);
				rootView.addView(view);
				return true;
			case MotionEvent.ACTION_MOVE:
				Log.d("motionEvent", "ACTION_MOVE");
				if (isDragging) {
					view.setX(x);
					view.setY(y);
					return true;
				} else {
					return false;
				}
			case MotionEvent.ACTION_UP:
				Log.d("motionEvent", "ACTION_UP");
				isDragging = false;
				rootView.removeView(view);
				View underView = findView(x, y, rootView);
				LinearLayout underLayout = (LinearLayout) underView;
				if (underLayout != null) {
					ImageView iv = new ImageView(a);
					iv.setBackgroundResource(R.drawable.ic_launcher_large);
					iv.setId(view.getId());
					iv.setLayoutParams(new LayoutParams(-2, -2));
					underLayout.addView(iv);
					findViewById(R.id.myimage1).setOnTouchListener(mOnTouchListener);
				}
				return true;
			default:
				break;
			}
			return false;
		}
	};

	/* Reference: http://stackoverflow.com/questions/10959400/how-to-find-element-in-view-by-coordinates-x-y-android */
	private View findView(int x, int y, ViewGroup rootView) {
		for (int _numChildren = 0; _numChildren < rootView.getChildCount(); _numChildren++) {
			View _child = rootView.getChildAt(_numChildren);
			Rect _bounds = new Rect();
			_child.getHitRect(_bounds);
			if (_bounds.contains(x, y)) {
				return _child;
			}
		}
		return null;
	}

	protected void dispatchView(final View v, final MotionEvent event) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				dispatchTouchEvent(event);
			}
		});
	}

	public class MyTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			
		};

		@SuppressWarnings("resource")
		protected String doInBackground(String... params) {
			try {
				int portNumber = 4444;
				boolean listening = true;
				ServerSocket serverSocket = new ServerSocket(portNumber);
				while (listening) {
					new MultiSocketsServerThread(serverSocket.accept(), viewsList).start();
				}
			} catch (IOException e) {
				Log.e("error", "IOException:" + e.getMessage());
			}
			return "Task complete";
		}

		@Override
		protected void onPostExecute(String result) {

		}

		@Override
		protected void onProgressUpdate(String... values) {

		}
	}

	@Override
	public void onBackPressed() {
		if (exit) {
			finish(); // finish activity
		} else {
			Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
			exit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exit = false;
				}
			}, 3 * 1000);
		}
	}

	public class MultiSocketsServerThread extends Thread {
		private Socket socket = null;

		public MultiSocketsServerThread(Socket socket, List<View> viewsList) {
			super("KKMultiServerThread");
			this.socket = socket;
		}

		@SuppressLint("NewApi")
		public void run() {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					JSONObject jo = new JSONObject(inputLine);
					Log.d("whatf", jo.toString());
					MotionEvent me = mConvertorOfJsonObjectToMotionEvent.createMotionEvent(jo);
					PointerCoords outPointerCoords = new PointerCoords();
					me.getPointerCoords(0, outPointerCoords);
					a.dispatchView(null, me);
					if (inputLine.equals("Bye")) {
						break;
					}
				}
				socket.close();
			} catch (IOException e) {
				Log.e("error", "IOException:" + e.getMessage());
			} catch (JSONException e) {
				Log.e("error", "JSONException:" + e.getMessage());

			}
		}
	}
}
