package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class AndroidSocketsServer extends Activity {
	private TextView textView1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_sockets_server);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		textView1.setMovementMethod(new ScrollingMovementMethod());
	

		MyTask mSocketsServer = new MyTask();
		mSocketsServer.execute();
		

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
