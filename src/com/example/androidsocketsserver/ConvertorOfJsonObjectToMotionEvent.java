package com.example.androidsocketsserver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

public class ConvertorOfJsonObjectToMotionEvent {
	private static ConvertorOfJsonObjectToMotionEvent instance = null;

	private ConvertorOfJsonObjectToMotionEvent() {

	}

	public static ConvertorOfJsonObjectToMotionEvent getInstance() {
		if (instance == null) {
			instance = new ConvertorOfJsonObjectToMotionEvent();
		}
		return instance;
	}

	@SuppressLint("NewApi")
	public MotionEvent createMotionEvent(JSONObject jsonObject) throws JSONException {

		int pointerCount = jsonObject.getInt("pointerCount");
		PointerCoords[] pointerCoords = new PointerCoords[pointerCount];
		PointerProperties[] pointerProperties = new PointerProperties[pointerCount];
		JSONArray japc = new JSONArray(jsonObject.getString("pointerCoords"));
		JSONArray japp = new JSONArray(jsonObject.getString("pointerProperties"));
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
		MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), jsonObject.getInt("actionEvent"), jsonObject.getInt("pointerCount"), pointerProperties, pointerCoords, jsonObject.getInt("metaState"), jsonObject.getInt("buttonState"),
				(float) jsonObject.getDouble("xPrecision"), (float) jsonObject.getDouble("yPrecision"), jsonObject.getInt("deviceId"), jsonObject.getInt("edgeFlags"), jsonObject.getInt("source"), jsonObject.getInt("flags"));
		return event;
	}

	// @SuppressLint("NewApi")
	// public MotionEvent createMotionEvent(JSONObject jsonObject) throws JSONException {
	// // if (1 == jsonObject.getInt("pointerCount")) {
	// // long downTime = SystemClock.uptimeMillis();// jsonObject.getLong("downTime");//SystemClock.uptimeMillis();
	// // long eventTime = SystemClock.uptimeMillis();// jsonObject.getLong("eventTime");
	// // int action = jsonObject.getInt("action");
	// // float x = Float.valueOf(jsonObject.getString("x"));
	// // float y = Float.valueOf(jsonObject.getString("y"));
	// // int metaState = jsonObject.getInt("metaState");
	// //
	// // float pressure = Float.valueOf(jsonObject.getString("pressure"));
	// // float xPrecision = Float.valueOf(jsonObject.getString("xPrecision"));
	// // float yPrecision = Float.valueOf(jsonObject.getString("yPrecision"));
	// //
	// // float size = Float.valueOf(jsonObject.getString("size"));
	// // float edgeFlags = Float.valueOf(jsonObject.getString("edgeFlags"));
	// // float deviceId = Float.valueOf(jsonObject.getString("deviceId"));
	// //
	// // MotionEvent me = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);
	// //
	// // return me;
	// // } else {
	// long downTime = jsonObject.getLong("downTime");
	// long eventTime = jsonObject.getLong("eventTime");
	// int action = jsonObject.getInt("action");
	// int pointerCount = jsonObject.getInt("pointerCount");
	//
	// PointerProperties[] pPs = new PointerProperties[pointerCount];
	// for (int i = 0; i < pointerCount; i++) {
	// PointerProperties aPP = new PointerProperties();
	// String strPointerProperties = jsonObject.getString("pointerProperties_" + i);
	// if (strPointerProperties.startsWith("\"") && strPointerProperties.endsWith("\"")) {
	// strPointerProperties = strPointerProperties.substring(1, strPointerProperties.length() - 1);
	// }
	// JSONArray obj = new JSONArray(strPointerProperties);
	// // JSONArray obj = jsonObject.getJSONArray("pointerProperties_"+i);
	// aPP.id = obj.getInt(0);
	// aPP.toolType = obj.getInt(1);
	// pPs[i] = aPP;
	// }
	//
	// PointerCoords[] pCs = new PointerCoords[pointerCount];
	// for (int i = 0; i < pointerCount; i++) {
	// PointerCoords aPc = new PointerCoords();
	// String strPointeRcoords = jsonObject.getString("pointeRcoords_" + i);
	// if (strPointeRcoords.startsWith("\"") && strPointeRcoords.endsWith("\"")) {
	// strPointeRcoords = strPointeRcoords.substring(1, strPointeRcoords.length() - 1);
	// }
	// JSONArray obj = new JSONArray(strPointeRcoords);
	// // JSONArray obj = jsonObject.getJSONArray("pointeRcoords_"+i);
	// aPc.x = (float) obj.getDouble(0);
	// aPc.y = (float) obj.getDouble(1);
	// aPc.pressure = (float) obj.getDouble(2);
	// aPc.size = (float) obj.getDouble(3);
	// aPc.touchMajor = (float) obj.getDouble(4);
	// aPc.touchMinor = (float) obj.getDouble(5);
	// aPc.toolMajor = (float) obj.getDouble(6);
	// aPc.toolMinor = (float) obj.getDouble(7);
	// aPc.orientation = (float) obj.getDouble(8);
	//
	// pCs[i] = aPc;
	// }
	//
	// int metaState = jsonObject.getInt("metaState");
	// int buttonState = jsonObject.getInt("buttonState");
	// float xPrecision = Float.valueOf(jsonObject.getString("xPrecision"));
	// float yPrecision = Float.valueOf(jsonObject.getString("yPrecision"));
	// int deviceId = jsonObject.getInt("deviceId");
	// int edgeFlags = jsonObject.getInt("edgeFlags");
	// int source = jsonObject.getInt("source");
	// int flags = jsonObject.getInt("flags");
	//
	// MotionEvent me = MotionEvent.obtain(downTime, eventTime, action, pointerCount, pPs, pCs, metaState, buttonState, xPrecision, yPrecision, deviceId, edgeFlags, source, flags);
	// // MotionEventToJson(me);
	// return me;
	// //}
	// }
}
