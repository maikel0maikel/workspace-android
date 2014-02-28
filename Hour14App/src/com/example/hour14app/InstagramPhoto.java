package com.example.hour14app;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InstagramPhoto extends Object{
	String id="";
	String owner="";
	String title="";
	String img_thumb_url="";
	Integer is_favorite=0;

	public InstagramPhoto(){	
		
	}
	public static InstagramPhoto Load_From_JSON(JSONObject img_json_obj)
	{
		JSONObject tmp = img_json_obj;
		if(tmp==null) return null;
		
		JSONObject img_tmp=null;
		try {
			img_tmp = tmp.getJSONObject("images");
		} catch (JSONException e) {
			img_tmp=null;
		}
		
		JSONObject thumb_img_tmp=null;
		if(img_tmp!=null)
		{
			try {
				thumb_img_tmp = img_tmp.getJSONObject("thumbnail");
			} catch (JSONException e) {
				thumb_img_tmp=null;
			}
		}
		
		
		JSONObject caption_tmp=null;
		try {
			caption_tmp = tmp.getJSONObject("caption");
		} catch (JSONException e) {
			caption_tmp=null;
		}
		
		JSONObject user_tmp=null;
		try {
			user_tmp = tmp.getJSONObject("user");
		} catch (JSONException e) {
			user_tmp=null;
		}
		
		InstagramPhoto currentPhoto = new InstagramPhoto ();
		
		if(user_tmp!=null)
			try {
				currentPhoto.owner=user_tmp.getString("full_name");
			} catch (JSONException e) {
				currentPhoto.owner="";
			}
		if(thumb_img_tmp!=null)
			try {
				currentPhoto.img_thumb_url=thumb_img_tmp.getString("url");
			} catch (JSONException e) {
				currentPhoto.img_thumb_url="";
			}
		if(caption_tmp!=null)
			try {
				currentPhoto.title=caption_tmp.getString("text");
			} catch (JSONException e) {
				currentPhoto.title="";
			}
		if(tmp!=null)
			try {
				currentPhoto.id=tmp.getString("id");
			} catch (JSONException e) {
				currentPhoto.id="";
			}
		
		return currentPhoto;
	}
	//for API: https://api.instagram.com/v1/media/popular
	public static ArrayList <InstagramPhoto> Load_From_RAW (String photoData_raw_json) {
		ArrayList <InstagramPhoto> instagramPhotos = new ArrayList<InstagramPhoto>();		
		JSONObject root_json;
		JSONArray img_json_array;
		try {
			root_json = new JSONObject(photoData_raw_json);
			img_json_array = root_json.optJSONArray("data");
			for(int i = 0; i < img_json_array.length(); i++) {
				JSONObject tmp =	(JSONObject) img_json_array.get(i);
				
				if(!tmp.getString("type").equals("image")) continue;//only image
				
				InstagramPhoto currentPhoto = InstagramPhoto.Load_From_JSON(tmp);
				
				if(currentPhoto!=null)
				{
					instagramPhotos.add(currentPhoto);
				}
			}
			return instagramPhotos;
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instagramPhotos;
		
	}
}
