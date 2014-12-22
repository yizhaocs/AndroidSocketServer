package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.androidsocketsserver.AndroidSocketsRecieverForButton.MultiSocketsServerThread;
import com.example.androidsocketsserver.AndroidSocketsRecieverForButton.MyTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
	List<View> viewsList;
	private View movingView = null;
	private AndroidSocketsRecieverForDragAndDrop a = this;
	ConvertorOfJsonObjectToMotionEvent mConvertorOfJsonObjectToMotionEvent = ConvertorOfJsonObjectToMotionEvent.getInstance();
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recieverfordraganddrop);
		View rootView = findViewById(R.id.rootview);
		viewsList = ViewTraversal.travasalViews(rootView);
		for (View v : viewsList) {
			if (v instanceof ImageView)
				Log.d("draganddroplog", String.valueOf(v.getId()));
		}
		findViewById(R.id.myimage1).setOnTouchListener(new OverrideTouchListener());
		
		findViewById(R.id.topleft).setOnDragListener(new MyDragListener());
		findViewById(R.id.topright).setOnDragListener(new MyDragListener());
		findViewById(R.id.bottomleft).setOnDragListener(new MyDragListener());
		findViewById(R.id.bottomright).setOnDragListener(new MyDragListener());
		
		MyTask mSocketsServer = new MyTask();
		mSocketsServer.execute();

	}
	
	protected void dispatchView(final View v, final MotionEvent event) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				// textView1.append(me.toString()+ "\n");
				if (viewsList.contains(v)) {
					Log.d("AAA","v.dispatchTouchEvent(event)");
					v.dispatchTouchEvent(event);
				} else {
					Log.d("AAA","v.onTouchEvent(event)");
					v.onTouchEvent(event);
				}
				// 
			}
		});

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
		//if (movingView == null) {
			PointerCoords outPointerCoords = new PointerCoords();
			event.getPointerCoords(0, outPointerCoords);
			movingView = ViewTraversal.getView(outPointerCoords.x, outPointerCoords.y, viewsList);
			Log.d("movingView.getId():", String.valueOf(movingView.getId()));

			//if (movingView.getId() == R.id.myimage1) {
				ClipData data = ClipData.newPlainText("", "");
				Log.d("movingView", String.valueOf(outPointerCoords.x));
				Log.d("movingView", String.valueOf(outPointerCoords.y));
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(movingView);
				movingView.startDrag(data, shadowBuilder, movingView, 0);
				// movingView.setVisibility(View.INVISIBLE);
//			} else {
//				movingView = null;
//			}
		//}

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
	
	
	public class MyTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			//updateDisplay("Starting task" + "\n");
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
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			//updateDisplay(result);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			// super.onProgressUpdate(values);
			//updateDisplay(values[0]);
		}
	}

	public class MultiSocketsServerThread extends Thread {

		private Socket socket = null;
		private List<View> viewsList;

		public MultiSocketsServerThread(Socket socket, List<View> viewsList) {
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

					View v = ViewTraversal.getView(outPointerCoords.x, outPointerCoords.y, viewsList);
					// Log.d("hahaID", String.valueOf(v.getId()));
					if (me != null && v != null) {
						Log.d("haha", "dispatchTouchEvent");
						// v.dispatchTouchEvent(me);
						a.dispatchView(v, me);
					}

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
}