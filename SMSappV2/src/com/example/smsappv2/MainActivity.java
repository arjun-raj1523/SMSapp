package com.example.smsappv2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button sendBtn, setDateBtn, setTimeBtn;
	EditText txtphoneNo;
	EditText date;
	EditText txtMessage;
	EditText time;
	static final int DATE_DIALOG_ID = 999;
	static final int TIME_DIALOG_ID = 99;
	int year;
	int month;
	int day;
	int hour;
	int minute;
	String currentTime, currentDate;
	private PendingIntent pendingIntent;
	private AlarmManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sendBtn = (Button) findViewById(R.id.btnSendSMS);
		setDateBtn = (Button) findViewById(R.id.setDate);
		setTimeBtn = (Button) findViewById(R.id.setTime);
		txtphoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
		date = (EditText) findViewById(R.id.date);
		time = (EditText) findViewById(R.id.time);
		txtMessage = (EditText) findViewById(R.id.editTextSMS);

		final Intent alarmIntent = new Intent(getBaseContext(),
				ServiceSMS.class);
		pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);

		sendBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				/*
				 * //startService(new
				 * Intent(getBaseContext(),ServiceSMS.class)); Intent intent =
				 * new Intent(getBaseContext(),ServiceSMS.class); Bundle bundle
				 * = new Bundle();
				 * bundle.putString("phoneNo",txtphoneNo.getText().toString());
				 * bundle
				 * .putString("txtMessage",txtMessage.getText().toString());
				 * intent.putExtras(bundle); startService(intent);
				 */
				stopService(alarmIntent);
				setCurrentTime();
				setCurrentDate();
				Calendar cal = Calendar.getInstance();
				manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

				SimpleDateFormat format = new SimpleDateFormat(
						"MM-dd-yyyy HH:mm");
				String setDateTime = date.getText().toString() + " "
						+ time.getText().toString();
				String currentDateTime = currentDate.toString() + " "
						+ currentTime.toString();
				Date d1 = null;
				Date d2 = null;
				try {
					d1 = format.parse(setDateTime);
					d2 = format.parse(currentDateTime);
					Long diff = (d1.getTime() - d2.getTime());
					Log.i("works12", "Works in " + diff.toString());
					Bundle bundle = new Bundle();
					bundle.putString("phoneNo", txtphoneNo.getText().toString());
					bundle.putString("txtMessage", txtMessage.getText()
							.toString());
					bundle.putInt("serviceCounter",1);
					alarmIntent.putExtras(bundle);
					startService(alarmIntent);
					manager.setRepeating(AlarmManager.RTC_WAKEUP,
							cal.getTimeInMillis(), diff, pendingIntent);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		setTimeBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				showDialog(TIME_DIALOG_ID);
			}
		});
		setDateBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		setCurrentDateOnView();
		setCurrentTimeOnView();

	}

	public void setCurrentTime() {

		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		// set current time into textview
		currentTime = hour + ":" + minute;
		// set current time into timepicker

	}

	public void setCurrentDate() {

		// dpResult = (DatePicker) findViewById(R.id.dpResult);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		currentDate = (month + 1) + "-" + (day) + "-" + (year) + " ";
		// set current date into datepicker
		// dpResult.init(year, month, day, null);

	}

	public void setCurrentTimeOnView() {

		final Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);

		// set current time into textview
		time.setText(new StringBuilder().append(pad(hour)).append(":")
				.append(pad(minute)));
		currentTime = time.getText().toString();
		// set current time into timepicker

	}

	public void setCurrentDateOnView() {

		// dpResult = (DatePicker) findViewById(R.id.dpResult);

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		date.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(month + 1).append("-").append(day).append("-")
				.append(year).append(" "));
		currentDate = date.getText().toString();
		// set current date into datepicker
		// dpResult.init(year, month, day, null);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, datePickerListener, year, month,
					day);

		case TIME_DIALOG_ID:
			// set time picker as current time
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);

		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			hour = selectedHour;
			minute = selectedMinute;

			// set current time into textview
			time.setText(new StringBuilder().append(pad(hour)).append(":")
					.append(pad(minute)));

		}
	};

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;

			// set selected date into textview
			date.setText(new StringBuilder().append(month + 1).append("-")
					.append(day).append("-").append(year).append(" "));
			;

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
