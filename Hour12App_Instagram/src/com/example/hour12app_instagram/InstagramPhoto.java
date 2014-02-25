package com.example.hour12app_instagram;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class InstagramPhoto extends Object{
	String id="";
	String owner="";
	String title="";
	String img_thumb_url="";

	public InstagramPhoto(){	
		
	}
	public static InstagramPhoto Load_From_JSON(JSONObject img_json_obj)
	{
		JSONObject tmp = img_json_obj;
		try {
			JSONObject img_tmp = tmp.getJSONObject("images");
			JSONObject thumb_img_tmp = img_tmp.getJSONObject("thumbnail");
			JSONObject caption_tmp = tmp.getJSONObject("caption");
			JSONObject user_tmp = tmp.getJSONObject("user");
			
			InstagramPhoto currentPhoto = new InstagramPhoto ();
			if(user_tmp!=null)
				currentPhoto.owner=user_tmp.getString("full_name");
			if(thumb_img_tmp!=null)
				currentPhoto.img_thumb_url=thumb_img_tmp.getString("url");
			if(caption_tmp!=null)
				currentPhoto.title=caption_tmp.getString("text");
			
			return currentPhoto;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//only image
		return null;
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
