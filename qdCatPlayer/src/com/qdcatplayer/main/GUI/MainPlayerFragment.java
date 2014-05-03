package com.qdcatplayer.main.GUI;
import android.app.Fragment;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.qdcatplayer.main.Utilities.Utilities;
import com.qdcatplayer.main.R;
import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.qdcatplayer.main.R;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

public abstract class MainPlayerFragment extends Fragment{
	private MediaPlayer mp;
	private Handler mHandler = new Handler();
	private Utilities utils=new Utilities();
	private TextView songTotalDurationLabel;
	private TextView songCurrentDurationLabel;
	private TextView songNameLabel;
	private SeekBar pro_bar;
	private TextView artistLabel;
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }  
	 private Runnable mUpdateTimeTask = new Runnable() {
         public void run() {
             long totalDuration = mp.getDuration();
             long currentDuration = mp.getCurrentPosition();
             // Displaying Total Duration time
             songTotalDurationLabel.setText(""+ utils.milliSecondsToTimer(totalDuration)); 
             // Displaying time completed playing
             songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
             // Updating progress bar
             int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
           //  int progress=(int)(((double)currentDuration/totalDuration)*100);
             //Log.d("Progress", ""+progress);
             pro_bar.setProgress(progress);
             // Running this thread after 100 milliseconds
             mHandler.postDelayed(this, 100);
         	}
      };
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState)
	{
		View v=inflater.inflate(R.layout.player_main, container,false);
		final ImageButton btn_play=(ImageButton)v.findViewById(R.id.imageButton_play);
		final ImageButton btn_shuffle=(ImageButton)v.findViewById(R.id.imageButton_shuffle);
		final ImageButton btn_repeat=(ImageButton)v.findViewById(R.id.imageButton_repeat);
		final ImageButton btn_fav=(ImageButton)v.findViewById(R.id.imageButton_favorite);
		pro_bar=(SeekBar)v.findViewById(R.id.seekBar_duration);
		songCurrentDurationLabel=(TextView)v.findViewById(R.id.textView_last_duration);
		songTotalDurationLabel=(TextView)v.findViewById(R.id.textView_remain_duration);
		songNameLabel=(TextView)v.findViewById(R.id.textView_SongName);
		artistLabel=(TextView)v.findViewById(R.id.textview_Artist);
		mp=new MediaPlayer();
		btn_play.setTag("Play");
		btn_shuffle.setTag("UnSelected");
		btn_repeat.setTag("UnSelected");
		btn_fav.setTag("UnSelected");
		pro_bar.setMax(100);
		btn_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_play.getTag().equals("Play")){
					if(!mp.isPlaying()){
					btn_play.setImageResource(R.drawable.pause);
					if(mp!=null)
						try {
							// pass song path
							mp.setDataSource("/storage/extSdCard/Music/Baby.mp3");
							mp.prepare();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					mp.start();
					//update total time label
					songTotalDurationLabel.setText(utils.milliSecondsToTimer(mp.getDuration()));
					//update song name label
					songNameLabel.setText("Baby");
					//update artist label
					artistLabel.setText("Justin Bieber");
					//set Tag
					btn_play.setTag("Pause");
					//kim: Su dung cach code mau
//					pro_bar.setProgress(0);
//					updateProgressBar();
					//kim: Su dung asynctask
					ShowProgressTask pt=new ShowProgressTask();
					pt.execute();
					
					}
				}
				else if(btn_play.getTag().equals("Pause")){
					if(mp!=null){
					mp.pause();
					btn_play.setImageResource(R.drawable.play_bg_auto);
					btn_play.setTag("Play");
					}
				}
			}
		});
		btn_fav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_fav.getTag().equals("UnSelected")){
					btn_fav.setImageResource(R.drawable.favorite_selected);
					btn_fav.setTag("Selected");
				}
				else if(btn_fav.getTag().equals("Selected")){
					btn_fav.setImageResource(R.drawable.favorite);
					btn_fav.setTag("UnSelected");
				}
			}
		} );
		btn_repeat.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_repeat.getTag().equals("UnSelected")){
					btn_repeat.setImageResource(R.drawable.repeat_selected);
					btn_repeat.setTag("Selected");
				}
				else if(btn_repeat.getTag().equals("Selected")){
					btn_repeat.setImageResource(R.drawable.repeat_auto);
					btn_repeat.setTag("UnSelected");
				}
			}
		} );
		btn_shuffle.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_shuffle.getTag().equals("UnSelected")){
					btn_shuffle.setImageResource(R.drawable.shuffle_selected);
					btn_shuffle.setTag("Selected");
				}
				else if(btn_shuffle.getTag().equals("Selected")){
					btn_shuffle.setImageResource(R.drawable.shuffle_auto);
					btn_shuffle.setTag("UnSelected");
				}
			}
		} );
// Listeners control seekbar when click on
		pro_bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				  mHandler.removeCallbacks(mUpdateTimeTask);
			      int totalDuration = mp.getDuration();
			      int currentPosition = (int)((((double)seekBar.getProgress())/100)*totalDuration);
			      // forward or backward to certain seconds
			      mp.seekTo(currentPosition);
			      // update timer progress again
			      //kim:  su dung theo code mau
			     updateProgressBar();
			     //kim: su dung async task
			      ShowProgressTask tk=new ShowProgressTask();
			      tk.execute(); 
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				 mHandler.removeCallbacks(mUpdateTimeTask);
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}
		});
		mp.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				
			}
		});
		return v;
		
	}
	private class ShowProgressTask extends AsyncTask<Void, Integer,Integer>{
		@Override
		protected void onPreExecute(){
			pro_bar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			while(mp.getCurrentPosition()<=mp.getDuration()){
				
				long a=mp.getCurrentPosition();
				long b=mp.getDuration();
				int i=(int)(((double)a/b)*100);
				try{
					Thread.sleep(1000);
				}catch (InterruptedException e) {
	                return -1;
	            } catch (Exception e) {
	                return -1;
	            }
				publishProgress(i);
			}
			return mp.getDuration();
		}
		@Override
		protected void onProgressUpdate(Integer...progess){
	          long currentDuration = mp.getCurrentPosition();
			int progress=progess[0];
			pro_bar.setProgress(progress);
			songCurrentDurationLabel.setText(utils.milliSecondsToTimer(currentDuration));
			
		}
		@Override
		protected void onPostExecute(Integer result){
			pro_bar.setVisibility(View.INVISIBLE);
		}
	}
}
