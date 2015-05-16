package org.jingfu.freshmoviebrowser.fragments;

import org.jingfu.freshmoviebrowser.R;
import org.jingfu.freshmoviebrowser.model.Movie;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

public class MovieDetailsFragment extends Fragment {
	private static final String TAG = "MovieDetailsFragment";
	
	public MovieDetailsFragment() {
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RatingBar rating = (RatingBar) getView().findViewById(R.id.movie_details_rating);
		rating.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View details_view = inflater.inflate(R.layout.movie_details_fragment, container, false);
		
		return details_view;
		
	}
	
	
	public void update(Movie movie) {
		Log.d(TAG, "setContent()");

		TextView title = (TextView) getView().findViewById(R.id.movie_details_title);
		RatingBar rating = (RatingBar) getView().findViewById(R.id.movie_details_rating);
		TextView mpaaRating = (TextView) getView().findViewById(R.id.movie_details_mpaa_rating);
		TextView synopsis = (TextView) getView().findViewById(R.id.movie_details_synopsis);

		title.setText(movie.getTitle());
		rating.setRating((float) movie.getRating());
		mpaaRating.setText(movie.getMpaaRating());
		synopsis.setText(movie.getSynopsis());
		
		rating.setVisibility(View.VISIBLE);
	}
	

	public void clear() {
		Log.d(TAG, "clear()");

		TextView title = (TextView) getView().findViewById(R.id.movie_details_title);
		RatingBar rating = (RatingBar) getView().findViewById(R.id.movie_details_rating);
		TextView mpaaRating = (TextView) getView().findViewById(R.id.movie_details_mpaa_rating);
		TextView synopsis = (TextView) getView().findViewById(R.id.movie_details_synopsis);

		title.setText("");
		rating.setRating(0.0f);
		mpaaRating.setText("");
		synopsis.setText("");
		
		rating.setVisibility(View.INVISIBLE);
	}
	
}
