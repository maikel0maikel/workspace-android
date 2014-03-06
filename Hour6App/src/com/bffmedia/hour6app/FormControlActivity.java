package com.bffmedia.hour6app;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FormControlActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_control_layout);
		final Button swapButton=(Button)findViewById(R.id.button2);
		final Drawable mIcon = getResources().getDrawable(R.drawable.ic_launcher);
		final Drawable mSkateboardIcon=getResources().getDrawable(R.drawable.robot_skateboarding);
		swapButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			if(swapButton.getText().equals("Button")){
				swapButton.setText("Skateboarder");
				swapButton.setCompoundDrawablesWithIntrinsicBounds(mSkateboardIcon, null, null, null);
			}else{
				swapButton.setText("Button");
				swapButton.setCompoundDrawablesWithIntrinsicBounds(mIcon, null, null, null);
			}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
