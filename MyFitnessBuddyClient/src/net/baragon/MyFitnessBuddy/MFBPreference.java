package net.baragon.MyFitnessBuddy;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import net.baragon.MyFitnessBuddy.client.ClientLocator;


public class MFBPreference extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        Preference button = (Preference) findPreference("pref_clear_database_button");
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {
                ClientLocator.getClient().DeleteLocalData();
                return true;
            }
        });
    }

}
