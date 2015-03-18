package edu.mel06002byui.expirationtracker;

import android.app.NotificationManager;
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

/**
 * Created by James on 2/26/2015.
 */
public class BackgroundNotifier extends Service {

    public static final long NOTIFY_INTERVAL = 20 * 1000; // 20 seconds
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

        notifyBuilder.setSmallIcon(R.drawable.warning_image);
        notifyBuilder.setContentText("You have some items expiring in the next week!");
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
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0,notifyBuilder.build());

                    }
                    else{

                        notifyBuilder.setContentText("No Expiring items");
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0,notifyBuilder.build());
                    }
                }

            });
        }
    }
}