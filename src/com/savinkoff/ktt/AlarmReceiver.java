package com.savinkoff.ktt;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
	private NotificationManager notificationManager;
	NotificationCompat.Builder myBuilder;
	private static final int MY_NOTIFICATION_ID = 1;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Intent myIntent = new Intent(context, AlarmHandler.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		myBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.heart)
				.setContentTitle(context.getString(R.string.app_name))	// <string name="app_name">Talk Time</string> 
				.setContentText(context.getString(R.string.app_desc))	//<string name="app_desc">Time to build on your relationship</string>
				.setContentIntent(pendingIntent)
				.setTicker(context.getString(R.string.app_desc))		// <string name="app_desc">Time to build on your relationship</string>
				.setAutoCancel(true);

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean sound = preferences.getBoolean("sound_checkbox", true);

		if (sound) {
			// Use phones current settings
			myBuilder.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true);
		} else {
			// silent mode
			myBuilder.setDefaults(0).setAutoCancel(true);
		}

		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(MY_NOTIFICATION_ID, myBuilder.build());
	}

}