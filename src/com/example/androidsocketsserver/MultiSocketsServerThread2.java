package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;

public class MultiSocketsServerThread2 extends Thread {
	
	private Socket socket = null;
	private List<View> viewsList;
	private Context context;
	public MultiSocketsServerThread2(Context context, Socket socket, List<View> viewsList) {
		super("KKMultiServerThread");
		this.socket = socket;
		this.viewsList = viewsList;
		this.context = context;
	}

	@SuppressLint("NewApi")
	public void run() {

		try {
			// PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String inputLine;

			// out.println("connection is setup between server and client");

			while ((inputLine = in.readLine()) != null) {
				JSONObject jo = new JSONObject(inputLine);
				MotionEvent me = ConvertorOfJsonObjectToMotionEvent.createMotionEvent(jo);
				PointerCoords outPointerCoords = new PointerCoords();
				me.getPointerCoords(0, outPointerCoords);
				Log.d("haha", me.toString());
				Log.d("hahax", String.valueOf(outPointerCoords.x));
				Log.d("hahay", String.valueOf(outPointerCoords.y));

				View v = ViewTraversal.getView(outPointerCoords.x, outPointerCoords.y, viewsList);
				// Log.d("hahaID", String.valueOf(v.getId()));
				if (me != null && v != null) {
					Log.d("haha", "dispatchTouchEvent");
					//v.dispatchTouchEvent(me);
					//ia.dispatchView(v, me);
				}

				// out.println("echo " + inputLine);
				if (inputLine.equals("Bye")) {
					break;
				}
			}
			socket.close();
		} catch (IOException e) {
			Log.e("error", "IOException:" + e.getMessage());
		
		} catch (JSONException e) {
			Log.e("error", "JSONException:" + e.getMessage());
	
		}
	}

}
