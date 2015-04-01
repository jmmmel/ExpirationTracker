package edu.mel06002byui.expirationtracker;

import android.app.Activity;
import android.os.Bundle;

public class SetPreferences extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MainActivity.PrefsFragment()).commit();
    }

}