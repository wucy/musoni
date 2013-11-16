package c4g.group18.musoni;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class NewHub extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_hub);
		Intent intent = getIntent();
		boolean ok = intent.getBooleanExtra("BOOL_PIN", false);
		
		if(ok){
		TextView okok = new TextView(this);
		okok.setTextSize(40);
		okok.setText("OK");
		}
		else
		{
			Intent newI = new Intent(this, MainActivity.class);
			startActivity(newI);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_hub, menu);
		return true;
	}

}
