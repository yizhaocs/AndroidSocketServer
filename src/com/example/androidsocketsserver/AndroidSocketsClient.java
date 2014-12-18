package com.example.androidsocketsserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AndroidSocketsClient extends Activity {
	private Button buttonSocketsClient1;
	private TextView clientMessage;
	MyTask mSocketsClient;
	List<MyTask> tasks;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_android_sockets_client);
		clientMessage= (TextView) this.findViewById(R.id.clientMessage);
		clientMessage.setMovementMethod(new ScrollingMovementMethod());
		tasks = new ArrayList<AndroidSocketsClient.MyTask>();
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
	
//	@SuppressLint("NewApi")
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		
//		
//		return super.onTouchEvent(event);
//	}
//	
//	

	@SuppressLint("NewApi")
	@Override
	public boolean onTouchEvent(MotionEvent me) {
		List<float[]> pointerCoordsList = new ArrayList<float[]>();
		List<int[]> pointerPropertiesList = new ArrayList<int[]>();
		for (int i = 0; i < me.getPointerCount(); i++) {
			PointerProperties outPointerProperties = new PointerProperties();
			me.getPointerProperties(i, outPointerProperties);
			int[] pointerPropertiesArray = new int[2];
			pointerPropertiesArray[0] = outPointerProperties.id;
			pointerPropertiesArray[1] = outPointerProperties.toolType;
			pointerPropertiesList.add(pointerPropertiesArray);
			
			PointerCoords outPointerCoords = new PointerCoords();
			me.getPointerCoords(i, outPointerCoords);
			float[] pointerCoordsArray = new float[9];
			pointerCoordsArray[0] = outPointerCoords.x;
			pointerCoordsArray[1] = outPointerCoords.y;
			pointerCoordsArray[2] = outPointerCoords.pressure;
			pointerCoordsArray[3] = outPointerCoords.size;
			pointerCoordsArray[4] = outPointerCoords.touchMajor;
			pointerCoordsArray[5] = outPointerCoords.touchMinor;
			pointerCoordsArray[6] = outPointerCoords.toolMajor;
			pointerCoordsArray[7] = outPointerCoords.toolMinor;
			pointerCoordsArray[8] = outPointerCoords.orientation;
			pointerCoordsList.add(pointerCoordsArray);
		}
		int pointCount = me.getPointerCount();
		JSONObject js = new JSONObject();
		if(pointCount == 1){
			try {
				js.put("pointerCount", pointCount);
				js.put("downTime", me.getDownTime());
				js.put("eventTime", me.getEventTime());
				js.put("action", me.getAction());
				js.put("x", me.getX());
				js.put("y", me.getY());
				js.put("pressure", me.getPressure());
				js.put("size", me.getSize());
				js.put("metaState", me.getMetaState());
				js.put("xPrecision", me.getXPrecision());
				js.put("yPrecision", me.getYPrecision());
				js.put("deviceId", me.getDeviceId());
				js.put("edgeFlags", me.getEdgeFlags());
				Log.d("jsonSinglePoint", js.toString());
			} catch (JSONException e) {
				
			}
		}else{
			try {
				js.put("pointerCount", me.getPointerCount());
				js.put("downTime", me.getDownTime());
				js.put("eventTime", me.getEventTime());
				js.put("action", me.getAction());
				
				for(int i = 0; i < pointerPropertiesList.size(); i++){
					js.put("pointerProperties" + "_" + i, Arrays.toString(pointerPropertiesList.get(i)));
				}
				for(int f = 0; f < pointerCoordsList.size(); f++){
					js.put("pointeRcoords" + "_" + f, Arrays.toString(pointerCoordsList.get(f)));
				}
				
				js.put("metaState", me.getMetaState());
				js.put("buttonState", me.getButtonState());
				js.put("xPrecision", me.getXPrecision());
				js.put("yPrecision", me.getYPrecision());
				js.put("deviceId", me.getDeviceId());
				js.put("edgeFlags", me.getEdgeFlags());
				js.put("source", me.getSource());
				js.put("flags", me.getFlags());
				Log.d("jsonMultiPoints", js.toString());
			} catch (JSONException e) {
				
			}
		}

		mSocketsClient = new MyTask();
		mSocketsClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, js.toString());
		return super.onTouchEvent(me);
	}

	
	public class MyTask extends AsyncTask<String, String, String> {
		private String hostName = "10.97.1.32";
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
