package edu.mel06002byui.expirationtracker;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

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
     *
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
        settings = getSharedPreferences("notifySettings", Context.MODE_PRIVATE);
        editSettings = settings.edit();
        editSettings.putBoolean("firstStart", false);
        editSettings.commit();
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
        if (db.expiringItems()) {
            notifyBuilder.
                    setContentText("Some items expiring in a week!");
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, notifyBuilder.build());

        }
    }

}