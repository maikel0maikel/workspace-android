package com.example.hour12app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Song_Info
{
	String link="";
	String name="";
	public static ArrayList<Song_Info> JSON_To_Array(String json)
	{
		ArrayList<Song_Info> re=new ArrayList<Song_Info>();
		Song_Info tmp;
		JSONObject jobj;
		try {
			JSONObject obj=new JSONObject(json);
			JSONArray list=obj.optJSONArray("data");
			
			for(int i=0;i<list.length();i++)
			{
				jobj = list.getJSONObject(i);
				tmp=new Song_Info();
				
				tmp.link = jobj.getString("link");
				tmp.name = jobj.getString("name");
				re.add(tmp);
			}
			return re;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return re;
	}
}
