package edu.uw.notsetdemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Gunnar on 4/20/2016.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        getFragmentManager().beginTransaction().
                replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
