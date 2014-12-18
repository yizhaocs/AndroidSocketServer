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
				/*
				 * The server program begins by creating a new ServerSocket
				 * object to listen on a specific port (see the statement in
				 * bold in the following code segment). When running this
				 * server, choose a port that is not already dedicated to some
				 * other service.
				 */
	
				ServerSocket serverSocket = new ServerSocket(4444);
			
				/*
				 * If the server successfully binds to its port, then the
				 * ServerSocket object is successfully created and the server
				 * continues to the next step—accepting a connection from a
				 * client. The accept method waits ∂until a client starts up and
				 * requests a connection on the host and port of this server. In
				 * this example, the server is running on the port number
				 * specified by the first command-line argument. When a
				 * connection is requested and successfully established, the
				 * accept method returns a new Socket object which is bound to
				 * the same local port and has its remote address and remote
				 * port set to that of the client. The server can communicate
				 * with the client over this new Socket and continue to listen
				 * for client connection requests on the original ServerSocket.
				 */
				Socket clientSocket = serverSocket.accept();
				/*
				 * Gets the socket's input and output stream and opens readers
				 * and writers on them.
				 */
				PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

				String inputLine;

				/*
				 * Initiates communication with the client by writing to the
				 * socket with simple conversation.
				 */
				SingleClientRequest_SocketServerProtocol kkp = new SingleClientRequest_SocketServerProtocol();
				out.println("Successfully Connected to Socket Server");

				/*
				 * Communicates with the client by reading from and writing to
				 * the socket (the while loop).
				 */
				while ((inputLine = in.readLine()) != null) {
					publishProgress(inputLine);
					out.println("server recieved: " + inputLine);
					if (inputLine.equals("bye")) {
						break;
					}
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
