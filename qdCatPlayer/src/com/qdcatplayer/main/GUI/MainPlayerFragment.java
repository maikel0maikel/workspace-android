package com.qdcatplayer.main.GUI;

import android.app.Fragment;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.qdcatplayer.main.Entities.MySong;
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

public abstract class MainPlayerFragment extends Fragment {
	public interface MyMainPLayerDataProvider {
		public MediaPlayer getMediaPlayer();

		public MySong getCurrentSong();

		public MySong getNextSong();

		public MySong getPrevSong();

		public Integer getCurrentPostion();

		/**
		 * 
		 * @return true (isPlaying), false (pause or stop)
		 */
		public Boolean getPlayingState();

		public void setPlayingState(Boolean isPlaying);
	}

	/*
	 * 
	 */
	private ShowProgressTask task = null;
	/*
	 * Listener to Activity
	 */
	private MyMainPLayerDataProvider mListener = null;
	/*
	 * View instances
	 */
	//private Handler mHandler = new Handler();
	private Utilities utils = new Utilities();
	private TextView songTotalDurationLabel;
	private TextView songCurrentDurationLabel;
	private TextView songNameLabel;
	private SeekBar pro_bar;
	private TextView artistLabel;
	private ImageButton btn_play;
	private ImageButton btn_shuffle;
	private ImageButton btn_repeat;
	private ImageButton btn_fav;
	private MediaPlayer mp;

