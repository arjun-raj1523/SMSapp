package com.example.smsappv2;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class ServiceSMS extends Service {

	EditText txtphoneNo;
	EditText txtMessage;
	String phoneNo;
	String txtM;
	int i = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {

		// Log.i("Works",phoneNo);
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			Integer keyCount = bundle.keySet().toString().length();
			if (keyCount == 37) {
				phoneNo = bundle.getString("phoneNo");
				txtM = bundle.getString("txtMessage");

			} else {

				if (i == 0) {
					Log.i("when i=0","when i=0");
					i++;

				} else if (i == 1) {
					Log.i("when i=1","when i=1");
					sendSMSMessage(phoneNo, txtM);
					i++;
					stopSelf();
				} else {
					Log.i("Works", "not Sent");

				}
			}

		}

		return START_STICKY;

	}

	public void onDestroy() {

		super.onDestroy();
		Toast.makeText(this, "Service Stopped", Toast.LENGTH_LONG).show();
	}

	protected void sendSMSMessage(String pNo, String tm) {
		String phoneNumber = pNo;
		String textmessage = tm;

		try {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(phoneNumber, null, textmessage, null,
					null);
			Toast.makeText(getApplicationContext(), "SMS sent.",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(),
					"SMS faild, please try again.", Toast.LENGTH_LONG).show();

		}
	}

}
