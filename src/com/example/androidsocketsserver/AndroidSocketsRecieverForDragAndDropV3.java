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
import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AndroidSocketsRecieverForDragAndDropV3 extends Activity {
	private OurView mOurView;
	private Boolean exit = false;
	private Boolean isDragging = false;

	private List<View> viewsList;
	// private View movingView = null;
	private AndroidSocketsRecieverForDragAndDropV3 a = this;
	ConvertorOfJsonObjectToMotionEvent mConvertorOfJsonObjectToMotionEvent = ConvertorOfJsonObjectToMotionEvent.getInstance();
	Bitmap mBitmap;
	Canvas c;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recieverfordraganddrop);
		mOurView = new OurView(this);
		
		viewsList = ViewTraversal.travasalViews(findViewById(R.id.rootview));

		findViewById(R.id.myimage1).setOnTouchListener(mOnTouchListener);
		findViewById(R.id.myimage1).setOnLongClickListener(mOnLongClickListener);

		// findViewById(R.id.topleft).setOnDragListener(new BackgroundViewsDragListener());
		// findViewById(R.id.topright).setOnDragListener(new BackgroundViewsDragListener());

		MyTask mSocketsServer = new MyTask();
		mSocketsServer.execute();

	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@SuppressLint("ClickableViewAccessibility")
		public boolean onTouch(View view, MotionEvent motionEvent) {

			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d("motionEvent", "ACTION_DOWN");
				isDragging = true;
				mBitmap = getBitmapFromView(view);
				c = new Canvas();
				return true;
			case MotionEvent.ACTION_MOVE:
				Log.d("motionEvent", "ACTION_MOVE");

				if (isDragging) {

					float xPosition = motionEvent.getRawX();
					float yPosition = motionEvent.getRawY();
					// view.setX(xPosition - view.getWidth());
					// view.setY(yPosition - view.getHeight());
					// view.requestLayout();
					c.drawBitmap(mBitmap, 60, 60, null);
					return true;
				} else {
					return false;
				}
			case MotionEvent.ACTION_UP:
				Log.d("motionEvent", "ACTION_UP");
				isDragging = false;
				ViewGroup owner = (ViewGroup) view.getParent();
				owner.removeView(view);
				view.setX(owner.getX());
				view.setY(owner.getY());
				owner.addView(view);
				return true;
			default:
				break;
			}
			return false;
		}
	};

	private void trigerDragAndDrop(View view) {
		SimpleDragShadow mSimpleDragShadow = new SimpleDragShadow(view);
		// DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		ClipData data = ClipData.newPlainText("", "");
		view.startDrag(data, mSimpleDragShadow, view, 0);
		view.setVisibility(View.INVISIBLE);
	}

	private void showToast(Toast toast, int position, String info) {
		// toast = Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT);
		// toast.setGravity(position, 0, 0);
		// toast.show();
	}

	private void cancelToast(Toast toast_1, Toast toast_2, Toast toast_3) {

		// if (toast_1 != null) {
		// toast_1.cancel();
		// }
		// if (toast_2 != null) {
		// toast_2.cancel();
		// }
		// if (toast_3 != null) {
		// toast_3.cancel();
		// }
	}

	private OnLongClickListener mOnLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View view) {
			Log.d("onLongClick", "onLongClick");
			// trigerDragAndDrop(view);
			return false;
		}
	};

	protected void dispatchView(final View v, final MotionEvent event) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				dispatchTouchEvent(event);
				// textView1.append(me.toString()+ "\n");
				// if (viewsList.contains(v)) {
				Log.d("AAA", "v.dispatchTouchEvent(event)");
				// v.dispatchTouchEvent(event);
				// } else {
				// Log.d("AAA", "v.onTouchEvent(event)");
				// v.onTouchEvent(event);
				// }
				//
			}
		});
	}

	class BackgroundViewsDragListener implements OnDragListener {
		@Override
		public boolean onDrag(View layoutview, DragEvent dragEvent) {
			switch (dragEvent.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				Log.d("dragEvent", "Drag event started");
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				Log.d("dragEvent", "Drag event entered into " + layoutview.toString());
				break;
			case DragEvent.ACTION_DRAG_EXITED:
				Log.d("dragEvent", "Drag event exited from " + layoutview.toString());
				break;
			case DragEvent.ACTION_DROP:
				Log.d("dragEvent", "Dropped");
				// Dropped, reassign View to ViewGroup
				View dragView = (View) dragEvent.getLocalState();
				ViewGroup owner = (ViewGroup) dragView.getParent();
				owner.removeView(dragView);
				LinearLayout container = (LinearLayout) layoutview;
				container.addView(dragView);
				dragView.setVisibility(View.VISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				Log.d("dragEvent", "Drag ended");
				break;
			default:
				break;
			}
			return true;
		}

	}

	public class MyTask extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// super.onPreExecute();
			// updateDisplay("Starting task" + "\n");
		};

		@SuppressWarnings("resource")
		protected String doInBackground(String... params) {
			try {
				int portNumber = 4444;
				boolean listening = true;
				ServerSocket serverSocket = new ServerSocket(portNumber);
				while (listening) {
					new MultiSocketsServerThread(serverSocket.accept(), viewsList).start();
				}
			} catch (IOException e) {
				Log.e("error", "IOException:" + e.getMessage());
			}
			return "Task complete";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);
			// updateDisplay(result);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			// super.onProgressUpdate(values);
			// updateDisplay(values[0]);
		}
	}

	@Override
	public void onBackPressed() {
		if (exit) {
			finish(); // finish activity
		} else {
			Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
			exit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exit = false;
				}
			}, 3 * 1000);

		}

	}

	public class MultiSocketsServerThread extends Thread {

		private Socket socket = null;
		private List<View> viewsList;

		public MultiSocketsServerThread(Socket socket, List<View> viewsList) {
			super("KKMultiServerThread");
			this.socket = socket;
			this.viewsList = viewsList;

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
					Log.d("whatf", jo.toString());
					MotionEvent me = mConvertorOfJsonObjectToMotionEvent.createMotionEvent(jo);
					PointerCoords outPointerCoords = new PointerCoords();
					me.getPointerCoords(0, outPointerCoords);
					Log.d("haha", me.toString());
					Log.d("hahax", String.valueOf(outPointerCoords.x));
					Log.d("hahay", String.valueOf(outPointerCoords.y));
					//
					// View v = ViewTraversal.getView(outPointerCoords.x, outPointerCoords.y, viewsList);
					// // Log.d("hahaID", String.valueOf(v.getId()));
					// if (me != null && v != null) {
					// Log.d("haha", "dispatchTouchEvent");
					// // v.dispatchTouchEvent(me);
					// //a.dispatchView(v, me);
					// //a.dispatchTouchEvent(me);
					//
					// }
					a.dispatchView(null, me);
					// a.dispatchTouchEvent(me);
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

	private Bitmap getBitmapFromView(View view) {
		// Define a bitmap with the same size as the view
		Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
		// Bind a canvas to it
		Canvas canvas = new Canvas(returnedBitmap);
		// Get the view's background
		Drawable bgDrawable = view.getBackground();
		if (bgDrawable != null)
			// has background drawable, then draw it on the canvas
			bgDrawable.draw(canvas);
		else
			// does not have background drawable, then draw white background on the canvas
			canvas.drawColor(Color.WHITE);
		// draw the view on the canvas
		view.draw(canvas);
		// return the bitmap
		return returnedBitmap;
	}

	private class SimpleDragShadow extends DragShadowBuilder {
		ColorDrawable greyBox;

		public SimpleDragShadow(View view) {
			super(view);
			greyBox = new ColorDrawable(Color.LTGRAY);
		}

		@Override
		public void onDrawShadow(Canvas canvas) {
			// super.onDrawShadow(canvas);
			greyBox.draw(canvas);
			;
		}

		@Override
		public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
			View v = getView();
			int height = (int) v.getHeight() / 2;
			int width = (int) v.getWidth() / 2;

			greyBox.setBounds(0, 0, width, height);
			shadowSize.set(width, height);
			shadowTouchPoint.set((int) width / 2, (int) height / 2);

			// super.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
		}
	}

	private class OurView extends SurfaceView implements Runnable {
		public OurView(Context context) {
			super(context);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub

		}

		public void pause() {

		}

		public void resume() {

		}

	}

}