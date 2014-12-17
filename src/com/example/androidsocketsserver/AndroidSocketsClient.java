package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.example.androidsocketsserver.AndroidSocketsServer.SocketsServer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AndroidSocketsClient extends Activity {
	private Button buttonSocketsClient1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_sockets_client);
		buttonSocketsClient1 = (Button) this.findViewById(R.id.buttonSocketsClient1);
		buttonSocketsClient1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		SocketsClient mSocketsClient = new SocketsClient(getApplicationContext());
		mSocketsClient.execute();
	}

	public static class SocketsClient extends AsyncTask {
		private Context context;
		private String hostName = "10.97.1.83";
		private int portNumber = 4444;

		public SocketsClient(Context context) {
			this.context = context;

		}

		@SuppressWarnings("resource")
		protected String doInBackground(Object... params) {
			try {
				Socket kkSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
				String fromServer;
				String fromUser;

				while ((fromServer = in.readLine()) != null) {
					System.out.println("Server: " + fromServer);
					if (fromServer.equals("Bye."))
						break;

					fromUser = stdIn.readLine();
					if (fromUser != null) {
						System.out.println("Client: " + fromUser);
						out.println(fromUser);
					}
				}
			} catch (UnknownHostException e) {
				System.err.println("Don't know about host " + hostName);
				System.exit(1);
			} catch (IOException e) {
				System.err.println("Couldn't get I/O for the connection to " + hostName);
				System.exit(1);
			}
			return null;
		}

	}
}
