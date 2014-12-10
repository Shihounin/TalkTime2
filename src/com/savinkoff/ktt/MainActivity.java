package com.savinkoff.ktt;

import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlarmManager; 
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {//changed "extends PreferenceActivity"  to "extends Activity" 
											// I did this because PreferenceActivity requires a ListView in XML.
											// ListView prevents a scrollable textView which we need for the instructions
											// on the "Welcome page".

//	SharedPreferences preferences;
//	String preferencesObjectName = "kttPreferences";
	private AlarmManager alarmManager;
	static public PendingIntent pendingIntent;
	private Intent intent;
	private int intentID = 16894;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Required to allow our textView to scroll
		TextView results;
		results = (TextView) findViewById(R.id.welcome_textview);
		results.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		// create the alarm object
		alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		intent = new Intent(this, AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, intentID, intent, 0);

		// test if there is a pending alarm already set
		Log.i("xxx", "onCreate: Is  AlarmPending?   alarmUp = "	+ AlarmPending());

		if (!AlarmPending()) {
			// getInit();
		} else {
			cancelAlarm();
			// getInit();
		}
	}
	
	@Override
	public void onResume() {
	    super.onResume();  // Always call the superclass method first
	    
	    /*********************************************************************************
	     *onResume
	     *	Check if we should be running. 
	     *		If not running
	     *			cancel any pending notifications
	     *	If we are running
	     *		Check if there is a pending notification. If so, do nothing
	     *		else Start a new notification
	     *	
	     *********************************************************************************/

	    // Check if we should be running
	    if(appRunning()) {
	    	Log.i("xxx", "MainActivity:onResume... The app is running");
	    	
			// test if there is a pending alarm is already set
			if (!AlarmPending()) {
				Log.i("xxx", "onResume: There is no an AlarmPending");
				// Start a new notification
				Log.i("xxx", "onResume: Starting a new alarm");
				scheduleAlarm(this.findViewById(android.R.id.content));
			} else {
				Log.i("xxx", "onResume: There is an AlarmPending");
			}
	    }else {
	    	Log.i("xxx", "MainActivity:onResume... The app is not running");
  		    	cancelAlarm();
	    }
	}

	/**********************************************************************************************
	 * AlarmPending: test if there is a pending alarm already set
	 * 
	 **********************************************************************************************/
	private boolean AlarmPending() {
		// test if there is a pending alarm already set
		boolean alarmUp = (PendingIntent.getBroadcast(this, intentID, intent,
				PendingIntent.FLAG_NO_CREATE) != null);
		return alarmUp;
	}


	/**********************************************************************************************
	 * onCreateOptionsMenu: Inflate the menu; this adds items to the action bar if it is present.
	 * 
	 **********************************************************************************************/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	/**********************************************************************************************
	 * cancelAlarm: Cancel any pending alarm
	 * 
	 **********************************************************************************************/
	public static void cancelAlarm() {
	//	Log.i("xxx", "cancelAlarm: Cancelling Pending Alarm.");
	//	Log.i("xxx", "cancelAlarm: on entry alarmUp = " + AlarmPending());

		pendingIntent.cancel();

	//	Log.i("xxx", "cancelAlarm: on exit alarmUp = " + AlarmPending());
	}


	/**********************************************************************************************
	 * scheduleAlarm: Schedule a new alarm
	 * 
	 **********************************************************************************************/
	public void scheduleAlarm(View V) {

		// Get a random number relative to the setting of the Frequency Slider
		long delayInSeconds = randomDelay();  // delay in seconds
		Log.i("xxx", "delayFactor in seconds = " + delayInSeconds);
		Log.i("xxx", "delayFactor in minutes  = " + (delayInSeconds / 60));
		Log.i("xxx", "delayFactor in hours  = " + (delayInSeconds / 60 / 60));
		
		// Get the pending intent ID
		pendingIntent = PendingIntent.getBroadcast(this, intentID, intent, 0);

		long delay = delayForTesting();
//		delay = 3;
		Log.i("xxx", "scheduleAlarm: delay = "	+ delay + " seconds");

		// Create a time object for testing purposes.
		Long time = new GregorianCalendar().getTimeInMillis() + delay * 1000;

		// set the alarm for particular time
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
		Toast.makeText(this, "Alarm Scheduled for " + delay + " seconds from now", Toast.LENGTH_SHORT).show();
	}
	

	/**********************************************************************************************
	 * delayForTesting: Calculate a number based on the Frequency slider setting
	 * This is for test purposes only<---------------------
	 **********************************************************************************************/
	private long delayForTesting() {
		// Fetch the current frequency preference. 100 = more often. 20 less often.
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		long delayFactor = settings.getInt("delayFactor", 50);	// if not set return 50.
		Log.i("xxx", "delayForTesting: delayFactor = "	+ delayFactor);

		return logslider(delayFactor);
	}
	
	/**********************************************************************************************
	 * logslider: Supply a number from a logarithmic scale
	 * This is for test purposes only<--------------------
	 **********************************************************************************************/
	private int logslider(long position) {
		  // position will be between 0 and 100
		  int minp = 0;
		  int maxp = 100;

		  // The result should be between 10 an 5000000
		  double minv = (double) Math.log(10);
		  double maxv = (double) Math.log(5000000);

		  // calculate adjustment factor
		  double scale = (maxv-minv) / (maxp-minp);

		  return (int) Math.exp(minv + scale*(position-minp));
		}


	/**********************************************************************************************
	 * randomDelay: Calculate a random number based on the Frequency slider setting
	 * 
	 **********************************************************************************************/
	private long randomDelay() {
		
		// Fetch the current frequency preference. 100 = more often. 20 less often.
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		float delayFactor = settings.getInt("delayFactor", 50);	// if not set return 50.
		int iterations = settings.getInt("iterations", 1);	// if not set return 50.
		Log.i("xxx", "randomDelay: iterations = "	+ iterations);

		// Reverse the weight of the delayFactor
		delayFactor = delayFactor/100; 
		// We want the delay factor between .5 and 1.5
		// 		Less Often(0) we multiply delayFactor by 1.5
		//		50% we multiply by 1 (no change)
		// 		More Often(100) multiply delayFactor by .5
		delayFactor = (float) (delayFactor + .5);
		Log.i("xxx", "randomDelay: delayFactor = "	+ delayFactor);
		
		// Using iterations:
		//    If this is the first time since install Get a random number between 8hrs and 12hrs
		//    If this is the second time since install Get a random number between 30hrs and 70hrs
		//    If this is than the second time since install Get a random number between 50hrs and 300hrs
		
		// For now we will pretend that it is always the first time for testing purposes.
		double twelveHoursOfSeconds = 12 * 3600;
		double eightHoursOfSeconds = 8 * 3600;
		
		// Update the Iterations variable in preferences
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("iterations", iterations + 1);
		editor.commit();
		
		return  (long) ((Math.random() * twelveHoursOfSeconds) + eightHoursOfSeconds);
	}
	

	/**********************************************************************************************
	 * onOptionsItemSelected
	 * 
	 **********************************************************************************************/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settings_menu_icon:
			settings(this.findViewById(android.R.id.content));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**********************************************************************************************
	 * Launch settings activity
	 * 
	 **********************************************************************************************/
	public void settings(View v) {
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, SettingsActivity.class);
		startActivity(intent);
	}

	/**********************************************************************************************
	 * Initialize and start the app
	 * 
	 **********************************************************************************************/
	public void start(View v) {
		if (checkSettings()) {
			scheduleAlarm(v);
			finish();
		} else {
			Toast.makeText(this, "Please set Name, Phone number & SMS number in settings", Toast.LENGTH_LONG).show();
		}
	}

	/**********************************************************************************************
	 * Check that all of the settings are entered to ensure proper operation of app
	 * 
	 **********************************************************************************************/
	private boolean checkSettings() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String name = settings.getString("partners_name_key", "NotSet");
		String phone = settings.getString("phone_number_key", "NotSet");
		if (name.equals("NotSet") || phone.equals("NotSet")) {
			return false;
		}
		return true;
	}

	/**********************************************************************************************
	 * Check if the app should be running
	 * 
	 **********************************************************************************************/
	private boolean appRunning() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		Log.i("xxx", "appRunning: running =  "	+ settings.getBoolean("run_checkbox_key", false));

		return settings.getBoolean("run_checkbox_key", false);
	}

	/**********************************************************************************************
	 * Launch help view - currently not implemented
	 * 
	 **********************************************************************************************/
	private void showHelp() {
		// TODO Auto-generated method stub
	}

}
