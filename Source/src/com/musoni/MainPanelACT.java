package com.musoni;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainPanelACT extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel_act);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_panel_act, menu);
        return true;
    }
    
}
