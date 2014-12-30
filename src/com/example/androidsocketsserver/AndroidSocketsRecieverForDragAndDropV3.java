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
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

public class AndroidSocketsRecieverForDragAndDropV3 extends Activity {
	/* For */
	ConvertorOfJsonObjectToMotionEvent mConvertorOfJsonObjectToMotionEvent = ConvertorOfJsonObjectToMotionEvent.getInstance();
	private AndroidSocketsRecieverForDragAndDropV3 a = this;
	private List<View> viewsList;
	private Boolean isDragging = false;
	/* For */
	private Boolean exit = false;
	/* For */
	BackgroundThreadDrawing v;
	Bitmap ball;
	float x, y;
	float initX;
	float initY;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		/* For SurfaceView Drawing */
		v = new BackgroundThreadDrawing(this);
		v.setOnTouchListener(mOnTouchListener);
		ball = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_large);
		initX = ball.getWidth() / 2;
		initY = ball.getHeight() / 2;
		resetCoordiToDefault();
		setContentView(v);
		viewsList = ViewTraversal.travasalViews(findViewById(R.id.rootview));
		/* For Sockets */
		MyTask mSocketsServer = new MyTask();
		mSocketsServer.execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		v.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		v.resume();
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@SuppressLint("ClickableViewAccessibility")
		public boolean onTouch(View view, MotionEvent motionEvent) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			int action = motionEvent.getAction();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				Log.d("motionEvent", "ACTION_DOWN");
				if (x == initX && y == initY) {
					// updateCoordi(motionEvent);
					isDragging = true;
					return true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				Log.d("motionEvent", "ACTION_MOVE");
				if (isDragging) {
					updateCoordi(motionEvent);
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
				Log.d("motionEvent", "ACTION_UP");
				if (x != initX && y != initY) {
					isDragging = false;
					resetCoordiToDefault();
					return true;
				}
				break;
			default:
				break;
			}
			resetCoordiToDefault();
			return false;
		}
	};
	private void resetCoordiToDefault(){
		x = initX;
		y = initY;
	}
	
	private void updateCoordi(MotionEvent motionEvent) {
		x = motionEvent.getX();
		y = motionEvent.getY();
	}

	protected void dispatchView(final View v, final MotionEvent event) {
		// run the motion event on the UI thread
		this.runOnUiThread(new Runnable() {
			public void run() {
				dispatchTouchEvent(event);
			}
		});
	}

	public class BackgroundThreadDrawing extends SurfaceView implements Runnable {
		Thread t = null;
		SurfaceHolder holder;
		boolean isItOK = false;

		public BackgroundThreadDrawing(Context context) {
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

				// Lock the user interface to prepare the canvas drawing
				Canvas c = holder.lockCanvas();
				// Clear the surfaceview
				// c.drawColor(Color.TRANSPARENT);
				c.drawColor(Color.WHITE);
				// Draw the image
				c.drawBitmap(ball, x - (ball.getWidth() / 2), y - (ball.getHeight() / 2), null);
				// Display the latest drawing to user interface
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

	public class BackgroundThreadSockets extends Thread {

		private Socket socket = null;
		private List<View> viewsList;

		public BackgroundThreadSockets(Socket socket, List<View> viewsList) {
			super("KKMultiServerThread");
			this.socket = socket;
			this.viewsList = viewsList;

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
					Log.d("haha", me.toString());
					Log.d("hahax", String.valueOf(outPointerCoords.x));
					Log.d("hahay", String.valueOf(outPointerCoords.y));
					//
					// View v = ViewTraversal.getView(outPointerCoords.x, outPointerCoords.y, viewsList);
					// // Log.d("hahaID", String.valueOf(v.getId()));
					// if (me != null && v != null) {
					// Log.d("haha", "dispatchTouchEvent");
					// // v.dispatchTouchEvent(me);
					// //a.dispatchView(v, me);
					// //a.dispatchTouchEvent(me);
					//
					// }
					a.dispatchView(null, me);
					// a.dispatchTouchEvent(me);
					// out.println("echo " + inputLine);
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
					new BackgroundThreadSockets(serverSocket.accept(), viewsList).start();
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
}