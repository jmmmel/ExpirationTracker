package edu.mel06002byui.expirationtracker;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class SetPreferences extends PreferenceActivity {

    private GrocerySQLiteHelper db = new GrocerySQLiteHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        PreferenceFragment fragment = new PrefsFragment();


        getFragmentManager().beginTransaction().replace(android.R.id.content,
                fragment).commit();

        getFragmentManager().executePendingTransactions();
    }

    public static class PrefsFragment extends PreferenceFragment
            implements Preference.OnPreferenceClickListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
            findPreference("clearDatabase").setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if(preference.getKey().equals("clearDatabase")) {
                new GrocerySQLiteHelper(this.getActivity()).clearDatabase();
                SharedPreferences.Editor editPref = PreferenceManager
                        .getDefaultSharedPreferences(this.getActivity()).edit();
                editPref.putBoolean("clearDatabase", true);
                editPref.commit();

            }
            return true;
        }
    }
}