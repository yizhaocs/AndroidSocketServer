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
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AndroidSocketsRecieverForDragAndDrop extends Activity {

	private List<View> viewsList;
	
	private AndroidSocketsRecieverForDragAndDrop a = this;
	ConvertorOfJsonObjectToMotionEvent mConvertorOfJsonObjectToMotionEvent = ConvertorOfJsonObjectToMotionEvent.getInstance();
	private View movingView = null;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recieverfordraganddrop);

		// viewsList = new ArrayList<View>();
		viewsList = ViewTraversal.travasalViews(this.findViewById(R.id.draganddroplayout));
	
		findViewById(R.id.dragimage).setOnTouchListener(new MyTouchListener());
		findViewById(R.id.toplinear).setOnDragListener(new MyDragListener());
		findViewById(R.id.bottomlinear).setOnDragListener(new MyDragListener());
		findViewById(R.id.bottomright).setOnDragListener(new MyDragListener());
		
		
		MyTask mSocketsServer = new MyTask();
		mSocketsServer.execute();

	}

	// Create an anonymous implementation of OnClickListener
	private OnClickListener mCorkyListener = new OnClickListener() {
		public void onClick(View v) {
			updateDisplay("0");
		}
	};

	// Create an anonymous implementation of OnClickListener
	private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			updateDisplay("OnLongClickListener");
			return false;
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("onTouchEvent", event.toString());
		updateDisplay("2");
		return super.onTouchEvent(event);
	}

	// @Override
	// public boolean dispatchTouchEvent(MotionEvent ev) {
	// // TODO Auto-generated method stub
	// return super.dispatchTouchEvent(ev);
	// }

	@SuppressLint("NewApi")
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		updateDisplay("3");
		return super.onGenericMotionEvent(event);
	}

	protected void updateDisplay(String message) {
		// textView1.append(message + "\n");
		//textView1.append(message);
	}

	protected void dispatchView(final View v, final MotionEvent event) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				// textView1.append(me.toString()+ "\n");
				if (viewsList.contains(v)) {
					v.dispatchTouchEvent(event);
				} else {
					v.onTouchEvent(event);
				}
				// 
			}
		});

	}

	public class MyTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			updateDisplay("Starting task" + "\n");
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
			updateDisplay(result);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			// super.onProgressUpdate(values);
			updateDisplay(values[0]);
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

	
	private final class MyTouchListener implements OnTouchListener {
		@SuppressLint("NewApi")
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			// if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// mode = MOVE;
			// PointerCoords outPointerCoords = new PointerCoords();
			// event.getPointerCoords(0, outPointerCoords);
			// movingView = ViewTraversal.getView(outPointerCoords.x, outPointerCoords.y, viewlist);
			// return true;
			// } else if (event.getAction() == MotionEvent.ACTION_UP) {
			// mode = NONE;
			// movingView = null;
			//
			// return true;
			// } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			// if (mode == MOVE) {
			// // // create it from the object's tag
			// // ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());
			// //
			// // String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
			// // ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
			// // DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			// //
			// // view.startDrag( data, //data to be dragged
			// // shadowBuilder, //drag shadow
			// // view, //local data about the drag and drop operation
			// // 0 //no needed flags
			// // );
			// //
			// //
			// // view.setVisibility(View.INVISIBLE);
			//
			// if (movingView != null) {
			// ClipData.Item item = new ClipData.Item((CharSequence) movingView.getTag());
			//
			// String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
			// ClipData data = new ClipData(IMAGEVIEW_TAG, mimeTypes, item);
			// DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(movingView);
			//
			// movingView.startDrag(data, shadowBuilder, view, 0);
			// movingView.setVisibility(View.INVISIBLE);
			// }
			// }
			if (movingView == null) {
				PointerCoords outPointerCoords = new PointerCoords();
				event.getPointerCoords(0, outPointerCoords);
				movingView = ViewTraversal.getView(outPointerCoords.x, outPointerCoords.y, viewsList);
				Log.d("movingView", String.valueOf(outPointerCoords.x));
				Log.d("movingView", String.valueOf(outPointerCoords.y));
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
				view.startDrag(data, shadowBuilder, movingView, 0);
				view.setVisibility(View.INVISIBLE);
			}
			return true;

			// } else
			// return false;
		}
	}
	
	@SuppressLint("NewApi")
	class MyDragListener implements OnDragListener {
		Drawable normalShape = getResources().getDrawable(R.drawable.normal_shape);
		Drawable targetShape = getResources().getDrawable(R.drawable.target_shape);
		Drawable shape = getResources().getDrawable(R.drawable.shape);

		@Override
		public boolean onDrag(View v, DragEvent event) {
			int action = event.getAction();
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// do nothing
				break;
			case DragEvent.ACTION_DRAG_ENTERED:

				break;
			case DragEvent.ACTION_DRAG_EXITED:

				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				// View view = (View) event.getLocalState();
				ViewGroup owner = (ViewGroup) movingView.getParent();
				owner.removeView(movingView);
				LinearLayout container = (LinearLayout) v;
				container.addView(movingView);
				movingView.setVisibility(View.VISIBLE);
				movingView = null;
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				v.setBackgroundDrawable(normalShape);
			default:
				break;
			}
			return true;
		}
	}
}