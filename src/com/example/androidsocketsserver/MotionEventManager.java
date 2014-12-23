package com.example.androidsocketsserver;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

public class MotionEventManager {
	
	/**
	 * @param joMotionEvent:This JSONObject was generated from a motion event since Motion event doesn't support serialization. 
	 * @return MotionEvent: It was restored from a JSONObject in order to replay this motion event as what it did on remote tablet.
	 * @throws Exception
	 */
	@SuppressLint("Recycle") 
	public static MotionEvent restoreMotionEvent(JSONObject joMotionEvent) throws Exception {
    	int pointerCount = joMotionEvent.getInt("pointerCount");
    	PointerCoords[] pointerCoords = new PointerCoords[pointerCount];
		PointerProperties[] pointerProperties = new PointerProperties[pointerCount];
		JSONArray japc = new JSONArray(joMotionEvent.getString("pointerCoords"));
		JSONArray japp = new JSONArray(joMotionEvent.getString("pointerProperties"));
		for (int i = 0; i < pointerCount; ++i) {
			JSONObject jpc = japc.getJSONObject(i);
			JSONObject jpp = japp.getJSONObject(i);
			pointerCoords[i] = new PointerCoords();
			pointerCoords[i].orientation = (float) jpc.getDouble("orientation");
			pointerCoords[i].pressure = (float) jpc.getDouble("pressure");
			pointerCoords[i].size = (float) jpc.getDouble("size");
			pointerCoords[i].toolMajor = (float) jpc.getDouble("toolMajor");
			pointerCoords[i].toolMinor = (float) jpc.getDouble("toolMinor");
			pointerCoords[i].touchMajor = (float) jpc.getDouble("touchMajor");
			pointerCoords[i].touchMinor = (float) jpc.getDouble("touchMinor");
			pointerCoords[i].x = (float) jpc.getDouble("x");
			pointerCoords[i].y = (float) jpc.getDouble("y");
			
			pointerProperties[i] = new PointerProperties();
			pointerProperties[i].id = jpp.getInt("id");
			pointerProperties[i].toolType = jpp.getInt("toolType");
		} 
		MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), joMotionEvent.getInt("actionEvent"),
				joMotionEvent.getInt("pointerCount"), pointerProperties, pointerCoords, joMotionEvent.getInt("metaState"),
				joMotionEvent.getInt("buttonState"), (float)joMotionEvent.getDouble("xPrecision"), (float)joMotionEvent.getDouble("yPrecision"), joMotionEvent.getInt("deviceId"),
				joMotionEvent.getInt("edgeFlags"), joMotionEvent.getInt("source"), joMotionEvent.getInt("flags"));
    	return event;
	}
	
	public static JSONObject encodeMotionEventToJSON(MotionEvent event) throws Exception{
    	JSONObject jo = new JSONObject();
    	int pointerCount = event.getPointerCount();
    	PointerCoords[] pointerCoords = new PointerCoords[pointerCount];
		PointerProperties[] pointerProperties = new PointerProperties[pointerCount];
		JSONArray arrPointerCoords = new JSONArray();
		JSONArray arrPointerProperties = new JSONArray();
		for (int i = 0; i < pointerCount; ++i) {
			JSONObject jpc = new JSONObject();
			JSONObject jpp = new JSONObject();
			pointerCoords[i] = new PointerCoords();
			event.getPointerCoords(i, pointerCoords[i]);
			jpc.put("orientation", pointerCoords[i].orientation);
			jpc.put("pressure", pointerCoords[i].pressure);
			jpc.put("size", pointerCoords[i].size);
			jpc.put("toolMajor", pointerCoords[i].toolMajor);
			jpc.put("toolMinor", pointerCoords[i].toolMinor);
			jpc.put("touchMajor", pointerCoords[i].touchMajor);
			jpc.put("touchMinor", pointerCoords[i].touchMinor);
			jpc.put("x", pointerCoords[i].x);
			jpc.put("y", pointerCoords[i].y);
			arrPointerCoords.put(jpc);
			
			pointerProperties[i] = new PointerProperties();
			event.getPointerProperties(i, pointerProperties[i]);
			jpp.put("id", pointerProperties[i].id);
			jpp.put("toolType", pointerProperties[i].toolType);
			arrPointerProperties.put(jpp);
		} 

		int actionEvent = event.getAction();
		jo.put("downTime", event.getDownTime());
		jo.put("eventTime", event.getEventTime());
		jo.put("actionEvent", actionEvent);
		jo.put("pointerCount", event.getPointerCount());
		jo.put("pointerProperties", arrPointerProperties.toString());
		jo.put("pointerCoords", arrPointerCoords.toString());
		jo.put("metaState", event.getMetaState());
		jo.put("buttonState", event.getButtonState());
		jo.put("xPrecision", event.getXPrecision());
		jo.put("yPrecision", event.getYPrecision());
		jo.put("deviceId", event.getDeviceId());
		jo.put("edgeFlags", event.getEdgeFlags());
		jo.put("source", event.getSource());
		jo.put("flags", event.getFlags());
		return jo;
    }

}
