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
import android.app.Activity;
import android.content.ClipData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AndroidSocketsRecieverForDragAndDropV1 extends Activity {
	private Boolean exit = false;
	List<View> viewsList;
	// private View movingView = null;
	private AndroidSocketsRecieverForDragAndDropV1 a = this;
	ConvertorOfJsonObjectToMotionEvent mConvertorOfJsonObjectToMotionEvent = ConvertorOfJsonObjectToMotionEvent.getInstance();

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recieverfordraganddrop);
		findViewById(R.id.myimage1).setOnTouchListener(mOnTouchListener);
		findViewById(R.id.topleft).setOnDragListener(new BackgroundViewsDragListener());
		findViewById(R.id.topright).setOnDragListener(new BackgroundViewsDragListener());

		MyTask mSocketsServer = new MyTask();
		mSocketsServer.execute();

	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@SuppressLint("ClickableViewAccessibility")
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d("motionEvent", "ACTION_DOWN");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				ClipData data = ClipData.newPlainText("","");
				view.startDrag(data, shadowBuilder, view, 0);
				view.setVisibility(View.INVISIBLE);
				return true;
			case MotionEvent.ACTION_MOVE:
				Log.d("motionEvent", "ACTION_MOVE");
				return true;
			case MotionEvent.ACTION_UP:
				Log.d("motionEvent", "ACTION_UP");
				return true;
			default:
				break;
			}
			return false;
		}
	};

	class BackgroundViewsDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View layoutview, DragEvent dragEvent) {
			switch (dragEvent.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				Log.d("dragEvent", "Drag event started");
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.d("dragEvent", "Drag event entered into " + layoutview.toString());
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				Log.d("dragEvent", "Drag event exited from " + layoutview.toString());
				break;
			case DragEvent.ACTION_DROP:
				Log.d("dragEvent", "Dropped");
				// Dropped, reassign View to ViewGroup
				View dragView = (View) dragEvent.getLocalState();
				ViewGroup owner = (ViewGroup) dragView.getParent();
				owner.removeView(dragView);
				LinearLayout container = (LinearLayout) layoutview;
				container.addView(dragView);
				dragView.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				Log.d("dragEvent", "Drag ended");
				break;
			default:
				break;
			}
			return true;
		}
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
			// super.onPreExecute();
		};

		@SuppressWarnings("resource")
		protected String doInBackground(String... params) {
			try {
				int portNumber = 4444;
				boolean listening = true;
				ServerSocket serverSocket = new ServerSocket(portNumber);
				while (listening) {
					new MultiSocketsServerThread(serverSocket.accept()).start();
				}
			} catch (IOException e) {
				Log.e("error", "IOException:" + e.getMessage());
			}
			return "Task complete";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			// super.onProgressUpdate(values);
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
		
		public MultiSocketsServerThread(Socket socket) {
			super("KKMultiServerThread");
			this.socket = socket;
		}

		@SuppressLint("NewApi")
		public void run() {
			try {
				// PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String inputLine;

				// out.println("connection is setup between server and client");

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