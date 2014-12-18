package com.example.androidsocketsserver;

import java.io.IOException;
import java.net.ServerSocket;
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
import android.view.View.OnLongClickListener;
import android.widget.TextView;

public class AndroidSocketsReciever extends Activity {
	private TextView textView1;
	static List<View> viewsList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reciever);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		textView1.setMovementMethod(new ScrollingMovementMethod());
		viewsList = new ArrayList<View>();
		ViewTraversal.recursion(this.findViewById(R.id.recieverContent),viewsList);
		// Initialize the TextView for vertical scrolling
		textView1.setOnClickListener(mCorkyListener);
		textView1.setOnLongClickListener(mOnLongClickListener);

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
			updateDisplay("1");
			return false;
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		updateDisplay("2");
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		updateDisplay("3");
		return super.onGenericMotionEvent(event);
	}
	
	protected void updateDisplay(String message) {
		textView1.append(message + "\n");
	}

	public class MyTask extends AsyncTask<String,String,String> {
		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			updateDisplay("Starting task");
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
				System.out.println("Exception caught when trying to listen on port " + 4444 + " or listening for a connection");
				System.out.println(e.getMessage());
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
