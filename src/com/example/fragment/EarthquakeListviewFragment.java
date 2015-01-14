package com.example.fragment;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.datamodel.AdapterData;
import com.example.datamodel.SumaryDataContainer;
import com.example.monitor.R;
import com.example.utils.Config;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class EarthquakeListviewFragment extends Fragment{
	
	private ListView earthquakeList;
	private TextView noItems;
	
	
	public EarthquakeListviewFragment newInstance(ArrayList<String> homes) {
		EarthquakeListviewFragment f = new EarthquakeListviewFragment();
		//in case we have some arguments
		Bundle b = new Bundle();
		f.setRetainInstance(true);
		f.setArguments(b);
		return f;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_act_earthquake ,container,
				false);
		earthquakeList = (ListView) view.findViewById(R.id.sumary_earthquake_list);
		noItems = (TextView) view.findViewById(R.id.sumary_no_item);
		setformatList();
		
		return view;
	}
	
	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);
		
	}

	// Listener
	public interface setOnListViewListener {
		public void onListViewClickListener(Fragment listView, SumaryDataContainer item, View view);
	}
	
	public setOnListViewListener listViewListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listViewListener = (setOnListViewListener) activity;
			
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnArticleSelectedListener");
		}
	}
	
	private void setformatList() {
		earthquakeList.setCacheColorHint(Color.WHITE);
		earthquakeList.setBackgroundColor(Color.WHITE);
		earthquakeList.setScrollbarFadingEnabled(false);
		earthquakeList.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		earthquakeList.setAdapter(null);
	}
	
	public void setList(JSONObject eathquakeSumary) {
		
		// convert into SumaryDataContainer object
		if(!eathquakeSumary.has("features")){
			Log.i(Config.TAG, "there is not items");
			noItems.setVisibility(View.VISIBLE);
			return;
		}
		
		JSONArray eathquakeItems  = new JSONArray();
		
		try {
			eathquakeItems = (JSONArray) eathquakeSumary.get("features");
			Log.i(Config.TAG, "there is  items"+eathquakeItems.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<SumaryDataContainer> list = new ArrayList<SumaryDataContainer>();
		if(eathquakeItems.length() > 0){
			for (int i = 0; i < eathquakeItems.length(); i++) {
				
			    JSONObject row = new JSONObject();
			    JSONObject properties = new JSONObject();
			    JSONObject geometry = new JSONObject();
			    JSONArray coordinates = new JSONArray();
			    SumaryDataContainer dataItem = new SumaryDataContainer();

				try {
					row = eathquakeItems.getJSONObject(i);
					properties = row.getJSONObject("properties");
					geometry = row.getJSONObject("geometry");
					coordinates = geometry.getJSONArray("coordinates");
					dataItem.setTitle(properties.getString("title"));
					dataItem.setPlace(properties.getString("place"));
					dataItem.setLongitude(coordinates.getDouble(0));
					dataItem.setLatitude(coordinates.getDouble(1));
					dataItem.setDepth(coordinates.getDouble(2));
					dataItem.setType(properties.getString("type"));
					
					double mag = properties.getDouble("mag");
					//set color according magnitude
					if(mag >= 0 && mag < 0.9){
						//yellow color
						dataItem.setColor(getResources().getColor(R.color.yellow));
					} else if(mag >= 0.9 && mag < 9){
						//green color
						dataItem.setColor(getResources().getColor(R.color.green));
					} else if(mag >= 9 && mag <= 9.9){
						//red color
						dataItem.setColor(getResources().getColor(R.color.red));
					}
					dataItem.setMagnitude(mag);
					
					//Conver long to date format 
					long time = properties.getLong("time"); 
					Date date = new Date(time);
					Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
					dataItem.setTime(format.format(date));
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				list.add(dataItem);
			}
		}
		else {
			noItems.setVisibility(View.VISIBLE);
		}
		
		AdapterData adapter = new AdapterData(getActivity(), R.layout.adapter_list_item, list);
		earthquakeList.setAdapter(adapter);
		earthquakeList.setOnItemClickListener(new OnClickListViewListener());
		
		
	}
	
	public class OnClickListViewListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			
			SumaryDataContainer item = (SumaryDataContainer) parent
					.getItemAtPosition(pos);
			listViewListener.onListViewClickListener(EarthquakeListviewFragment.this, item, view);

		}

	}
	
	
	
	
	
	
}
