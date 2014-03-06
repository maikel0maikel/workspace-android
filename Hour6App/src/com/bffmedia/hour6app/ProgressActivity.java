package com.bffmedia.hour6app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class ProgressActivity extends Activity {
	ProgressBar mProgressBar;
	ProgressBar mHorizontalProgressBar;
	SeekBar mSeekBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_layout);
		mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
		mProgressBar.setVisibility(View.INVISIBLE);
		mHorizontalProgressBar = (ProgressBar)findViewById(R.id.progressBar2);
		mSeekBar = (SeekBar)findViewById(R.id.seekBar1);
		mSeekBar.setMax(100);
		Button btnStart=(Button)findViewById(R.id.button1);
		btnStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ShowProgressTask showTask = new ShowProgressTask();
				showTask.execute();
			}
		});
	
	}
	private class ShowProgressTask extends AsyncTask<Void, Integer,Integer>{
		@Override
		protected void onPreExecute(){
			mProgressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			for(int i=1;i<=100;i++)
			{
				try{
					Thread.sleep(100);
					publishProgress(i);
				}catch(InterruptedException e){
					return -1;
				}
			}
			return 100;
		}
		@Override
		protected void onProgressUpdate(Integer...progess){
			int progress=progess[0];
			mHorizontalProgressBar.setProgress(progress);
			mSeekBar.setProgress(progress);
			
		}
		@Override
		protected void onPostExecute(Integer result){
			mProgressBar.setVisibility(View.INVISIBLE);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
