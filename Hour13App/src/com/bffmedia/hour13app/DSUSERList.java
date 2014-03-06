package com.bffmedia.hour13app;

import java.io.IOException;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
public class DSUSERList extends ListFragment {
	String []msource;
	ArrayList<User_Obj> users = new ArrayList<User_Obj>();
	@Override
	public void onActivityCreated(Bundle savedInstanceState)  {
		super.onActivityCreated(savedInstanceState);
		Resources resource=getResources();
		//msource=resource.getStringArray(R.array.user);
		//qd
		QdDBHelper kkk=new QdDBHelper(getActivity().getApplicationContext());
		kkk.open();
		users = kkk.get_all_obj();
		kkk.close();
		msource = new String[users.size()];
		for(int ii=0;ii<users.size();ii++)
		{
			msource[ii] = users.get(ii).username;
		}
		setListAdapter(new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1,msource));
	}

	@Override
	public void onListItemClick(ListView l,View v,int position,long id){
		Intent tmp=new Intent(getActivity().getApplicationContext(),User_Detail_Activity.class);
		tmp.putExtra("username", users.get(position).username);
		startActivity(tmp);
		
	}

}
