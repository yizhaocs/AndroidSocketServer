package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView textView1;
	private TextView recievedText1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView1 = (TextView) this.findViewById(R.id.textView1);

		recievedText1 = (TextView) this.findViewById(R.id.recievedText1);
		new SocketsServer(getApplicationContext(), textView1,recievedText1).execute();

	}

	public static class SocketsServer extends AsyncTask {
		private Context context;
		private TextView subTextView1;
		private TextView subRecievedText1;
		public SocketsServer(Context context, TextView textView1,TextView recievedText1) {
			this.context = context;
			this.subTextView1 = textView1;
			this.subRecievedText1 = recievedText1;
			
		}

		/**
		 * Start activity that can handle the JPEG image
		 */
		protected void onPostExecute(String result) {
			subTextView1.setText("abcd");
			if (result != null) {
				
				subRecievedText1.setText("File copied - " + result);
				Intent intent = new Intent();
				intent.setAction(android.content.Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.parse("file://" + result), "image/*");
				context.startActivity(intent);
			}
		}
		
		protected void onProgressUpdate(Integer... progress) {
			subTextView1.setText("abcd");
	     }
		
		@SuppressWarnings("resource")
		protected String doInBackground(Object... params) {

			try {
				try {
					subTextView1.setText(String.valueOf(InetAddress.getLocalHost().getLocalHost()));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				/*
				 * The server program begins by creating a new ServerSocket
				 * object to listen on a specific port (see the statement in
				 * bold in the following code segment). When running this
				 * server, choose a port that is not already dedicated to some
				 * other service.
				 */
				Log.d("heihei", "1233");
				ServerSocket serverSocket = new ServerSocket(4444);
				Log.d("heihei", "1234");
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
				PrintWriter out = new PrintWriter(
						clientSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));

				String inputLine, outputLine;

				/*
				 * Initiates communication with the client by writing to the
				 * socket with simple conversation.
				 */
				SingleClientRequest_SocketServerProtocol kkp = new SingleClientRequest_SocketServerProtocol();
				outputLine = kkp.processInput(null);
				out.println(outputLine);

				/*
				 * Communicates with the client by reading from and writing to
				 * the socket (the while loop).
				 */
				while ((inputLine = in.readLine()) != null) {
					
					outputLine = kkp.processInput(inputLine);
					out.println(outputLine);
					if (outputLine.equals("Bye.")) {
						break;
					}
				}
			} catch (IOException e) {
				System.out
						.println("Exception caught when trying to listen on port "
								+ 4444 + " or listening for a connection");
				System.out.println(e.getMessage());
			}
			return null;
		}

	}
}
