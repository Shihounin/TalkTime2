package com.savinkoff.ktt;

import android.app.Activity;
import android.os.Bundle; 
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

	public class SMSsendFinal extends Activity {

		private String phoneNumber;
		private String smsMessage;
		private EditText pn;
		private EditText msg;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.sms_send_final);
			
			phoneNumber = getIntent().getExtras().getString("phone");
			smsMessage = getIntent().getExtras().getString("message");
			
			pn = (EditText) findViewById(R.id.sms_phone_number);
			pn.setText(phoneNumber);
			
			msg = (EditText) findViewById(R.id.sms_message);
			msg.setText(smsMessage);
			
		}

	public void sendIt(View v) {
		// Send the message
		smsMessage = msg.getText().toString();
		phoneNumber = pn.getText().toString();
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, smsMessage, null, null);
		
		Log.i("xxx", "sms phone number = " + phoneNumber);
		Log.i("xxx", "sms message = " + smsMessage);

		finish();
	}
}
