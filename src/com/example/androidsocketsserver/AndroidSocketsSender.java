package com.example.androidsocketsserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AndroidSocketsSender extends Activity {
	private Button buttonSocketsClient1;
	private TextView clientMessage;
	MyTask mSocketsClient;
	List<MyTask> tasks;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sender);
		clientMessage = (TextView) this.findViewById(R.id.clientMessage);
		clientMessage.setMovementMethod(new ScrollingMovementMethod());
		tasks = new ArrayList<AndroidSocketsSender.MyTask>();
		buttonSocketsClient1 = (Button) this.findViewById(R.id.buttonSocketsClient1);
		buttonSocketsClient1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// MyTask mSocketsClient = new MyTask();
				// mSocketsClient.execute();
			}
		});

	}

	protected void updateDisplay(String message) {
		// clientMessage.append(message + "\n");
		//clientMessage.append(message);
	}




	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		mSocketsClient = new MyTask();
		mSocketsClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, ConvertorOfMotionEventToJsonObject.motionEventToJsonObject(me).toString());
		return super.onTouchEvent(me);
	}

	public class MyTask extends AsyncTask<String, String, String> {
		private String hostName = "10.97.1.98";
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
