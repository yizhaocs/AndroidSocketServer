package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MotionEvent.PointerCoords;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AndroidSocketsRecieverForButton extends Activity {
	private TextView textView1;
	private List<View> viewsList;
	private Button button1;
	private AndroidSocketsRecieverForButton a = this;
	ConvertorOfJsonObjectToMotionEvent mConvertorOfJsonObjectToMotionEvent = ConvertorOfJsonObjectToMotionEvent.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recieverforbutton);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		textView1.setMovementMethod(new ScrollingMovementMethod());
		// viewsList = new ArrayList<View>();
		viewsList = ViewTraversal.travasalViews(this.findViewById(R.id.recieverrootview));
		// Initialize the TextView for vertical scrolling
		textView1.setOnClickListener(mCorkyListener);
		textView1.setOnLongClickListener(mOnLongClickListener);
		button1 = (Button) this.findViewById(R.id.button1);
		button1.setOnLongClickListener(mOnLongClickListener);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateDisplay("OnClickListener");

			}
		});

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
		textView1.append(message);
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
					MotionEvent me = null;
					try {
						me = MotionEventManager.restoreMotionEvent(jo);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
