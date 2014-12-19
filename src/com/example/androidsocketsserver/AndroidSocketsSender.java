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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

public class AndroidSocketsSender extends Activity {
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
		setContentView(R.layout.sender);
		clientMessage = (TextView) this.findViewById(R.id.clientMessage);
		clientMessage.setMovementMethod(new ScrollingMovementMethod());
		tasks = new ArrayList<AndroidSocketsSender.MyTask>();

	}

	protected void updateDisplay(String message) {
		// clientMessage.append(message + "\n");
		// clientMessage.append(message);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.d("onTouchEvent", event.toString());
		// clientMessage.append(me.toString()+ "\n");
		mSocketsClient = new MyTask();
		// mSocketsClient.execute();
		// mSocketsClient.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, ConvertorOfMotionEventToJsonObject.motionEventToJsonObject(me).toString());
		mSocketsClient.executeOnExecutor(new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory), mConvertorOfMotionEventToJsonObject.motionEventToJsonObject(event).toString());
		return super.onTouchEvent(event);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		updateDisplay("3");
		mSocketsClient = new MyTask();
		// mSocketsClient.execute();
		// mSocketsClient.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, ConvertorOfMotionEventToJsonObject.motionEventToJsonObject(me).toString());
		mSocketsClient.executeOnExecutor(new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory), mConvertorOfMotionEventToJsonObject.motionEventToJsonObject(event).toString());
		return super.onGenericMotionEvent(event);
	}

	public class MyTask extends AsyncTask<String, String, String> {
		private String hostName = "10.97.1.89";
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
