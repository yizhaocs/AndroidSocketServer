package com.example.androidsocketsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.MotionEvent.PointerCoords;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.example.androidsocketsserver.AndroidSocketsRecieverForDragAndDropV3.BackgroundThreadDrawing;
import com.example.androidsocketsserver.AndroidSocketsRecieverForDragAndDropV3.BackgroundThreadSockets;
import com.example.androidsocketsserver.AndroidSocketsRecieverForDragAndDropV3.MyTask;

public class AndroidSocketsRecieverForDragAndDropV4 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(new MyCustomView(this, null));	
	}
}
