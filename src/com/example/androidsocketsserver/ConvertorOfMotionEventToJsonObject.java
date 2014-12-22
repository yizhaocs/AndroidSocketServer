package com.example.androidsocketsserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

public class ConvertorOfMotionEventToJsonObject {
	private static ConvertorOfMotionEventToJsonObject instance = null;

	private ConvertorOfMotionEventToJsonObject() {

	}

	public static ConvertorOfMotionEventToJsonObject getInstance() {
		if (instance == null) {
			instance = new ConvertorOfMotionEventToJsonObject();
		}
		return instance;
	}

	@SuppressLint("NewApi")
	public JSONObject motionEventToJsonObject(MotionEvent event) throws Exception {

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

	// public JSONObject motionEventToJsonObject(MotionEvent event) {
	// JSONObject jo = new JSONObject();
	// try {
	// int pointerCount = event.getPointerCount();
	// PointerCoords[] pointerCoords = new PointerCoords[pointerCount];
	// PointerProperties[] pointerProperties = new PointerProperties[pointerCount];
	// JSONArray arrPointerCoords = new JSONArray();
	// JSONArray arrPointerProperties = new JSONArray();
	// for (int i = 0; i < pointerCount; ++i) {
	// JSONObject jpc = new JSONObject();
	// JSONObject jpp = new JSONObject();
	// pointerCoords[i] = new PointerCoords();
	// event.getPointerCoords(i, pointerCoords[i]);
	// jpc.put("orientation", pointerCoords[i].orientation);
	//
	// jpc.put("pressure", pointerCoords[i].pressure);
	//
	// jpc.put("size", pointerCoords[i].size);
	// jpc.put("toolMajor", pointerCoords[i].toolMajor);
	// jpc.put("toolMinor", pointerCoords[i].toolMinor);
	// jpc.put("touchMajor", pointerCoords[i].touchMajor);
	// jpc.put("touchMinor", pointerCoords[i].touchMinor);
	// jpc.put("x", pointerCoords[i].x);
	// jpc.put("y", pointerCoords[i].y);
	// arrPointerCoords.put(jpc);
	//
	// pointerProperties[i] = new PointerProperties();
	// event.getPointerProperties(i, pointerProperties[i]);
	// jpp.put("id", pointerProperties[i].id);
	// jpp.put("toolType", pointerProperties[i].toolType);
	// arrPointerProperties.put(jpp);
	// }
	//
	// int actionEvent = event.getAction();
	// jo.put("downTime", event.getDownTime());
	// jo.put("eventTime", event.getEventTime());
	// jo.put("actionEvent", actionEvent);
	// jo.put("pointerCount", event.getPointerCount());
	// jo.put("pointerProperties", arrPointerProperties.toString());
	// jo.put("pointerCoords", arrPointerCoords.toString());
	// jo.put("metaState", event.getMetaState());
	// jo.put("buttonState", event.getButtonState());
	// jo.put("xPrecision", event.getXPrecision());
	// jo.put("yPrecision", event.getYPrecision());
	// jo.put("deviceId", event.getDeviceId());
	// jo.put("edgeFlags", event.getEdgeFlags());
	// jo.put("source", event.getSource());
	// jo.put("flags", event.getFlags());
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return jo;
	// }
	//
}
