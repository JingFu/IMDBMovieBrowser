package org.jingfu.freshmoviebrowser.interfaces;

import org.json.JSONArray;

import com.android.volley.VolleyError;

public interface IMDBResponseListener {
	
	public void onInTheatresMovieListResponse(JSONArray jsonArray);
	
	public void onOpeningMovieListResponse(JSONArray jsonArray);
	
	public void onError(VolleyError error);

}
