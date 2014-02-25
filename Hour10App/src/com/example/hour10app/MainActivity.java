package com.example.hour10app;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity implements QdListViewItemClickListener {
	Tab mTab1;
	Tab mTab2;
	private String qdListString[] = {"Show ListView on SecondTab", "Show GridView on SecondTab"};
	private String qdSecondString[] = {"Xoài", "Cốc" ,"Mận","Ổi", "Vú sữa"};
	int selected_layout = R.layout.list_view_fr;//by default
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//create Tab
		ActionBar actionBar = getActionBar();		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mTab1= actionBar.newTab().setText("ControlTab").setTabListener(new NavTabListener());
		mTab2= actionBar.newTab().setText("SecondTab").setTabListener(new NavTabListener());
		actionBar.addTab(mTab1);
		actionBar.addTab(mTab2);
		
		//default layout
		DisplayListView(qdListString);
	}
	
	private void DisplayListView(String[] input)//call to update Fragment
	{
		//replace fragment
		QdListViewFr fr=new QdListViewFr();
		//set question
		Bundle data=new Bundle();
		data.putStringArray("data", qdListString);
		fr.setArguments(data);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.place_holder, fr);
		ft.addToBackStack("example");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void OnListViewItemClick(int d) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "Chọn layout có giá trị là: "+d , 2000).show();
		selected_layout = d;
		//auto selected secondtab
		//emulator tab selected
		ActionBar actionBar = getActionBar();
		actionBar.setSelectedNavigationItem(1);
		showDataOnSecondTab(selected_layout);
	}
	private void showDataOnSecondTab(int layout_int)
	{
		//replace fragment
		QdSecondFr fr=new QdSecondFr();
		//set question
		Bundle data=new Bundle();
		data.putStringArray("data", qdSecondString);
		data.putInt("layout_int", layout_int);
		fr.setArguments(data);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.place_holder, fr);
		ft.addToBackStack("example");
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
	}
	
	private class NavTabListener implements ActionBar.TabListener {
		public NavTabListener() {
		}
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			if (tab.equals(mTab1)){
				DisplayListView(qdListString);
			}else if (tab.equals (mTab2)){
				showDataOnSecondTab(selected_layout);
			}
		}
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}
	}

}
