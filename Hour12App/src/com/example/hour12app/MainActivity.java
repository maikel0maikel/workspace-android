package com.example.hour12app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {
	ListView lv;
	Button bt;
	EditText et;
	ProgressBar pb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//
		setTitle("quocdung.net API");
		//get object
		lv = (ListView)findViewById(R.id.listView_result);
		bt = (Button)findViewById(R.id.button_search);
		et = (EditText)findViewById(R.id.editText_songname);
		pb = (ProgressBar)findViewById(R.id.progressBar_working);
		//clear list view
		lv.setAdapter(null);
		//set button click
		bt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//show progressview
				pb.setVisibility(ProgressBar.VISIBLE);
				
				String param = et.getText().toString();
				Fetch_Song_List myt = new Fetch_Song_List();
				myt.execute(param);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private void Display_On_ListView(ArrayList<Song_Info> input)
	{
		ArrayList<String> names =new ArrayList<String>();
		for(Song_Info tmp:input)
		{
			names.add(tmp.name);
		}
		
		lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names)); 
	}
	private class Fetch_Song_List extends AsyncTask<String, String, ArrayList<Song_Info>>
	{
		String API_link = "http://quocdung.net/nct/home/api/find_by_song_name/";//param after
		ArrayList<Song_Info> songs;
		@Override
		protected ArrayList<Song_Info> doInBackground(String... params) {
			// TODO Auto-generated method stub
			HttpURLConnection connection = null;
			try {
				URL dataUrl = new URL(API_link+params[0]);//call api
				connection = (HttpURLConnection) dataUrl.openConnection();
				connection.connect();
				int status = connection.getResponseCode();
				Log.w("qd", "connection status " + status);
				if (status ==200){
				  InputStream is = connection.getInputStream();
				  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				  String responseString;
				  StringBuilder sb = new StringBuilder();
				  while ((responseString = reader.readLine()) != null) {
					  sb = sb.append(responseString);
				  }
				  String json_raw = sb.toString();
				  songs = Song_Info.JSON_To_Array(json_raw);
				  //Tested OK
				  Log.w("qd", "JSON: "+json_raw);
				  
				  return songs;
				}else{
					return (null);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return (null);
			} 
			catch (IOException e) {
				System.out.println("IOException");
				e.printStackTrace();
				return (null);
			} catch (NullPointerException e) {
				e.printStackTrace();
				return (null);
			} finally {
				connection.disconnect();
			}
		}
		@Override
		protected void onPostExecute(ArrayList<Song_Info> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result!=null)
			{
				//call display to listview
				Display_On_ListView(result);
				//hide progressbar
				pb.setVisibility(ProgressBar.INVISIBLE);
			}
			else
			{
				Toast.makeText(MainActivity.this, "Internet or Server fail", 2000).show();
				pb.setVisibility(ProgressBar.INVISIBLE);
			}
		}
	}
}
