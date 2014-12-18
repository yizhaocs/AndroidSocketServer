package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AndroidSocketsClient extends Activity {
	private Button buttonSocketsClient1;
	private TextView clientMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_sockets_client);
		clientMessage= (TextView) this.findViewById(R.id.clientMessage);
		clientMessage.setMovementMethod(new ScrollingMovementMethod());
		
		buttonSocketsClient1 = (Button) this.findViewById(R.id.buttonSocketsClient1);
		buttonSocketsClient1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				MyTask mSocketsClient = new MyTask();
//				mSocketsClient.execute();
			}
		});
		
	}

	protected void updateDisplay(String message) {
		clientMessage.append(message + "\n");
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		MyTask mSocketsClient = new MyTask();
		//mSocketsClient.execute(event.toString());
		mSocketsClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, event.toString());
		mSocketsClient.cancel(true);
		return super.onTouchEvent(event);
	}
	
	public class MyTask extends AsyncTask<String, String, String> {
		private String hostName = "10.97.1.32";
		private int portNumber = 4444;

		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			updateDisplay("Starting task");
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
		}
		
		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			// super.onProgressUpdate(values);
			updateDisplay(values[0]);
		}
	}
}
