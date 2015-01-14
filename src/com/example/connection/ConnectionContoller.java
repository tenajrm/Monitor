package com.example.connection;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import android.app.Application;
import android.text.TextUtils;

public class ConnectionContoller extends Application {
	
	private RequestQueue requestQueue;
	private static ConnectionContoller _Instance;
	public String TAG = ConnectionContoller.class.getSimpleName();
	
	@Override
    public void onCreate() {
        super.onCreate();
        _Instance = this;
    }
	
	public static synchronized ConnectionContoller getInstance() {
        return _Instance;
    }
 
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
        	requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
 
        return requestQueue;
    }
    
    public <T> void addToRequestQueue(Request<T> req, String tag) {
		//Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
    	req.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    	// set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
 
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }
 
    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
        	requestQueue.cancelAll(tag);
        }
    }
}
