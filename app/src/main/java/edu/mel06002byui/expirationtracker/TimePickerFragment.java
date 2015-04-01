package edu.mel06002byui.expirationtracker;

/**
 * Created by James on 4/1/2015.
 */

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Used to manipulate settings dialog
 */
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        Log.d("OnTimeSet", "Hour: " + hourOfDay);
        Log.d("OnTimeSet", "Minute: " + minute);
        SharedPreferences settings
                = this.getActivity().getSharedPreferences("notifySettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("notify_hour",hourOfDay);
        editor.putInt("notify_minutes",minute);
        editor.commit();
        startSchedule();

    }
    private void startSchedule() {

        try {
            SharedPreferences settings = this.getActivity()
                    .getSharedPreferences("notify_settings",Context.MODE_PRIVATE);
            SharedPreferences.Editor prefEditor = settings.edit();
            AlarmManager alarms = (AlarmManager) this
                    .getActivity()
                    .getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(this
                    .getActivity()
                    .getApplicationContext(),
                    BackgroundAlarm.class);
            intent.putExtra(BackgroundAlarm.BACKGROUND_ALARM,
                    BackgroundAlarm.BACKGROUND_ALARM);

            final PendingIntent pIntent = PendingIntent.getBroadcast(this.getActivity(),
                    1234567, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR,settings.getInt("notify_hour",9));
            today.set(Calendar.MINUTE, settings.getInt("notify_minutes",0));
            today.set(Calendar.MILLISECOND,0);
            Log.d("TIMECHAMBER", "SystemTime: " + System.currentTimeMillis());
            Log.d("TIMECHAMBER", "AlarmTime: " + today.getTimeInMillis());
            Log.d("TIMECHAMBER", "Difference: " + (today.getTimeInMillis()-System.currentTimeMillis()));
            alarms.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pIntent);
        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}