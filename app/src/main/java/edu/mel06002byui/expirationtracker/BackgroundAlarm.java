package edu.mel06002byui.expirationtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by James on 3/31/2015.
 */
public class BackgroundAlarm extends BroadcastReceiver {

    public static String BACKGROUND_ALARM = "myBackgroundAlarm";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("Alarm Receiver", "Entered");

        Bundle bundle = intent.getExtras();
        String action = bundle.getString(BACKGROUND_ALARM);
        if (action.equals(BACKGROUND_ALARM)) {
            Log.i("Alarm Receiver", "If loop");
            Intent inService = new Intent(context, BackgroundNotifier.class);
            context.startService(inService);
        } else {
            Log.i("Alarm Receiver", "Else loop");
        }
    }
}
