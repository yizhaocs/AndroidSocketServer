package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

public class MultiSocketsServerThread extends Thread {
	private Socket socket = null;

	public MultiSocketsServerThread(Socket socket) {
		super("KKMultiServerThread");
		this.socket = socket;
	}

	public void run() {

		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine;

			out.println("connection is setup between server and client");

			while ((inputLine = in.readLine()) != null) {
				JSONObject jo = new JSONObject(inputLine);
				MotionEvent me = ConvertorOfJsonObjectToMotionEvent.createMotionEvent(jo);
				Log.d("haha", me.toString());
				// out.println("echo " + inputLine);
				if (inputLine.equals("Bye")) {
					break;
				}
			}
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
