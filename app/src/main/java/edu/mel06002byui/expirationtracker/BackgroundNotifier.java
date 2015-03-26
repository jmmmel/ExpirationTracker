package edu.mel06002byui.expirationtracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by James on 2/26/2015.
 */
public class BackgroundNotifier extends Service {

    public static final long NOTIFY_INTERVAL
            = TimeUnit.SECONDS.convert(7, TimeUnit.DAYS) * 1000; // 7 days
    private GrocerySQLiteHelper db = new GrocerySQLiteHelper(this);
    private NotificationCompat.Builder notifyBuilder
            = new NotificationCompat.Builder(this);

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;

    private static final String TAG = "BackgroundNotifier";

    @Override
    public void onCreate() {

        String notifTitle = "Service";
        String notifMessage = "Running";

        final Intent notificationIntent = new Intent(this,   MainActivity.class);
        notificationIntent.putExtra("extra", "value");
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationIntent.setAction("android.intent.action.MAIN");
        notificationIntent.addCategory("android.intent.category.LAUNCHER");

        final PendingIntent contentIntent = PendingIntent
                .getActivity(this, 0, notificationIntent,0);
        notifyBuilder.setSmallIcon(R.drawable.warning_image);
        notifyBuilder.setContentIntent(contentIntent);
        notifyBuilder.setContentTitle("Expiring");
        Toast.makeText(this, "Notificatons Started", Toast.LENGTH_LONG).show();
        // cancel if already existed
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);

    }

    @Override
    public void onDestroy() {
        mTimer.cancel();
        Toast.makeText(this, "Notificatons Stopped", Toast.LENGTH_LONG).show();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    if(db.expiringItems()) {
                        notifyBuilder.
                                setContentText("Some items expiring in a week!");
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0,notifyBuilder.build());

                    }

                }

            });
        }
    }
}