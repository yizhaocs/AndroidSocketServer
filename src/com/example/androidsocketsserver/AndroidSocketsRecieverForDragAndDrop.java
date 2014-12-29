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

public class AndroidSocketsRecieverForDragAndDrop extends Activity {
	
	List<View> viewsList;
	// private View movingView = null;
	private AndroidSocketsRecieverForDragAndDrop a = this;
	ConvertorOfJsonObjectToMotionEvent mConvertorOfJsonObjectToMotionEvent = ConvertorOfJsonObjectToMotionEvent.getInstance();

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recieverfordraganddrop);
		viewsList = ViewTraversal.travasalViews(findViewById(R.id.rootview));

		findViewById(R.id.myimage1).setOnTouchListener(mOnTouchListener);
		findViewById(R.id.myimage1).setOnLongClickListener(mOnLongClickListener);

		findViewById(R.id.topleft).setOnDragListener(new BackgroundViewsDragListener());
		findViewById(R.id.topright).setOnDragListener(new BackgroundViewsDragListener());

		MyTask mSocketsServer = new MyTask();
		mSocketsServer.execute();

	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		Toast toast_1;
		Toast toast_2;
		Toast toast_3;
		
		@SuppressLint("ClickableViewAccessibility")
		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d("motionEvent", "ACTION_DOWN");
				// DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				// view.startDrag(ClipData.newPlainText("", ""), shadowBuilder, view, 0);
				cancelToast(toast_1, toast_2, toast_3);
				trigerDragAndDrop(view);
				showToast(toast_1, Gravity.BOTTOM, "MotionEvent.ACTION_DOWN");
				//
				return true;
			case MotionEvent.ACTION_MOVE:
				Log.d("motionEvent", "ACTION_MOVE");
				cancelToast(toast_1, toast_2, toast_3);
				showToast(toast_2, Gravity.CENTER, "MotionEvent.ACTION_MOVE");
				return true;
			case MotionEvent.ACTION_UP:
				Log.d("motionEvent", "ACTION_UP");
				cancelToast(toast_1, toast_2, toast_3);
				showToast(toast_3, Gravity.TOP, "MotionEvent.ACTION_UP");
				view.setVisibility(View.VISIBLE);
				return true;
			default:
				break;
			}
			return false;
		}
	};

	private void trigerDragAndDrop(View view) {
		DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		view.startDrag(null, shadowBuilder, view, 0);
		view.setVisibility(View.INVISIBLE);
	}

	private void showToast(Toast toast, int position, String info) {
		toast = Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT);
		toast.setGravity(position, 0, 0);
		toast.show();
	}

	private void cancelToast(Toast toast_1, Toast toast_2, Toast toast_3) {

		if (toast_1 != null) {
			toast_1.cancel();
		}
		if (toast_2 != null) {
			toast_2.cancel();
		}
		if (toast_3 != null) {
			toast_3.cancel();
		}
	}

	private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View view) {
			Log.d("XYXYXY", "onLongClick");
			return false;
		}
	};

	protected void dispatchView(final View v, final MotionEvent event) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				dispatchTouchEvent(event);
				// textView1.append(me.toString()+ "\n");
				// if (viewsList.contains(v)) {
				Log.d("AAA", "v.dispatchTouchEvent(event)");
				// v.dispatchTouchEvent(event);
				// } else {
				// Log.d("AAA", "v.onTouchEvent(event)");
				// v.onTouchEvent(event);
				// }
				//
			}
		});
	}

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

	public class MyTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			// updateDisplay("Starting task" + "\n");
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
			// updateDisplay(result);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			// super.onProgressUpdate(values);
			// updateDisplay(values[0]);
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
}