	/*
	 * public void updateProgressBar() { mHandler.postDelayed(mUpdateTimeTask,
	 * 100); }
	 */

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mListener = (MyMainPLayerDataProvider) activity;
	}
	/*
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = mp.getDuration();
			long currentDuration = mp.getCurrentPosition();
			// Displaying Total Duration time
			songTotalDurationLabel.setText(""
					+ utils.milliSecondsToTimer(totalDuration));
			// Displaying time completed playing
			songCurrentDurationLabel.setText(""
					+ utils.milliSecondsToTimer(currentDuration));
			// Updating progress bar
			int progress = (int) (utils.getProgressPercentage(currentDuration,
					totalDuration));
			// int progress=(int)(((double)currentDuration/totalDuration)*100);
			// Log.d("Progress", ""+progress);
			pro_bar.setProgress(progress);
			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};
	*/

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle saveInstanceState) {
		View v = inflater.inflate(R.layout.player_main, container, false);
		// get view instances
		btn_play = (ImageButton) v.findViewById(R.id.imageButton_play);
		btn_shuffle = (ImageButton) v.findViewById(R.id.imageButton_shuffle);
		btn_repeat = (ImageButton) v.findViewById(R.id.imageButton_repeat);
		btn_fav = (ImageButton) v.findViewById(R.id.imageButton_favorite);
		pro_bar = (SeekBar) v.findViewById(R.id.seekBar_duration);
		songCurrentDurationLabel = (TextView) v
				.findViewById(R.id.textView_last_duration);
		songTotalDurationLabel = (TextView) v
				.findViewById(R.id.textView_remain_duration);
		songNameLabel = (TextView) v.findViewById(R.id.textView_SongName);
		artistLabel = (TextView) v.findViewById(R.id.textview_Artist);
		// get from provider
		mp = mListener.getMediaPlayer();
		prepareMediaPlayer();
		// set pre value from provider
		// ...
		updateSongInfo();

		// set view tag
		btn_play.setTag("Play");
		btn_shuffle.setTag("UnSelected");
		btn_repeat.setTag("UnSelected");
		btn_fav.setTag("UnSelected");
		pro_bar.setMax(100);

		// set listener for view
		btn_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean isPlaying = mListener.getPlayingState();
				if (isPlaying) {
					pauseMediaPLayer();
				} else {
					playMediaPLayer();
				}
			}
		});
		btn_fav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btn_fav.getTag().equals("UnSelected")) {
					btn_fav.setImageResource(R.drawable.favorite_selected);
					btn_fav.setTag("Selected");
				} else if (btn_fav.getTag().equals("Selected")) {
					btn_fav.setImageResource(R.drawable.favorite);
					btn_fav.setTag("UnSelected");
				}
			}
		});
		btn_repeat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btn_repeat.getTag().equals("UnSelected")) {
					btn_repeat.setImageResource(R.drawable.repeat_selected);
					btn_repeat.setTag("Selected");
				} else if (btn_repeat.getTag().equals("Selected")) {
					btn_repeat.setImageResource(R.drawable.repeat_auto);
					btn_repeat.setTag("UnSelected");
				}
			}
		});
		btn_shuffle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (btn_shuffle.getTag().equals("UnSelected")) {
					btn_shuffle.setImageResource(R.drawable.shuffle_selected);
					btn_shuffle.setTag("Selected");
				} else if (btn_shuffle.getTag().equals("Selected")) {
					btn_shuffle.setImageResource(R.drawable.shuffle_auto);
					btn_shuffle.setTag("UnSelected");
				}
			}
		});
		// Listeners control seekbar when click on
		pro_bar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// mHandler.removeCallbacks(mUpdateTimeTask);
				int totalDuration = mp.getDuration();
				int currentPosition = (int) ((((double) seekBar.getProgress()) / 100) * totalDuration);
				if (currentPosition >= totalDuration) {
					currentPosition = totalDuration - 1;
				}
				seekMediaPlayer(currentPosition);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// mHandler.removeCallbacks(mUpdateTimeTask);
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
				mListener.setPlayingState(false);
			}
		});

		// done
		return v;

	}

	/**
	 * 
	 * @param currentPosition
	 *            milisec
	 */
	protected void seekMediaPlayer(int currentPosition) {
		if (mp == null) {
			return;
		}
		if (task != null && !task.isCancelled()) {
			task.cancel(true);
			task = null;
		}
		// forward or backward to certain seconds
		mp.seekTo(currentPosition);
		// update timer progress again
		// kim: su dung theo code mau
		// updateProgressBar();
		// kim: su dung async task
		task = new ShowProgressTask();
		task.execute();
	}
	/**
	 * call when creating View or switch Song
	 */
	private void updateSongInfo() {
		// update total time label
		songTotalDurationLabel.setText(utils.milliSecondsToTimer(mListener
				.getCurrentSong().getDuration() * 1000));
		// update song name label
		songNameLabel.setText(mListener.getCurrentSong().getTitle());
		// update artist label
		artistLabel.setText(mListener.getCurrentSong().getArtist().getName());
	}
	/**
	 * khoi tao truoc khi play or pause
	 */
	private void prepareMediaPlayer() {
		if (mp == null) {
			return;
		}
		try {
			mp.reset();
			mp.setDataSource(mListener.getCurrentSong().getPath().getAbsPath());
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
	}
	
	private void playMediaPLayer() {
		if (mp == null) {
			return;
		}
		if (task != null && !task.isCancelled()) {
			task.cancel(true);
			task = null;
		}
		try {
			mp.start();

			/*
			 * update view
			 */
			btn_play.setImageResource(R.drawable.pause);
			// set Tag
			btn_play.setTag("Pause");
			mListener.setPlayingState(true);
			// kim: Su dung cach code mau
			// pro_bar.setProgress(0);
			// updateProgressBar();
			// kim: Su dung asynctask
			task = new ShowProgressTask();
			task.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class ShowProgressTask extends AsyncTask<Void, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			//pro_bar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Integer doInBackground(Void... params) {
			Integer b = mp.getDuration();
			Integer a = 0;
			// TODO Auto-generated method stub
			while (mp.isPlaying() && mp.getCurrentPosition() <= b) {
				a = mp.getCurrentPosition();
				publishProgress(a, b);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					return -1;
				}
			}
			return 0;
		}

		/**
		 * Input la miliseconds
		 */
		@Override
		protected void onProgressUpdate(Integer... progress) {
			Integer cur = progress[0];
			Integer duration = progress[1];
			updateProgressBar(cur, duration);
		}

		@Override
		protected void onPostExecute(Integer result) {
			// pro_bar.setVisibility(View.INVISIBLE);
		}
	}
	
	private void pauseMediaPLayer() {
		if (mp == null) {
			return;
		}
		if (task != null && !task.isCancelled()) {
			task.cancel(true);
			task = null;
		}
		btn_play.setImageResource(R.drawable.play_bg_auto);
		btn_play.setTag("PLay");
		mListener.setPlayingState(false);
		try {
			if (mp.isPlaying()) {
				mp.pause();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param current
	 *            milisec
	 * @param duration
	 *            milisec
	 */
	private void updateProgressBar(Integer current, Integer duration) {
		if (pro_bar != null && songCurrentDurationLabel != null) {
			pro_bar.setProgress((int) (((double) current / duration) * 100));
			songCurrentDurationLabel
					.setText(utils.milliSecondsToTimer(current));
		}
	}
}
