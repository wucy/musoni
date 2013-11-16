package c4g.group18.musoni;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

@SuppressLint("NewApi") public class MainActivity extends Activity {
	
	protected final int pin = 1234;
	public final static boolean BOOL_PIN = false;

    @SuppressLint("NewApi") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);

        final View customActionBarView = inflater.inflate(R.layout.actionbar_view, null);

        /* Show the custom action bar view and hide the normal Home icon and title */
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setIcon(R.drawable.musoni_logo);
        actionBar.setCustomView(customActionBarView);
        actionBar.setDisplayShowCustomEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void pinSucceed(View view)
    {
    	Intent intent = new Intent(this, NewHub.class);
    	EditText editPin = (EditText) findViewById(R.id.pin);
    	int myPin = Integer.parseInt(editPin.getText().toString());
    	intent.putExtra("BOOL_PIN", myPin == pin);
    	startActivity(intent);
    }
    
}
