package com.qdcatplayer.main.Setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.qdcatplayer.main.R;
import com.qdcatplayer.main.R.id;
import com.qdcatplayer.main.R.layout;
import com.qdcatplayer.main.dummy.DummyContent;

/**
 * A fragment representing a single Setting detail screen. This fragment is
 * either contained in a {@link SettingListActivity} in two-pane mode (on
 * tablets) or a {@link SettingDetailActivity} on handsets.
 */
public class SettingDetailFragment extends Fragment {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The dummy content this fragment is presenting.
	 */
	private DummyContent.DummyItem mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SettingDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			mItem = DummyContent.ITEM_MAP.get(getArguments().getString(
					ARG_ITEM_ID));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_setting_detail,
				container, false);
		
		// Show the dummy content as text in a TextView.
		if (mItem != null) {
			if(mItem.id.equals("1"))
			{
				((TextView) rootView.findViewById(R.id.setting_detail))
				.setText(mItem.content);
			}
			else
			{
				rootView = inflater.inflate(R.layout.fragment_setting_detail_2,
						container, false);
				//set on item change listenner
				CheckBox k = (CheckBox)rootView.findViewById(R.id.checkBox1);
				k.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
						Toast.makeText(getActivity().getApplicationContext(), "Olla", 1000).show();
					}
				});
			}
			return rootView;
		}
		return rootView;
	}
}
