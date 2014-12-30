package com.example.androidsocketsserver;

import android.app.Activity;
import android.os.Bundle;

public class ProgrammaticallyCreateLayout extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(new MyCustomView(this, null));	
	}
}
