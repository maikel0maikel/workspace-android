package com.qdcatplayer.main.GUI;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.qdcatplayer.main.R;

public class MainPlayerFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle saveInstanceState)
	{
		View v=inflater.inflate(R.layout.player_main, container,false);
		final ImageButton btn_play=(ImageButton)v.findViewById(R.id.imageButton_play);
		final ImageButton btn_shuffle=(ImageButton)v.findViewById(R.id.imageButton_shuffle);
		final ImageButton btn_repeat=(ImageButton)v.findViewById(R.id.imageButton_repeat);
		final ImageButton btn_fav=(ImageButton)v.findViewById(R.id.imageButton_favorite);
		btn_play.setTag("Play");
		btn_shuffle.setTag("UnSelected");
		btn_repeat.setTag("UnSelected");
		btn_fav.setTag("UnSelected");
		btn_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(btn_play.getTag().equals("Play")){
					btn_play.setImageResource(R.drawable.pause);
					btn_play.setTag("Pause");
				}
				else if(btn_play.getTag().equals("Pause")){
					btn_play.setImageResource(R.drawable.play_bg_auto);
					btn_play.setTag("Play");
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
		return v;
	}

}
