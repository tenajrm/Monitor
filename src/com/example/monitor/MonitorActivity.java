package com.example.monitor;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.connection.ConnectionContoller;
import com.example.datamodel.SumaryDataContainer;
import com.example.fragment.EarthquakeListviewFragment;
import com.example.utils.Config;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MonitorActivity extends FragmentActivity implements 
	EarthquakeListviewFragment.setOnListViewListener{
	
	private final String TAG_REQUEST = "MyRequest";	
	private EarthquakeListviewFragment earthquakeListFragment;
	private SwipeRefreshLayout swipeContainer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_monitor);
		earthquakeListFragment = (EarthquakeListviewFragment) getSupportFragmentManager()
				.findFragmentById(R.id.earthquake_frag);
		this.sumaryEarthquakeHttpRequest();
		
		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		
		swipeContainer.setOnRefreshListener(new OnRefreshListListener());
		swipeContainer.setColorSchemeResources(R.color.red, 
	                R.color.yellow, 
	                R.color.red, 
	                R.color.yellow);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		earthquakeListFragment = new EarthquakeListviewFragment();
		ft.add(R.id.earthquake_frag, earthquakeListFragment,
				"Earthquake_List_Fragment");
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
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void sumaryEarthquakeHttpRequest() {
		final RequestQueue queue = ConnectionContoller.getInstance().getRequestQueue();
		
		JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                Config.URL, null,
                new Response.Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject response) {
				try {
					Log.i(Config.TAG, response.toString());
					earthquakeListFragment.setList(response);
					
				} catch (Exception e) {
					e.printStackTrace();
					Log.i(TAG_REQUEST, "Error Exception");
				}
				
			}
		}, new Response.ErrorListener() {

			@Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG_REQUEST, "ErrorListener: " + error.getMessage()); 
                if(queue.getCache().get(Config.URL)!=null){
                	String cachedResponse = new String(queue.getCache().get(Config.URL).data);
                	Log.i(TAG_REQUEST, "Cache :"+cachedResponse);
                	try {
						JSONObject cache = new JSONObject(cachedResponse);
						earthquakeListFragment.setList(cache);
					} catch (JSONException e) {
						
						e.printStackTrace();
					}
                	
                }
                
            }
		});
		
		ConnectionContoller.getInstance().addToRequestQueue(jsonObjReq, TAG_REQUEST);
	}

	@Override
	public void onListViewClickListener(Fragment listView, SumaryDataContainer item, View view) {
		
		Log.i(Config.TAG, item.getTitle());
		Intent intent = new Intent(this, DetailsActivity.class);
		intent.putExtra("item", item);
		startActivity(intent);
		
	}

	public class OnRefreshListListener implements OnRefreshListener {

		@Override
		public void onRefresh() {
			swipeContainer.setRefreshing(false);
			sumaryEarthquakeHttpRequest();
		}
			
	}

}
