package org.jingfu.freshmoviebrowser.imdb;


import org.jingfu.freshmoviebrowser.interfaces.IMDBResponseListener;
import org.jingfu.freshmoviebrowser.util.BitmapCache;
import org.json.JSONArray;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


public class IMDBClientFragment extends Fragment {
	private static final String TAG = "IMDBClientFragment";
	private static final String BASE_SEARCH_URL = "http://www.myapifilms.com/imdb";
	private static final String IN_THEATERS_REQUEST = "/inTheaters";
	private static final String OPENING_REQUEST = "/comingSoon";
	
	// Volley queue, cache, image loader
		private RequestQueue requestQueue = null;
		private ImageLoader	imageLoader = null;
		private BitmapCache	bitmapCache = null;

		private IMDBResponseListener imdbResponseListener;
	
	public IMDBClientFragment() {
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach()");
		super.onAttach(activity);
		try {
			imdbResponseListener = (IMDBResponseListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement TradeMeResponseListener");
		}
	}

	/**
	 * This method is called only once when the Fragment is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate(Bundle)");
		super.onCreate(savedInstanceState);

		// initialise the Volley queue and image loader
		requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
		bitmapCache = new BitmapCache();
		imageLoader = new ImageLoader(requestQueue, bitmapCache);

		Log.i(TAG, "onCreate(Bundle) : "+getActivity().hashCode());
		Log.i(TAG, "onCreate(Bundle) : "+getActivity().getApplicationContext().hashCode());
		// keep state across config changes (we don't lose the queue and loader)
		setRetainInstance(true);
	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public BitmapCache getBitmapCache() {
		return bitmapCache;
	}

	public void cancelAllRequests() {
		requestQueue.cancelAll(this);

	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop");
		super.onStop();
		cancelAllRequests();
	}
	
	public void getInTheatresMoviesList() {
		String requestURL = BASE_SEARCH_URL + IN_THEATERS_REQUEST;
		JsonArrayRequest request = new JsonArrayRequest(Method.GET, requestURL, null, new Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray jsonArray) {
				Log.d(TAG, "onResponse");
				imdbResponseListener.onInTheatresMovieListResponse(jsonArray);
			}
			
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, "getMovieList : onErrorResponse : " + error.getMessage());
				error.printStackTrace();
				imdbResponseListener.onError(error);
				
			}
			
		});
		request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
		requestQueue.add(request);
		Log.d(TAG, "add request to retrieve In Theatres Movies");
	}
	
	public void getOpenningMoviesList() {
		String requestURL = BASE_SEARCH_URL + OPENING_REQUEST;
		JsonArrayRequest request = new JsonArrayRequest(Method.GET, requestURL, null, new Listener<JSONArray>() {

			@Override
			public void onResponse(JSONArray jsonArray) {
				Log.d(TAG, "onResponse");
				imdbResponseListener.onOpeningMovieListResponse(jsonArray);
			}
			
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d(TAG, "getMovieList : onErrorResponse : " + error.getMessage());
				error.printStackTrace();
				imdbResponseListener.onError(error);
				
			}
			
		});
		request.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_MAX_RETRIES));
		requestQueue.add(request);
		Log.d(TAG, "add request to retrieve Opening Movies");
	}
	

}
