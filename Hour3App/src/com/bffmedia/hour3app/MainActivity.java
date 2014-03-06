package com.bffmedia.hour3app;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.R.integer;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		final SharedPreferences a=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if(a.getBoolean("custom_text", false)){
			TextView t=(TextView)findViewById(R.id.textView1);
			t.setText(a.getString("edit_text", "Be Kim"));
			t=(TextView)findViewById(R.id.TextView01);
			t.setText(a.getString("edit_text", "Be Kim"));
			t=(TextView)findViewById(R.id.TextView02);
			t.setText(a.getString("edit_text", "Be Kim"));
			t=(TextView)findViewById(R.id.TextView03);
			t.setText(a.getString("edit_text", "Be Kim"));
		}
		choose_images(Integer.parseInt(a.getString("images", "1")));
		a.registerOnSharedPreferenceChangeListener(new OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
				// TODO Auto-generated method stub
				if(arg1.equals("custom_text")){
					if(!arg0.getBoolean("custom_text", false))
					{
						TextView t=(TextView)findViewById(R.id.textView1);
						t.setText("Be Kim");
						t=(TextView)findViewById(R.id.TextView01);
						t.setText("Be Kim");
						t=(TextView)findViewById(R.id.TextView02);
						t.setText("Be Kim");
						t=(TextView)findViewById(R.id.TextView03);
						t.setText("Be Kim");
					}
					else{
						TextView t=(TextView)findViewById(R.id.textView1);
						t.setText(a.getString("edit_text", "Be Kim"));
						t=(TextView)findViewById(R.id.TextView01);
						t.setText(a.getString("edit_text", "Be Kim"));
						t=(TextView)findViewById(R.id.TextView02);
						t.setText(a.getString("edit_text", "Be Kim"));
						t=(TextView)findViewById(R.id.TextView03);
						t.setText(a.getString("edit_text", "Be Kim"));
					}
				}
				else if(arg1.equals("edit_text"))
				{
					TextView t=(TextView)findViewById(R.id.textView1);
					t.setText(a.getString(arg1, "Be Kim"));
					t=(TextView)findViewById(R.id.TextView01);
					t.setText(a.getString(arg1, "Be Kim"));
					t=(TextView)findViewById(R.id.TextView02);
					t.setText(a.getString(arg1, "Be Kim"));
					t=(TextView)findViewById(R.id.TextView03);
					t.setText(a.getString(arg1, "Be Kim"));
					
				}
				else{
				String k1=a.getString("images", "unknown");
				int k=Integer.parseInt(k1);
				choose_images(k);
				}
			}
		});
		InputStream input=getResources().openRawResource(R.raw.instructions);
		try {
			String text=inputStreamToString(input);
			TextView t=(TextView)findViewById(R.id.textView2);
			t.setText(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void choose_images(int k){
		final ImageView logo=(ImageView)findViewById(R.id.imageView1);
		switch(k){
		case 1:
			logo.setImageResource(R.drawable.ic_launcher);break;
		case 2:
			logo.setImageResource(R.drawable.kim1);break;
		default:
			logo.setImageResource(R.drawable.madagascar1);break;
	}
		Animation fade=AnimationUtils.loadAnimation(this, R.anim.fadein);
		logo.startAnimation(fade);
		}
public String inputStreamToString(InputStream is) throws IOException{
	StringBuffer sBuffer=new StringBuffer();
	DataInputStream dataIO=new DataInputStream(is);
	String strLine=null;
	while((strLine=dataIO.readLine())!=null){
		sBuffer.append(strLine+"\n");
	}
	dataIO.close();
	is.close();
	return sBuffer.toString();
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.action_settings:
			Intent intent=new Intent(this,SettingActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
