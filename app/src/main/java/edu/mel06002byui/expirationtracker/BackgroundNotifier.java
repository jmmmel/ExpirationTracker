package edu.mel06002byui.expirationtracker;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by James on 2/26/2015.
 */
public class BackgroundNotifier extends IntentService {
    private GrocerySQLiteHelper db = new GrocerySQLiteHelper(this);
    private NotificationCompat.Builder notifyBuilder
            = new NotificationCompat.Builder(this);

    private static final String TAG = "BackgroundNotifier";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public BackgroundNotifier() {
        super("BackgroundNotifier");

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences settings;
        SharedPreferences.Editor editSettings;
        String notifTitle = "Service";
        String notifMessage = "Running";
        settings = PreferenceManager.getDefaultSharedPreferences(this
                .getApplication());

        final Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.putExtra("extra", "value");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.setAction("android.intent.action.MAIN");
        notificationIntent.addCategory("android.intent.category.LAUNCHER");

        final PendingIntent contentIntent = PendingIntent
                .getActivity(this, 0, notificationIntent, 0);
        notifyBuilder.setSmallIcon(R.drawable.warning);
        notifyBuilder.setContentIntent(contentIntent);
        notifyBuilder.setContentTitle("Expiring");
        boolean validDay = false;
        int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        Set<String> validDays = settings.getStringSet("notify_days", new TreeSet<String>());
        Log.d("DAYSTEST", "TESTING DAYS");
        for (String test : validDays)
            Log.d("DAYSTEST", "day: " + test);
        switch (dayOfWeek) {
            case Calendar.MONDAY:
                validDay = validDays.contains("monday_valid");
                Log.d("DAYSTEST", "monday_valid: " + validDay);
                break;
            case Calendar.TUESDAY:
                validDay = validDays.contains("tuesday_valid");
                Log.d("DAYSTEST", "tuesday_valid: " + validDay);
                break;
            case Calendar.WEDNESDAY:
                validDay = validDays.contains("wednesday_valid");
                Log.d("DAYSTEST", "wednesday_valid: " + validDay);
                break;
            case Calendar.THURSDAY:
                validDay = validDays.contains("thursday_valid");
                Log.d("DAYSTEST", "thursday_valid: " + validDay);
                break;
            case Calendar.FRIDAY:
                validDay = validDays.contains("friday_valid");
                Log.d("DAYSTEST", "friday_valid: " + validDay);
                break;
            case Calendar.SATURDAY:
                validDay = validDays.contains("saturday_valid");
                Log.d("DAYSTEST", "saturday_valid: " + validDay);
                break;
            case Calendar.SUNDAY:
                validDay = validDays.contains("sunday_valid");
                Log.d("DAYSTEST", "sunday_valid: " + validDay);
                break;
        }

        if (validDay && db.expiringItems()) {
            notifyBuilder.
                    setContentText("Some items expiring in a week!");
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, notifyBuilder.build());

        }
    }

}