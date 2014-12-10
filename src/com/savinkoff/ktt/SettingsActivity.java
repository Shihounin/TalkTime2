package com.savinkoff.ktt;

import android.app.Activity;
import android.os.Bundle;
/*
 * using one fragment for setting view
 * 
 * */
public class SettingsActivity extends Activity {

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	  // TODO Auto-generated method stub
	  super.onCreate(savedInstanceState);
	  
	  getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new PrefsFragment()).commit();
	 }

	}