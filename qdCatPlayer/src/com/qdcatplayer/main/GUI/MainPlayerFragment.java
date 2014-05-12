package com.qdcatplayer.main.GUI;

import android.app.Activity;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.Entities.MySong;
import com.qdcatplayer.main.Libraries.PlayerUtilities;

public abstract class MainPlayerFragment extends Fragment {
	public interface MyMainPLayerDataProvider {
		public MySong getCurrentSong();

		public Integer getPLayedCount();

		public Integer getTotalCount();

		public MediaPlayer getMediaPlayer();

		public Boolean prepareMediaPlayer(MediaPlayer player, MySong obj);

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

		public Boolean getShuffle();

		public Integer getRepeat();
	}

	private class ShowProgressTask extends AsyncTask<Void, Integer, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			if (mp == null) {
				return -1;
			}
			Integer b = mp.getDuration();
			Integer a = 0;
			// TODO Auto-generated method stub
			while (mp.getCurrentPosition() <= b) {
				if (isCancelled()) {
					break;// dead code but still here for sure
				}
				a = mp.getCurrentPosition();
				publishProgress(a, b);
				if (!mp.isPlaying())// importance
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
			// super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// super.onPreExecute();
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

	/*
	 * View instances
	 */
	private TextView artistLabel;
	private TextView songIndex;
	private ImageButton btn_fav;
	private ImageButton btn_play;
	private ImageButton btn_next;
	private ImageButton btn_prev;
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

	/**
	 * Update textView, SongName, Artist Name,... (get data from provider)
	 */
	private void updateCurrentSongInfoToView() {
		if (dataProvider.getCurrentSong() == null) {
			return;
		}
		// update total time label
		songTotalDurationLabel.setText(PlayerUtilities
				.milliSecondsToTimer(dataProvider.getCurrentSong()
						.getDuration()));
		// update song name label
		songNameLabel.setText(dataProvider.getCurrentSong().getTitle());
		// update artist label
		artistLabel
				.setText(dataProvider.getCurrentSong().getArtist().getName());
		songIndex.setText(dataProvider.getPLayedCount() + "/"
				+ dataProvider.getTotalCount());
	}

	/**
	 * Goi khi khoi tao View cho Fragment, su dung khi chuyen doi giua cac
	 * Fragment Duoc goi sau khi da co duoc cac View Instances
	 */
	private void loadSavedState() {
		// Step 1:load song info
		mp = dataProvider.getMediaPlayer();// very importance
		if (mp == null) {
			return;// fail here
		}

		updateCurrentSongInfoToView();
		// Step2: load current seekbar position (update immedialy if isPlaying)
		// create new instance of Propressbar Asynctask
		if(dataProvider.getCurrentSong()!=null)
		{
			task = initNewProgressTask(task);
			task.execute();
		}
		// Step 3:load current pause/play
		// load PLay/pause state
		try {
			setPlayButton(btn_play, !mp.isPlaying());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// load shuffle state
		setShuffleButton(btn_shuffle, dataProvider.getShuffle());
		// load repeat state
		setRepeatButton(btn_repeat, dataProvider.getRepeat());

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
		btn_next = (ImageButton) v.findViewById(R.id.imageButton_next);
		btn_prev = (ImageButton) v.findViewById(R.id.imageButton_prev);
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
		songIndex = (TextView) v.findViewById(R.id.textView_song_index);

		// set View tag and default values
		btn_fav.setTag("UnSelected");
		pro_bar.setMax(100);
		// load saved state
		loadSavedState();// fisrt after have view Instances
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
		btn_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				nextAndPlayMediaPlayer();
			}
		});
		btn_prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				prevAndPlayMediaPlayer();
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
				dataProvider
						.setRepeat(switchRepeatButtonNextState((ImageButton) v));
			}
		});
		btn_shuffle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dataProvider.setShuffle(setShuffleButton((ImageButton) v,
						!dataProvider.getShuffle()));
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

		// set listener for finish player
		mp.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				nextAndPlayMediaPlayer();
			}
		});

		// done
		return v;

	}

	/**
	 * Hỗ trợ set Default khi v chưa có tag
	 * 
	 * @param v
	 * @return RepeateState dung cho gan nguoc lại dataProvider
	 */
	protected Integer switchRepeatButtonNextState(ImageButton v) {
		String tag;
		if (v.getTag() == null) {
			v.setTag("SelectedAll");
		}
		tag = v.getTag().toString();

		if (tag.equals("UnSelected")) {
			return setRepeatButton(v, 1);
		} else if (tag.equals("SelectedOne")) {
			return setRepeatButton(v, 2);
		} else if (tag.equals("SelectedAll")) {
			return setRepeatButton(v, 0);
		}
		return 0;
	}

	/**
	 * 
	 * @param button
	 * @param mode
	 *            = {0: "UnSelected", 1: "One", 2: "All"}
	 * @return Dùng để gán lại cho dataProvider thông qua hàm
	 */
	private Integer setRepeatButton(ImageButton button, Integer mode) {
		if (mode == 0) {
			button.setImageResource(R.drawable.repeat_none_auto);
			button.setTag("UnSelected");
		} else if (mode == 1) {
			button.setImageResource(R.drawable.repeat_selected_one_auto);
			button.setTag("SelectedOne");
		} else if (mode == 2) {
			button.setImageResource(R.drawable.repeat_selected_all_auto);
			button.setTag("SelectedAll");
		}
		return mode;
	}

	/**
	 * 
	 * @param button
	 * @param mode
	 *            = {true: "Play", false: "Pause"}
	 */
	private void setPlayButton(ImageButton button, Boolean mode) {
		if (mode) {
			btn_play.setImageResource(R.drawable.play_bg_auto);
			btn_play.setTag("PLay");
		} else {
			btn_play.setImageResource(R.drawable.pause_bg_auto);
			btn_play.setTag("Pause");
		}
	}

	/**
	 * 
	 * @param button
	 * @param mode
	 *            = {false: "UnSelected", true: "Selected"}
	 * @return Dùng để gán lại cho dataProvider.SetShuffle(...)
	 */
	private Boolean setShuffleButton(ImageButton button, Boolean mode) {
		if (mode) {
			btn_shuffle.setImageResource(R.drawable.shuffle_selected_auto);
			btn_shuffle.setTag("Selected");
		} else {
			btn_shuffle.setImageResource(R.drawable.shuffle_none_auto);
			btn_shuffle.setTag("UnSelected");
		}
		return mode;
	}

	private void nextAndPlayMediaPlayer() {
		if (mp == null) {
			return;
		}
		destroyProgressTask(task);
		// prepare next song

		if (dataProvider.requestNextSong()) {
			// call play
			playMediaPLayer();
			// call update view
			updateCurrentSongInfoToView();
		}
	}

	private void prevAndPlayMediaPlayer() {
		if (mp == null) {
			return;
		}
		destroyProgressTask(task);
		// prepare prev song

		if (dataProvider.requestPrevSong()) {
			// call play
			playMediaPLayer();
			// call update view
			updateCurrentSongInfoToView();
		}
	}

	private void pauseMediaPLayer() {
		if (mp == null || dataProvider.getCurrentSong() == null) {
			return;
		}
		destroyProgressTask(task);
		try {
			if (mp.isPlaying()) {
				mp.pause();
				setPlayButton(btn_play, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void playMediaPLayer() {
		if (mp == null || dataProvider.getCurrentSong() == null) {
			return;
		}
		try {
			mp.start();
			/*
			 * update view
			 */
			setPlayButton(btn_play, false);
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
		if (mp == null || dataProvider.getCurrentSong()==null) {
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
			songCurrentDurationLabel.setText(PlayerUtilities
					.milliSecondsToTimer(current));
		}
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		destroyProgressTask(task);// not trigged
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		destroyProgressTask(task);// not trigged
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		destroyProgressTask(task);// very importance
	}

}
