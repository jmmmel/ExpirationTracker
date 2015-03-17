package edu.mel06002byui.expirationtracker;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2/26/2015.
 */
public class BackgroundNotifier extends Service{

    private static final String TAG = "BackgroundNotifier";

    @Override
    public void onCreate(){
        Toast.makeText(this, "Congrats! MyService Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy(){

    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
