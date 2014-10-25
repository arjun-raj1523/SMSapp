package com.example.smsappv2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	static int i=0;
	@Override
	public void onReceive(Context arg0, Intent arg1) {

		// For our recurring task, we'll just display a message
		if(i==0)
		{
			i++;
			Log.i("Works","When i=0");

		}
		else if(i==1){
			Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
			Log.i("Works","Sent");
			i++;
		}
		else
		{ 
			Log.i("Works","not Sent");
			
		}

	}

}
