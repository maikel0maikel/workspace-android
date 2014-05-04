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
import android.support.v4.app.TaskStackBuilder;
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
		public MySong getCurrentSong();

		public MediaPlayer getMediaPlayer();

		/**
		 * bai hat se khong duoc phat cho toi khi dieu khien MediaPlayer
		 * 
		 * @return
		 */
		public Boolean requestNextSong();

		/**
		 * bai hat se khong duoc phat cho toi khi dieu khien MediaPlayer
		 * 
		 * @return
		 */
		public Boolean requestPrevSong();

		/**
		 * 
		 * @param mode
		 *            0:khong repeat, 1: repeat 1 bai, 2: repeat toan bo
		 * @return
		 */
		public Boolean setRepeat(Integer mode);

		/**
		 * 
		 * @param mode
		 *            true: on, false: off
		 * @return
		 */
		public Boolean setShuffle(Boolean mode);
	}

	private class ShowProgressTask extends AsyncTask<Void, Integer, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			if(mp==null)
			{
				return -1;
			}
			Integer b = mp.getDuration();
			Integer a = 0;
			// TODO Auto-generated method stub
			while (mp.getCurrentPosition() <= b) {
				if(isCancelled())
				{
					break;//dead code but still here for sure
				}
				a = mp.getCurrentPosition();
				publishProgress(a, b);
				if(!mp.isPlaying())//importance
				{
					break;
				}
				
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					return -1;
				}
				
			}
			return 0;
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			//super.onPostExecute(result);
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			//super.onPreExecute();
		}
		
		/**
		 * Input la miliseconds
		 */
		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			Integer cur = progress[0];
			Integer duration = progress[1];
			updateProgressBar(cur, duration);
		}
	}
	private TextView artistLabel;
	private ImageButton btn_fav;
	private ImageButton btn_play;
	private ImageButton btn_repeat;
	private ImageButton btn_shuffle;
	/*
	 * Listener to Activity
	 */
	private MyMainPLayerDataProvider dataProvider = null;
	private MediaPlayer mp;
	private SeekBar pro_bar;
	private TextView songCurrentDurationLabel;
	private TextView songNameLabel;
	private TextView songTotalDurationLabel;
	/*
	 * 
	 */
	private ShowProgressTask task = null;

	/*
	 * public void updateProgressBar() { mHandler.postDelayed(mUpdateTimeTask,
	 * 100); }
	 */

	/*
	 * View instances
	 */
	// private Handler mHandler = new Handler();
	private Utilities utils = new Utilities();

	/*
	 * private Runnable mUpdateTimeTask = new Runnable() { public void run() {
	 * long totalDuration = mp.getDuration(); long currentDuration =
	 * mp.getCurrentPosition(); // Displaying Total Duration time
	 * songTotalDurationLabel.setText("" +
	 * utils.milliSecondsToTimer(totalDuration)); // Displaying time completed
	 * playing songCurrentDurationLabel.setText("" +
	 * utils.milliSecondsToTimer(currentDuration)); // Updating progress bar int
	 * progress = (int) (utils.getProgressPercentage(currentDuration,
	 * totalDuration)); // int
	 * progress=(int)(((double)currentDuration/totalDuration)*100); //
	 * Log.d("Progress", ""+progress); pro_bar.setProgress(progress); // Running
	 * this thread after 100 milliseconds mHandler.postDelayed(this, 100); } };
	 */

	private void destroyProgressTask(ShowProgressTask input) {
		if (input != null) {
			input.cancel(false);
		}
	}

	/**
	 * Cancel task hien tai, va tao task moi, nhung khong chay lien
	 * 
	 * @param input
	 */
	private ShowProgressTask initNewProgressTask(ShowProgressTask input) {
		destroyProgressTask(input);
		return new ShowProgressTask();
	}

	private void loadSavedState() {
		// Step 1:load song info
		// update total time label
		songTotalDurationLabel.setText(utils.milliSecondsToTimer(dataProvider
				.getCurrentSong().getDuration() * 1000));
		// update song name label
		songNameLabel.setText(dataProvider.getCurrentSong().getTitle());
		// update artist label
		artistLabel
				.setText(dataProvider.getCurrentSong().getArtist().getName());
		// Step2: load current seekbar position (update immedialy if isPlaying)
		mp = dataProvider.getMediaPlayer();
		//create new instance of Propressbar Asynctask
		task = initNewProgressTask(task);
		task.execute();
		// Step 3:load current pause/play
		if (mp == null) {
			return;
		}
		try {
			if (mp.isPlaying()) {
				btn_play.setImageResource(R.drawable.pause);
				btn_play.setTag("Pause");
			} else {
				btn_play.setImageResource(R.drawable.play_bg_auto);
				btn_play.setTag("PLay");
			}
		} catch (IllegalStateException e) {
			btn_play.setImageResource(R.drawable.play_bg_auto);
			btn_play.setTag("PLay");
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		dataProvider = (MyMainPLayerDataProvider) activity;
	}

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
		// load saved state
		loadSavedState();

		// btn_play.setTag("Play");
		// btn_shuffle.setTag("UnSelected");
		// btn_repeat.setTag("UnSelected");
		// btn_fav.setTag("UnSelected");
		pro_bar.setMax(100);

		// set listener for view
		btn_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Boolean isPlaying = mp.isPlaying();
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
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				// mHandler.removeCallbacks(mUpdateTimeTask);
			}

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
		});
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {

			}
		});
		
		// done
		return v;

	}

	private void pauseMediaPLayer() {
		if (mp == null) {
			return;
		}
		destroyProgressTask(task);
		btn_play.setImageResource(R.drawable.play_bg_auto);
		btn_play.setTag("PLay");
		try {
			if (mp.isPlaying()) {
				mp.pause();
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	private void playMediaPLayer() {
		if (mp == null) {
			return;
		}
		try {
			mp.start();

			/*
			 * update view
			 */
			btn_play.setImageResource(R.drawable.pause);
			// set Tag
			btn_play.setTag("Pause");
			// kim: Su dung cach code mau
			// pro_bar.setProgress(0);
			// updateProgressBar();
			// kim: Su dung asynctask
			task = initNewProgressTask(task);
			task.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		// forward or backward to certain seconds
		mp.seekTo(currentPosition);
		// update timer progress again
		// kim: su dung theo code mau
		// updateProgressBar();
		// kim: su dung async task
		task = initNewProgressTask(task);
		task.execute();
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
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		destroyProgressTask(task);
	}
}
