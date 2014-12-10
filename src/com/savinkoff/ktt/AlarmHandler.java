package com.savinkoff.ktt;

import java.util.Random;

//import com.google.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
//import com.google.android.gms.ads.AdRequest;


public class AlarmHandler extends Activity {
	
	final Context context = this;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_handler);
		
		// Since we are here, cancel the alarm so that a new one can be issued 
		// no matter what happens here
		if(MainActivity.pendingIntent != null) {
			MainActivity.pendingIntent.cancel();
		}
/*
		// Look up the AdView as a resource and load a request.
	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder().build();
	    adView.loadAd(adRequest);

*/
		
		// Place a random suggestion message from an array of 6 messages
        Random random = new Random();
        String[] suggestionArray =   getResources().getStringArray(R.array.suggestions_array);    
        int maxIndex = suggestionArray.length;
        int generatedIndex = random.nextInt(maxIndex);

        String message = getString(R.string.itsTime) + suggestionArray[generatedIndex];
        
		// place the appropriate suggestion text
		TextView suggestion=(TextView)findViewById(R.id.suggestion_id);
		suggestion.setText(message);   
}

	public void later(View view) {
		Log.i("xxx", "Later");
		if(MainActivity.pendingIntent != null) {
			MainActivity.pendingIntent.cancel();
		}
		finish();

	}

	public void phone(View view) {
		
		// cancel the alarm so that a new one can be issued no matter what happens here
		if(MainActivity.pendingIntent != null) {
			MainActivity.pendingIntent.cancel();
		}

		// Get phone number
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String phoneNumber = preferences.getString("phone_number_key", "empty");

		Log.i("xxx", "PhoneNumber = " + phoneNumber);

		if (phoneNumber.equals("empty")) {
			// Phone number is not set
			Toast.makeText(getBaseContext(), "Sorry. No Phone Number set!", Toast.LENGTH_LONG).show();
			finish();

		} 
		 else {
				// Build intent
				Intent phoneCallIntent = new Intent(Intent.ACTION_DIAL);
				phoneCallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				phoneCallIntent.setData(Uri.parse("tel:" + phoneNumber));

				// start the built-in phone application
				getBaseContext().startActivity(phoneCallIntent);
			}
	}

	public void sms(View view) {
	    Intent smsIntent = new Intent(getBaseContext(), SendSMSActivity.class);
	    smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(smsIntent);   
	}

}
