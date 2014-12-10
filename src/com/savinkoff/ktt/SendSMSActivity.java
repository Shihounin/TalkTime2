package com.savinkoff.ktt;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SendSMSActivity extends Activity {

	private String phoneNumber;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_sms_layout);
		
		// cancel the alarm so that a new one can be issued no matter what happens here
		if(MainActivity.pendingIntent != null) {
			MainActivity.pendingIntent.cancel();
		}

		// Label the send button
		Button sendButton = (Button) this.findViewById(R.id.sms_send_button_id);
		sendButton.setText(getString(R.string.send));

		// Get Name & phone number
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				phoneNumber = preferences.getString("phone_number_key", "empty");
		String partnersName = preferences.getString("partners_name_key", "empty");
		
		// Put Partners name in field
		TextView pName = (TextView) this.findViewById(R.id.smsPartner_key);
		pName.setText(getString(R.string.send_a_text_to) + " " + partnersName);
		
		// If the phone number is not set, then Fail!
		if (phoneNumber.equals("empty")) {
			// Phone number is not set
			Toast.makeText(getBaseContext(), "Sorry. No Phone Number set!", Toast.LENGTH_LONG).show();
			finish();

		} else {
			String formattedNumber = PhoneNumberUtils.formatNumber(phoneNumber);
			// Put Partners phone number in field
			TextView pNumber = (TextView) this.findViewById(R.id.partnerPhone_key);
			pNumber.setText(formattedNumber);
		}

	}
	
	/*********************************************************************************
	 * 		Send the Text message
	 *********************************************************************************/
	public void sendIt(View v) {
		
		// Get the text of the message
		TextView msg = (TextView) this.findViewById(R.id.smsMessage_key);
		String smsMessage = msg.getText().toString();
		
		if (!smsMessage.isEmpty()) {
			SmsManager sms = SmsManager.getDefault();
			sms.sendTextMessage(phoneNumber, null, smsMessage, null, null);
//			Log.i("xxx", "sms phone number = " + phoneNumber);
//			Log.i("xxx", "sms message = " + smsMessage);
			finish();
		}
		
	}
}
