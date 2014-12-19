package com.example.androidsocketsserver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;
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
	public JSONObject motionEventToJsonObject(MotionEvent me){
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
				// Log.d("jsonSinglePoint", js.toString());
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
				// Log.d("jsonMultiPoints", js.toString());
			} catch (JSONException e) {
				
			}
		}
		return js;
	}
}
