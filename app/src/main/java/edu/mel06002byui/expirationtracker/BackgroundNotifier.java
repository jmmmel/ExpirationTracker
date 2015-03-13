package edu.mel06002byui.expirationtracker;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by James on 2/26/2015.
 */
public class BackgroundNotifier extends Service{
    private MainActivity monitorMain;
    private List<Grocery> expiresSoon;

    public BackgroundNotifier(MainActivity testMain) {
        monitorMain = testMain;
        expiresSoon = new ArrayList<>();
    }

    private void updateList() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
