package com.example.androidsocketsserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.example.androidsocketsserver.AndroidSocketsRecieverForDragAndDropV1.BackgroundViewsDragListener;
import com.example.androidsocketsserver.AndroidSocketsSenderForDifferent.MyTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidSocketsSenderForSame extends Activity {
	ConvertorOfMotionEventToJsonObject mConvertorOfMotionEventToJsonObject = ConvertorOfMotionEventToJsonObject.getInstance();
	private TextView clientMessage;
	private MyTask mSocketsClient;
	private List<MyTask> tasks;
	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	// private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	// private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int CORE_POOL_SIZE = CPU_COUNT * 2 + 1;
	private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 5 + 1;
	private static final int KEEP_ALIVE = 1;
	private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(128);

	private static final ThreadFactory sThreadFactory = new ThreadFactory() {
		private final AtomicInteger mCount = new AtomicInteger(1);

		public Thread newThread(Runnable r) {
			return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
		}
	};
	public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.senderforsame);
//		clientMessage = (TextView) this.findViewById(R.id.clientMessage);
//		clientMessage.setMovementMethod(new ScrollingMovementMethod());
		
		findViewById(R.id.myimage1).setOnTouchListener(mOnTouchListener);
		findViewById(R.id.myimage1).setOnLongClickListener(mOnLongClickListener);

		findViewById(R.id.topleft).setOnDragListener(new BackgroundViewsDragListener());
		findViewById(R.id.topright).setOnDragListener(new BackgroundViewsDragListener());
		tasks = new ArrayList<AndroidSocketsSenderForSame.MyTask>();

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
				//sendMessage(motionEvent);
				//
				return true;
			case MotionEvent.ACTION_MOVE:
				Log.d("motionEvent", "ACTION_MOVE");
				cancelToast(toast_1, toast_2, toast_3);
				showToast(toast_2, Gravity.CENTER, "MotionEvent.ACTION_MOVE");
				//sendMessage(motionEvent);
				return true;
			case MotionEvent.ACTION_UP:
				Log.d("motionEvent", "ACTION_UP");
				cancelToast(toast_1, toast_2, toast_3);
				showToast(toast_3, Gravity.TOP, "MotionEvent.ACTION_UP");
				//view.setVisibility(View.VISIBLE);
				//sendMessage(motionEvent);
				return true;
			default:
				break;
			}
			return false;
		}
	};
	
	private void sendMessage(MotionEvent event){
		mSocketsClient = new MyTask();
		// mSocketsClient.execute();
		// mSocketsClient.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, ConvertorOfMotionEventToJsonObject.motionEventToJsonObject(me).toString());
		try {
			mSocketsClient.executeOnExecutor(new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory), MotionEventManager.encodeMotionEventToJSON(event).toString());
		} catch (Exception e) {
			Log.e("error", "Exception:" + e.getMessage());
		}
	}

	private void trigerDragAndDrop(View view) {
		DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		view.startDrag(null, shadowBuilder, view, 0);
		view.setVisibility(View.INVISIBLE);
	}

	private void showToast(Toast toast, int position, String info) {
		//toast = Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT);
		//toast.setGravity(position, 0, 0);
		//toast.show();
	}

	private void cancelToast(Toast toast_1, Toast toast_2, Toast toast_3) {

//		if (toast_1 != null) {
//			toast_1.cancel();
//		}
//		if (toast_2 != null) {
//			toast_2.cancel();
//		}
//		if (toast_3 != null) {
//			toast_3.cancel();
//		}
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
	

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("onTouchEvent", event.toString());
		// clientMessage.append(me.toString()+ "\n");
		mSocketsClient = new MyTask();
		// mSocketsClient.execute();
		// mSocketsClient.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, ConvertorOfMotionEventToJsonObject.motionEventToJsonObject(me).toString());
		try {
			mSocketsClient.executeOnExecutor(new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory), MotionEventManager.encodeMotionEventToJSON(event).toString());
		} catch (Exception e) {
			Log.e("error", "Exception:" + e.getMessage());
		}
		return super.onTouchEvent(event);
	}

	protected void updateDisplay(String message) {
		// clientMessage.append(message + "\n");
		// clientMessage.append(message);
	}

//	@SuppressLint("NewApi")
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		Log.d("onTouchEvent", event.toString());
//		// clientMessage.append(me.toString()+ "\n");
//		mSocketsClient = new MyTask();
//		// mSocketsClient.execute();
//		// mSocketsClient.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, ConvertorOfMotionEventToJsonObject.motionEventToJsonObject(me).toString());
//		try {
//			mSocketsClient.executeOnExecutor(new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory), MotionEventManager.encodeMotionEventToJSON(event).toString());
//		} catch (Exception e) {
//			Log.e("error", "Exception:" + e.getMessage());
//		}
//		return super.onTouchEvent(event);
//	}

//	@SuppressLint("NewApi")
//	@Override
//	public boolean onGenericMotionEvent(MotionEvent event) {
//		updateDisplay("3");
//		mSocketsClient = new MyTask();
//		// mSocketsClient.execute();
//		// mSocketsClient.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, ConvertorOfMotionEventToJsonObject.motionEventToJsonObject(me).toString());
//		try {
//			mSocketsClient.executeOnExecutor(new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory), MotionEventManager.encodeMotionEventToJSON(event).toString());
//		} catch (Exception e) {
//			Log.e("error", "Exception:" + e.getMessage());
//		}
//		return super.onGenericMotionEvent(event);
//	}

	public class MyTask extends AsyncTask<String, String, String> {
		private String hostName = "10.97.1.55";
		private int portNumber = 4444;

		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			updateDisplay("Starting task");
			tasks.add(this);
		};

		@SuppressWarnings("resource")
		@Override
		protected String doInBackground(String... params) {
			try {
				Socket kkSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
				Log.d("hahahahaha:", params[0].toString());
				out.println(params[0]);
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host " + hostName);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to " + hostName);
				System.exit(1);
			}
			return "Task complete";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			updateDisplay(result);
			mSocketsClient.cancel(true);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			// super.onProgressUpdate(values);
			updateDisplay(values[0]);
			tasks.remove(this);
		}

	}
}
