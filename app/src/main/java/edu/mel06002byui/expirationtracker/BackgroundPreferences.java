package edu.mel06002byui.expirationtracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by James on 4/1/2015.
 */
public class BackgroundPreferences extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
