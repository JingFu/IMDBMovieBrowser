package org.jingfu.freshmoviebrowser.imdb;

import java.util.ArrayList;

import org.jingfu.freshmoviebrowser.model.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	private static final String	TAG	= "JSONParser";
	private static final String IN_THEATRES_TYPE = "In Theaters Now"; 

	public static Movie parseMovieJSON(JSONObject jsonMovie) {

		Movie movie = new Movie();
		movie.setTitle(jsonMovie.optString("title"));
		movie.setMpaaRating(jsonMovie.optString("rated"));
		movie.setId(jsonMovie.optString("idIMDB"));
		movie.setSynopsis(jsonMovie.optString("simplePlot")); 
		movie.setRating((5.0 * jsonMovie.optDouble("rating")) / 10.0);
		movie.setImdbURL(jsonMovie.optString("urlIMDB"));
		movie.setThumbURL(jsonMovie.optString("urlPoster"));

		return movie;
	}
	
	public static ArrayList<Movie> parseInTheatresMovieListJSON(JSONArray jsonArray) {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		JSONArray movieList = null;
		try {
			Log.d(TAG, jsonArray.toString(2));
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject movieInfo = jsonArray.getJSONObject(i);
				String date = movieInfo.optString("date");
				if(IN_THEATRES_TYPE.equals(date)) {
					movieList = movieInfo.getJSONArray("movies");
					break;
				}
			}
			for (int i = 0; i < movieList.length(); i++) {
				JSONObject movie = movieList.getJSONObject(i);
				movies.add(parseMovieJSON(movie));
			}
		} catch (JSONException e) {
			Log.d(TAG, "JSONException");
			e.printStackTrace();
			return movies;
		}
		return movies;
	}
	
	public static ArrayList<Movie> parseOpeningMovieListJSON(JSONArray jsonArray) {
		ArrayList<Movie> movies = new ArrayList<Movie>();
		JSONArray movieList = null;
		try {
			Log.d(TAG, jsonArray.toString(2));
			for(int i = 0; i < jsonArray.length(); i++) {
				JSONObject movieInfo = jsonArray.getJSONObject(i);
				movieList = movieInfo.getJSONArray("movies");
				for (int j = 0; j < movieList.length(); j++) {
					JSONObject movie = movieList.getJSONObject(j);
					movies.add(parseMovieJSON(movie));
				}
			}
			
		} catch (JSONException e) {
			Log.d(TAG, "JSONException");
			e.printStackTrace();
			return movies;
		}
		return movies;
	}

}
