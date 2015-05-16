package org.jingfu.freshmoviebrowser.fragments;

import java.util.ArrayList;

import org.jingfu.freshmoviebrowser.MovieBrowserActivity;
import org.jingfu.freshmoviebrowser.R;
import org.jingfu.freshmoviebrowser.adapter.MovieListAdapter;
import org.jingfu.freshmoviebrowser.interfaces.MovieSelectionListener;
import org.jingfu.freshmoviebrowser.model.Movie;

import android.app.Activity;
import android.app.ListFragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class MovieListFragment extends ListFragment {
	private static final String TAG = "MovieListFragment";
	private static final int NO_SELECTION = -1;

	private ArrayList<Movie> movies = new ArrayList<Movie>();
	private MovieSelectionListener movieSelectionListener;
	private MovieListAdapter movieListAdapter;

	private int selectedItemPosition = NO_SELECTION;
	private boolean isLoading = false;
	
	public MovieListFragment() {

	}

	@Override
	public void onAttach(Activity activity) {
		Log.d(TAG, "onAttach()");
		super.onAttach(activity);
		try {
			movieSelectionListener = (MovieSelectionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement MovieSelectionListener");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.d(TAG, "onActivityCreated()");
		Log.d(TAG, "isLoading = " + isLoading);
		super.onActivityCreated(savedInstanceState);
		movieListAdapter = new MovieListAdapter(getActivity(), R.layout.movie_list_item, R.id.movie_title, movies,
		((MovieBrowserActivity) getActivity()).getImageLoader());
		getListView().setAdapter(movieListAdapter);
		
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		Log.d(TAG, "onDetach()");
		super.onDetach();
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause()");
		super.onPause();
	}

	@Override
	public void onResume() {
		Log.d(TAG, "onResume()");
		super.onResume();
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		setIsLoading(isLoading);
		

		if (movieListAdapter.getCount() > 0 && selectedItemPosition != NO_SELECTION) {
			getListView().setItemChecked(selectedItemPosition, true);
			getListView().setSelection(selectedItemPosition);
			getListView().smoothScrollToPositionFromTop(selectedItemPosition, 200, 0);
			movieSelectionListener.onMovieSelected(movieListAdapter.getItem(selectedItemPosition));
			// If in portrait orientation, display UP button on the movie detail Fragment
			if(Configuration.ORIENTATION_PORTRAIT == getResources().getConfiguration().orientation) {
				getActivity().getActionBar().setHomeButtonEnabled(true);
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
			
		}
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	public void setIsLoading(boolean isLoading) {
		this.isLoading = isLoading;
		setListShown(!isLoading);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Log.d(TAG, "onListItemClick() : " + position);
		selectedItemPosition = position;

		getListView().setItemChecked(selectedItemPosition, true);

		movieSelectionListener.onMovieSelected(movieListAdapter.getItem(selectedItemPosition));
	}

	public void update(ArrayList<Movie> retrievedMovies, boolean typeChanged) {
		Log.d(TAG, "update() : changed = " + typeChanged);
		Log.d(TAG, "update() : selectedItemPosition = " + selectedItemPosition);

		setIsLoading(false);

		movies.clear();
		movies.addAll(retrievedMovies);
		movieListAdapter.notifyDataSetChanged();

		if (typeChanged) {
				getListView().setItemChecked(selectedItemPosition, false);
				selectedItemPosition = NO_SELECTION;
				getListView().setSelection(0);
				SlidingPaneLayout slidingLayout = (SlidingPaneLayout) getActivity().findViewById(R.id.sliding_layout);
				if(!slidingLayout.isOpen()) {
					slidingLayout.openPane();
				}
		} else if (movieListAdapter.getCount() > 0
				&& selectedItemPosition != NO_SELECTION) {
			getListView().setItemChecked(selectedItemPosition, true);
			getListView().setSelection(selectedItemPosition);
			getListView().smoothScrollToPositionFromTop(selectedItemPosition,
					200, 0);
			movieSelectionListener
					.onMovieSelected(movieListAdapter
							.getItem(selectedItemPosition));
		}

	}
	
	public void updateErrorMessage(String message) {
		Log.d(TAG, "updateErrorMessage() : message = " + message);
		setIsLoading(false);
		if(selectedItemPosition != NO_SELECTION) {
			selectedItemPosition = NO_SELECTION;
		}
		movies.clear();
		this.setEmptyText(message);
	}
	
	public int getSelectedItemPosition() {
		return selectedItemPosition;
	}
	

}
