package com.uzumaki.naruto;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by aarushi on 27/3/15.
 */
public class settings extends ActionBarActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Intent intent = getIntent();

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    SharedPreferences.Editor editor;
                    editor = getSharedPreferences("com.uzumaki.naruto", MODE_PRIVATE).edit();
                    editor.putBoolean("Shuffle", true);
                    editor.commit();
                } else {
                    // The toggle is disabled
                    SharedPreferences.Editor editor = getSharedPreferences("com.uzumaki.naruto", MODE_PRIVATE).edit();
                    editor.putBoolean("Shuffle", false);
                    editor.commit();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
            finish(); // finish activity
    }
}
