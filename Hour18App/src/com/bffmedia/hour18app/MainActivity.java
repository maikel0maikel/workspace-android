package com.bffmedia.hour18app;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	TextView mTV;
	Button myLocation;
	LocationManager lManager;
	Location lastLocation;
	Location alertLocation;
	private static final int proximityRadius=1;
	
	private static final int minTime = 0;//milisec
	private static final int minDistance = 0;//meter
	//Location listener
	LocationListener qdListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location arg0) {
			// TODO Auto-generated method stub
			lastLocation = new Location(arg0);
			String tmp="";
			tmp+="Latitute: "+arg0.getLatitude()+"\r\n";
			tmp+="Longtitute: "+arg0.getLongitude()+"\r\n";
			tmp+="Altitute: "+arg0.getAltitude()+"\r\n";
			tmp+="Speed: "+arg0.getSpeed()+"m/s"+"\r\n";
			tmp+="Accuracy: "+arg0.getAccuracy()+"m"+"\r\n";
			tmp+="Time: "+getDate(arg0.getTime(),"dd/MM/yyyy hh:mm:ss.SSS")+" (dd/MM/yyyy hh:mm:ss.SSS)"+"\r\n";
			
			//get address from location
			Geocoder gCoder = new Geocoder(getApplicationContext());
			try {
				List<Address> addresses = gCoder.getFromLocation(arg0.getLatitude(), arg0.getLongitude(), 1);
				tmp+="Address: ";
				for(Address addr:addresses)
				{
					for(int i=0;i<=4;i++)
					{
						tmp+=String.valueOf(addr.getAddressLine(i));
						if(i<4)
						{
							tmp+=", ";
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//final action
			mTV.setText(tmp);
		}
	};
	PendingIntent proximityIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//get view instances
		mTV = (TextView) findViewById(R.id.displayTextView);
		myLocation = (Button) findViewById(R.id.button_setmylocation);
		//set view litenner
		myLocation.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertLocation = new Location(lastLocation);
				Intent intent = new Intent(getApplicationContext(),AlertActivity.class);
				Bundle data = new Bundle();
				data.putString("message", "OK");
		        if(proximityIntent!=null)
		        {
		        	lManager.removeProximityAlert(proximityIntent);
		        }
		        proximityIntent =
		        		PendingIntent.getActivity(
		        				getApplicationContext(),
		        				-1,
		        				intent,PendingIntent.FLAG_ONE_SHOT
		        				);

				//add proximity alert
				lManager.addProximityAlert(
						alertLocation.getLatitude(),
						alertLocation.getLongitude(),
						proximityRadius,
						-1,
						proximityIntent);
			}
		});
		//get location interval 5 minutes with accuracy under 100 meter 
		lManager =
				(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		lManager.requestLocationUpdates(getProvider(), minTime, minDistance, qdListener);
		
		
		
		/*
		
		mLocationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Location lastGPSLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		logLocation(lastGPSLocation);
		Location lastLocation =  getRecentLocation(); 
		logLocation(lastLocation);
		// Use provider from lastLocation to track changes
		mLocationManager.requestLocationUpdates(lastLocation.getProvider(), 0, 0, locationListener);		
		//Use GPS_PROVIDER
//		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//		}
		//Use Criteria from getProvider() method
//		mLocationManager.requestLocationUpdates(getProvider(), 0, 0, locationListener);

		
		
		Geocoder coder = new Geocoder(getApplicationContext());
		String locationToFind = "White Plains, NY";
		List<Address> geocodeResults;
		try {
			geocodeResults = coder.getFromLocationName(locationToFind, 3);
			for (Address address: geocodeResults){
				Log.d("Location", locationToFind + " " + address.getLatitude() +"," + address.getLongitude());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

	}
	
	/*protected void onStop() {
	    super.onStop();
	    mLocationManager.removeUpdates(locationListener);
	}*/
	
	/*LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			Geocoder coder = new Geocoder(getApplicationContext());
			List<Address> geocodeResults;
			try {
				geocodeResults = coder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				for (Address address: geocodeResults){
					Log.d("Location", location.getLatitude() +"," + location.getLongitude() +" : " +address.getLocality());
					mTV.setText(location.getLatitude() +"," + location.getLongitude()+" : " +address.getLocality());
				}
				String geoURI = "geo:" + location.getLatitude() +"," + location.getLongitude() ;
				Uri geo = Uri.parse(geoURI);
				Intent geoMap = new Intent(Intent.ACTION_VIEW, geo);
				startActivity(geoMap);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void onProviderDisabled(String provider) {}
		@Override
		public void onProviderEnabled(String provider) {}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};*/
	
	public String getProvider(){
		//return LocationManager.NETWORK_PROVIDER;
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
		criteria.setSpeedRequired(false);
		String providerName =  lManager.getBestProvider(criteria, true);//Fail
		if (providerName != null) {
			return providerName;
		}else{
			return LocationManager.GPS_PROVIDER;
		}
	}
	/*
	public Location getRecentLocation() {
	    Location recentLocation = null;
	    long bestTime = 0;
	    List<String> matchingProviders = mLocationManager.getAllProviders();
	    for (String provider: matchingProviders) {
	      Location location = mLocationManager.getLastKnownLocation(provider);
	      if (location != null) {
	        long time = location.getTime();
	        if (time > bestTime) {
	          bestTime = time;
	          recentLocation = location;
	        }
	      }
	    }
	    return recentLocation;
	}
	*/
	/**
	 * Return date in specified format.
	 * @param milliSeconds Date in milliseconds
	 * @param dateFormat Date format 
	 * @return String representing date in specified format
	 */
	public static String getDate(long milliSeconds, String dateFormat)
	{
	    // Create a DateFormatter object for displaying date in specified format.
	    DateFormat formatter = new SimpleDateFormat(dateFormat);

	    // Create a calendar object that will convert the date and time value in milliseconds to date. 
	     Calendar calendar = Calendar.getInstance();
	     calendar.setTimeInMillis(milliSeconds);
	     return formatter.format(calendar.getTime());
	}
	
}
