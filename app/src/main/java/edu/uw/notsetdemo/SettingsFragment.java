package edu.uw.notsetdemo;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Gunnar on 4/20/2016.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
