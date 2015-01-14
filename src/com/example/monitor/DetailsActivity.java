package com.example.monitor;

import com.example.datamodel.SumaryDataContainer;
import com.example.utils.Config;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailsActivity extends FragmentActivity {
	
	private GoogleMap map;
	protected GoogleApiClient mGoogleApiClient;
	private LatLng location;
	private TextView info;
	private SumaryDataContainer item;
	
    //Represents a geographical location.
    protected Location mCurrentLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		Intent intent = getIntent();
		item = (SumaryDataContainer) intent.getSerializableExtra("item");
		Log.i(Config.TAG,item.getTitle());
		
		// Get handles to the UI view objects
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		info = (TextView) findViewById(R.id.info_details);
		 
	}
	
	@Override
	public void onResume() {
		super.onResume();

		String data = item.getTitle()+"\n"+ "Magnitude :"+item.getMagnitude()+
					"\nDate :"+item.getTime()+ "\nLocation :"+ item.getLatitude()+ ", "+ item.getLongitude()+
					", "+item.getDepth();
		info.setText(data);
		location = new LatLng(item.getLatitude(),item.getLongitude());
		
		Marker locationDetails = map.addMarker(new MarkerOptions()
			.position(location)
			.snippet("Magnitude :" + String.valueOf(item.getMagnitude()))
			.title(item.getType()));
		
		Circle circle = map.addCircle(new CircleOptions()
		     .center(new LatLng(item.getLatitude(),item.getLongitude()))
		     .radius(5000)
		     .strokeColor(item.getColor())
		     .fillColor(item.getColor()));
		
		CameraUpdate center = CameraUpdateFactory.newLatLngZoom(location, 10);
		map.animateCamera(center);
		
		
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
